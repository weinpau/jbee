package com.jbee.device.ardrone2;

import com.jbee.BatteryState;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeModule;
import com.jbee.BusRegistry;
import com.jbee.ControlState;
import com.jbee.ControlStateMachine;
import com.jbee.PrincipalAxes;
import com.jbee.TargetDevice;
import com.jbee.Velocity;
import com.jbee.buses.AltitudeBus;
import com.jbee.buses.PositionBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.buses.VelocityBus;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.device.ardrone2.internal.CommandDispatcher;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import com.jbee.device.ardrone2.internal.navdata.NavDataClient;
import com.jbee.device.ardrone2.internal.navdata.options.Demo;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.Frequency;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author weinpau
 */
public class ARDrone2 extends BeeModule implements TargetDevice {

    Frequency transmissionRate = Frequency.ofHz(15);

    AltitudeBus altitudeBus = new AltitudeBus();
    VelocityBus velocityBus = new VelocityBus();
    PrincipalAxesBus principalAxesBus = new PrincipalAxesBus();

    AT_CommandSender commandSender;
    NavDataClient navdataClient;
    CommandDispatcher commandDispatcher;

    ControlStateMachine controlStateMachine = ControlStateMachine.init(ControlState.DISCONNECTED);

    volatile BatteryState batteryState = new BatteryState(.99, false);

    int timeout = 1000;
    int navdataPort = 5554;
    int controlPort = 5556;
    String host = "192.168.1.1";

    public ARDrone2() {

        register(altitudeBus);
        register(velocityBus);
        register(principalAxesBus);
    }

    @Override
    public String getId() {
        return "ar-drone2";
    }

    @Override
    public RunnableFuture<CommandResult> execute(Command command) {
        return new FutureTask<>(() -> {
            return commandDispatcher.dispatch(command);
        });

    }

    @Override
    public void bootstrap(BusRegistry busRegistry) throws BeeBootstrapException {
        if (controlStateMachine.getControlState() != ControlState.DISCONNECTED) {
            throw new RuntimeException("Drone is already connected.");
        }

        busRegistry.get(PositionBus.class).
                orElseThrow(() -> new BeeBootstrapException("A position bus is missing."));

        try {
            initNavClient();
            initCommandSender();
            initCommandDispatcher();

            controlStateMachine.changeState(ControlState.READY_FOR_TAKE_OFF);

        } catch (IOException ex) {
            Logger.getLogger(ARDrone2.class.getName()).log(Level.SEVERE, null, ex);
            throw new BeeBootstrapException(ex);
        }
    }

    void initCommandDispatcher() {
        commandDispatcher = new CommandDispatcher(commandSender, navdataClient, controlStateMachine);
    }

    void initCommandSender() throws IOException, UnknownHostException {
        commandSender = new AT_CommandSender(InetAddress.getByName(host), controlPort);
        commandSender.connect();
    }

    void initNavClient() throws UnknownHostException, IOException {
        navdataClient = new NavDataClient(InetAddress.getByName(host), navdataPort, timeout);
        navdataClient.connect();
        navdataClient.onNavDataReceived("main", n -> {

            Demo demo = n.getOption(Demo.class);
            if (demo != null) {
                handleVelocityBus(demo);

                principalAxesBus.publish(new PrincipalAxes(
                        Angle.ofDegrees(demo.getYaw()),
                        Angle.ofDegrees(demo.getRoll()),
                        Angle.ofDegrees(demo.getPitch())));

                altitudeBus.publish(Distance.ofMillimeters(demo.getAltitude()));

                batteryState = new BatteryState(demo.getBatteryPercentage() / 100d, n.getState().isBatteryTooLow());

            }

        });
    }

    void handleVelocityBus(Demo demo) {
        double x = demo.getSpeedX() / 1000d;
        double y = demo.getSpeedY() / 1000d;

        double phi = Math.toRadians(demo.getYaw());

        velocityBus.publish(new Velocity(
                Speed.mps(x * Math.cos(phi) - y * Math.sin(phi)),
                Speed.mps(x * Math.sin(phi) + y * Math.cos(phi)),
                Speed.mps(demo.getSpeedZ() / 1000d)));
    }

    @Override
    public void disconnect() throws IOException {
        if (!controlStateMachine.changeState(ControlState.DISCONNECTED)) {
            throw new RuntimeException("Drone is not connected");
        }
        navdataClient.disconnect();
        commandSender.disconnect();

    }

    @Override
    public ControlState getControlState() {
        return controlStateMachine.getControlState();
    }

    @Override
    public BatteryState getBatteryState() {
        return batteryState;
    }

    @Override
    public Frequency getTransmissionRate() {
        return transmissionRate;
    }

    @Override
    public Speed getMaxSpeed() {
        return Speed.mps(3);
    }

    @Override
    public Speed getDefaultSpeed() {
        return Speed.mps(1);
    }

    @Override
    public RotationalSpeed getMaxRotationalSpeed() {
        return RotationalSpeed.rps(1);
    }

    @Override
    public RotationalSpeed getDefaultRotationalSpeed() {
        return RotationalSpeed.rps(.25);
    }

}
