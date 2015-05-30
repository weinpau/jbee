package com.jbee.device.simulation;

import com.jbee.BatteryState;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeModule;
import com.jbee.BusRegistry;
import com.jbee.ControlState;
import com.jbee.PrincipalAxes;
import com.jbee.TargetDevice;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.buses.AxisVelocityBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.Frequency;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import com.jbee.ControlStateMachine;
import com.jbee.commands.LandCommand;
import com.jbee.commands.TakeOffCommand;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 *
 * @author weinpau
 */
public class Simulation extends BeeModule implements TargetDevice {

    Frequency transmissionRate = Frequency.ofHz(20);

    Speed defaultSpeed = Speed.mps(1);
    Speed maxSpeed = Speed.mps(10);
    RotationalSpeed defaultRotationalSpeed = RotationalSpeed.rps(1);
    RotationalSpeed maxRotationalSpeed = RotationalSpeed.rps(5);
    Distance takeOffAltitude = Distance.ofMeters(2);
    BatteryState batteryState = new BatteryState(.99, false);

    CommandDispatcher dispatcher;

    AxisVelocityBus velocityBus = new AxisVelocityBus();
    PrincipalAxesBus principalAxesBus = new PrincipalAxesBus();

    Timer stateListener = new Timer("simulation-state-listener", true);

    ControlStateMachine controlStateMachine = ControlStateMachine.init(ControlState.DISCONNECTED);

    TimerTask stateTimerTask = new TimerTask() {
        @Override
        public void run() {

            State state = dispatcher.getCurrentState();
            velocityBus.publish(state.getVelocity());
            principalAxesBus.publish(new PrincipalAxes(state.getYaw(), Angle.ZERO, Angle.ZERO));
        }
    };

    public Simulation() {
        register(velocityBus);
        register(principalAxesBus);
    }

    @Override
    public String getId() {
        return "simulation";
    }

    @Override
    public RunnableFuture<CommandResult> execute(Command command) {

        return new FutureTask<>(() -> {
            if (command instanceof TakeOffCommand) {
                if (!controlStateMachine.changeState(ControlState.TAKING_OFF)) {
                    return CommandResult.NOT_EXECUTED;
                }
            } else if (command instanceof LandCommand) {
                if (!controlStateMachine.changeState(ControlState.LANDING)) {
                    return CommandResult.NOT_EXECUTED;
                }
            } else {
                if (!controlStateMachine.changeState(ControlState.FLYING)) {
                    return CommandResult.NOT_EXECUTED;
                }
            }

            CommandResult result = dispatcher.execute(command);

            if (command instanceof LandCommand) {
                controlStateMachine.changeState(ControlState.READY_FOR_TAKE_OFF);
            }
            if (command instanceof TakeOffCommand) {
                controlStateMachine.changeState(ControlState.FLYING);
            }
            return result;

        });

    }

    @Override
    public void bootstrap(BusRegistry busRegistry) throws BeeBootstrapException {
        if (controlStateMachine.getControlState() != ControlState.DISCONNECTED) {
            throw new RuntimeException("Simulation is already connected.");
        }

        dispatcher = new CommandDispatcher(defaultSpeed, takeOffAltitude);
        controlStateMachine.changeState(ControlState.READY_FOR_TAKE_OFF);
        stateListener.scheduleAtFixedRate(stateTimerTask, 0,
                transmissionRate.toCycleDuration().toMillis());

    }

    @Override
    public void disconnect() throws IOException {
        if (!controlStateMachine.changeState(ControlState.DISCONNECTED)) {
            throw new RuntimeException("Simulation is not connected");
        }
        stateTimerTask.cancel();
    }

    @Override
    public Speed getDefaultSpeed() {
        return defaultSpeed;
    }

    public void setDefaultSpeed(Speed defaultSpeed) {
        this.defaultSpeed = defaultSpeed;
    }

    @Override
    public RotationalSpeed getDefaultRotationalSpeed() {
        return defaultRotationalSpeed;
    }

    public void setDefaultRotationalSpeed(RotationalSpeed defaultRotationalSpeed) {
        this.defaultRotationalSpeed = defaultRotationalSpeed;
    }

    public Distance getTakeOffAltitude() {
        return takeOffAltitude;
    }

    public void setTakeOffAltitude(Distance takeOffAltitude) {
        this.takeOffAltitude = takeOffAltitude;
    }

    public void setBatteryState(BatteryState batteryState) {
        this.batteryState = batteryState;
    }

    @Override
    public BatteryState getBatteryState() {
        return batteryState;
    }

    @Override
    public ControlState getControlState() {
        return controlStateMachine.getControlState();
    }

    @Override
    public Frequency getTransmissionRate() {
        return transmissionRate;
    }

    public void setTransmissionRate(Frequency transmissionRate) {
        this.transmissionRate = transmissionRate;
    }

    @Override
    public Speed getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Speed maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public RotationalSpeed getMaxRotationalSpeed() {
        return maxRotationalSpeed;
    }

    public void setMaxRotationalSpeed(RotationalSpeed maxRotationalSpeed) {
        this.maxRotationalSpeed = maxRotationalSpeed;
    }

}
