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
import com.jbee.buses.BeeStateBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.buses.VelocityBus;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.concurrent.CallbackWrapper;
import com.jbee.device.ardrone2.internal.CommandDispatcher;
import com.jbee.device.ardrone2.internal.commands.AT_COMWDG;
import com.jbee.device.ardrone2.internal.commands.AT_CONFIG;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import com.jbee.device.ardrone2.internal.commands.AT_FTRIM;
import com.jbee.device.ardrone2.internal.commands.AT_REF;
import com.jbee.device.ardrone2.internal.navdata.NavData;
import com.jbee.device.ardrone2.internal.navdata.NavDataClient;
import com.jbee.device.ardrone2.internal.navdata.options.Demo;
import com.jbee.device.ardrone2.internal.navdata.options.OptionId;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.Frequency;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
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
    BeeStateBus beeStateBus;

    AT_CommandSender commandSender;
    NavDataClient navdataClient;
    CommandDispatcher commandDispatcher;

    ControlStateMachine controlStateMachine = ControlStateMachine.init(com.jbee.ControlState.DISCONNECTED);

    volatile BatteryState batteryState = new BatteryState(.99, false);

    int timeout = 1000;
    int navdataPort = 5554;
    int controlPort = 5556;
    String host = "192.168.1.1";
    Duration bootstrapTimeout = Duration.ofSeconds(10);

    static final Random RANDOM = new Random();
    static final OptionId[] NAVDATA_OPTIONS = {OptionId.DEMO};

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
        if (controlStateMachine.getControlState() != com.jbee.ControlState.DISCONNECTED) {
            throw new BeeBootstrapException("Drone is already connected.");
        }
        try {
            beeStateBus = busRegistry.get(BeeStateBus.class).
                    orElseThrow(() -> new BeeBootstrapException("A state bus is missing."));

            initNavClient();
            initCommandSender();
            initCommandDispatcher();

            if (configARDrone()) {
                trim();
                controlStateMachine.changeState(com.jbee.ControlState.READY_FOR_TAKE_OFF);
            } else {
                throw new BeeBootstrapException("AR Drone cannot be configured.");
            }
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException ex) {
            Logger.getLogger(ARDrone2.class.getName()).log(Level.SEVERE, null, ex);
            throw new BeeBootstrapException("AR Drone cannot be configured.", ex);

        } finally {
            navdataClient.removeNavDataReceiver("bootstrap");
        }

    }

    boolean configARDrone() throws InterruptedException, TimeoutException, ExecutionException {
        return Executors.newSingleThreadExecutor().submit(new CallbackWrapper<Boolean>() {

            @Override
            protected void handle() {

                try {
                    commandSender.send(new AT_COMWDG());
                    commandSender.send(new AT_CONFIG("general:navdata_demo", true));
                    commandSender.send(new AT_CONFIG("general:navdata_options", OptionId.mask(NAVDATA_OPTIONS)));
                    commandSender.send(new AT_CONFIG("control:flying_mode", "0"));
//                    commandSender.send(new AT_CONFIG("control:control_vz_max", "200"));
//                    commandSender.send(new AT_CONFIG("control:control_yaw", "2.0"));

                    navdataClient.onNavDataReceived("bootstrap", navdata -> {
                        if (!navdata.getState().isNavDataBootstrap() && navdata.getOption(Demo.class) != null) {
                            navdataHandler.accept(navdata);
                            submit(true);
                        }

                    });
                } catch (InterruptedException e) {
                    submit(false);
                }

            }
        }).get(bootstrapTimeout.toMillis(), TimeUnit.MILLISECONDS);
    }

    void trim() throws InterruptedException {
        commandSender.send(new AT_FTRIM());
        Thread.sleep(200);
    }

    void initCommandDispatcher() {
        commandDispatcher = new CommandDispatcher(commandSender, navdataClient, beeStateBus, controlStateMachine);
    }

    void initCommandSender() throws IOException, UnknownHostException {
        commandSender = new AT_CommandSender(InetAddress.getByName(host), controlPort);
        commandSender.connect();
    }

    void initNavClient() throws UnknownHostException, IOException {
        navdataClient = new NavDataClient(InetAddress.getByName(host), navdataPort, timeout);
        navdataClient.connect();
        navdataClient.onNavDataReceived("navdata-handler", navdataHandler);
    }

    @Override
    public void disconnect() throws IOException {
        if (controlStateMachine.getControlState() != ControlState.DISCONNECTED) {
            try {
                commandSender.send(AT_REF.LAND);
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
            }
        }
        navdataClient.disconnect();
        commandSender.disconnect();
        commandDispatcher.close();
        controlStateMachine.changeStateForced(ControlState.DISCONNECTED);
    }

    public void onNavDataReceived(Consumer<NavData> receiver) {
        String receiverId = String.format("client-%d-%d", System.nanoTime(), RANDOM.nextLong());
        navdataClient.onNavDataReceived(receiverId, receiver);
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

    Consumer<NavData> navdataHandler = new Consumer<NavData>() {

        @Override
        public void accept(NavData navdata) {
            Demo demo = navdata.getOption(Demo.class);
            if (demo != null) {
                publishVelocity(demo);
                publishAxes(demo);
                publishAltitude(demo);
                publishBatteryState(demo, navdata);
            }

        }

        void publishAltitude(Demo demo) {
            Distance altitude = Distance.ofMillimeters(demo.getAltitude());
            altitudeBus.publish(altitude);
        }

        void publishBatteryState(Demo demo, NavData navdata) {
            double percentage = demo.getBatteryPercentage() / 100d;
            batteryState = new BatteryState(percentage, navdata.getState().isBatteryTooLow());
        }

        void publishAxes(Demo demo) {
            principalAxesBus.publish(new PrincipalAxes(
                    Angle.ofDegrees(demo.getYaw()),
                    Angle.ofDegrees(demo.getRoll()),
                    Angle.ofDegrees(demo.getPitch())));
        }

        void publishVelocity(Demo demo) {
            double xSpeed = demo.getSpeedX() / 100d;
            double ySpeed = demo.getSpeedY() / 100d;

            double phi = Math.toRadians(demo.getYaw());
            double directedXSpeed = xSpeed * Math.cos(phi) - ySpeed * Math.sin(phi);
            double directedYSpeed = xSpeed * Math.sin(phi) + ySpeed * Math.cos(phi);
            double zSpeed = demo.getSpeedZ() / 100d;

            Velocity velocity = new Velocity(Speed.mps(directedXSpeed), Speed.mps(directedYSpeed), Speed.mps(zSpeed));
            velocityBus.publish(velocity);
        }

    };

}
