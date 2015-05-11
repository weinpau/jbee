package com.jbee.device.simulation;

import com.jbee.BatteryState;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeModule;
import com.jbee.ControlState;
import com.jbee.PrincipalAxes;
import com.jbee.TargetDevice;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.commands.LandCommand;
import com.jbee.commands.TakeOffCommand;
import com.jbee.buses.VelocityBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.Frequency;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
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
    ControlState controlState = ControlState.DISCONNECTED;
    StateMachine stateMachine;

    VelocityBus velocityBus = new VelocityBus();
    PrincipalAxesBus principalAxesBus = new PrincipalAxesBus();

    Timer stateListener = new Timer("simulation-state-listener", true);

    TimerTask stateTimerTask = new TimerTask() {
        @Override
        public void run() {

            State state = stateMachine.getCurrentState();
            velocityBus.publish(state.getVelocity());
            principalAxesBus.publish(new PrincipalAxes(state.getYAW(), Angle.ZERO, Angle.ZERO));
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

                if (controlState != ControlState.READY_FOR_TAKE_OFF) {
                    return CommandResult.NOT_EXECUTED;
                }
                controlState = ControlState.TAKING_OFF;
            } else if (command instanceof LandCommand) {

                if (controlState != ControlState.FLYING) {
                    return CommandResult.NOT_EXECUTED;
                }

                controlState = ControlState.LANDING;
            } else {
                controlState = ControlState.FLYING;
            }

            CommandResult result = stateMachine.execute(command);
            if (command instanceof LandCommand) {
                controlState = ControlState.READY_FOR_TAKE_OFF;
            }
            if (command instanceof TakeOffCommand) {
                controlState = ControlState.FLYING;
            }
            return result;

        });

    }

    @Override
    public void bootstrap() throws BeeBootstrapException {
        stateMachine = new StateMachine(defaultSpeed, takeOffAltitude);
        controlState = ControlState.READY_FOR_TAKE_OFF;
        stateListener.scheduleAtFixedRate(stateTimerTask, 0,
                transmissionRate.toCycleDuration().toMillis());

    }

    @Override
    public void disconnect() throws IOException {
        controlState = ControlState.DISCONNECTED;
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
        return controlState;
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
