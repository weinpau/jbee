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
import com.jbee.buses.TranslationalVelocityBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.Frequency;
import com.jbee.units.Velocity;
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

    Velocity defaultVelocity = Velocity.mps(1);
    Distance takeOffHeight = Distance.ofMeters(2);
    BatteryState batteryState = new BatteryState(.99, false);
    ControlState controlState = ControlState.DISCONNECTED;
    StateMachine stateMachine;

    TranslationalVelocityBus velocityBus = new TranslationalVelocityBus();
    PrincipalAxesBus principalAxesBus = new PrincipalAxesBus();

    Timer stateListener = new Timer("simulation-state-listener", true);

    TimerTask stateTimerTask = new TimerTask() {
        @Override
        public void run() {
            long time = System.currentTimeMillis();
            SimulationStep step = stateMachine.getCurrentStep();

            velocityBus.publish(step.simulateTranslationalVelocity(time));
            principalAxesBus.publish(new PrincipalAxes(step.simulateYAW(time), Angle.ZERO, Angle.ZERO));
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
        stateMachine = new StateMachine(defaultVelocity, takeOffHeight);
        controlState = ControlState.READY_FOR_TAKE_OFF;
        stateListener.scheduleAtFixedRate(stateTimerTask, 0,
                transmissionRate.toCycleDuration().toMillis());

    }

    @Override
    public void disconnect() throws IOException {
        controlState = ControlState.DISCONNECTED;
        stateTimerTask.cancel();
    }

    public Velocity getDefaultVelocity() {
        return defaultVelocity;
    }

    public void setDefaultVelocity(Velocity defaultVelocity) {
        this.defaultVelocity = defaultVelocity;
    }

    public Distance getTakeOffHeight() {
        return takeOffHeight;
    }

    public void setTakeOffHeight(Distance takeOffHeight) {
        this.takeOffHeight = takeOffHeight;
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

}
