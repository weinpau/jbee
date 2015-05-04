package com.jbee.device.simulation;

import com.jbee.BatteryState;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeModule;
import com.jbee.ControlState;
import com.jbee.TargetDevice;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.commands.LandCommand;
import com.jbee.commands.TakeOffCommand;
import com.jbee.providers.PositionProvider;
import com.jbee.providers.VelocityProvider;
import com.jbee.providers.YAWProvider;
import com.jbee.units.Distance;
import com.jbee.units.Velocity;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 *
 * @author weinpau
 */
public class Simulation extends BeeModule implements TargetDevice {

    Velocity defaultVelocity = Velocity.mps(1);
    Distance takeOffHeight = Distance.ofMeters(2);
    BatteryState batteryState = new BatteryState(.99, false);
    ControlState controlState = ControlState.DISCONNECTED;
    StateMachine stateMachine;

    public Simulation() {

        register((PositionProvider) () -> {
            return stateMachine.getCurrentState().getPosition();
        });
        register((VelocityProvider) () -> {
            return stateMachine.getCurrentState().getVelocity();
        });
        register((YAWProvider) () -> {
            return stateMachine.getCurrentState().getYAW();
        });

    }

    @Override
    public String getId() {
        return "simulation";
    }

    @Override
    public RunnableFuture<CommandResult> execute(Command command) {
        if (command instanceof TakeOffCommand) {
            controlState = ControlState.TAKING_OFF;
        } else if (command instanceof LandCommand) {
            controlState = ControlState.LANDING;
        } else {
            controlState = ControlState.FLYING;
        }

        return new FutureTask<>(() -> {
            CommandResult result = stateMachine.execute(command);            
            if (command instanceof LandCommand) {
                controlState = ControlState.READY_FOR_TAKE_OFF;
            }            
            if (command instanceof TakeOffCommand)
                controlState = ControlState.FLYING;            
            return result;

        });

    }

    @Override
    public void bootstrap() throws BeeBootstrapException {
        controlState = ControlState.BOOTSTRAP;
        stateMachine = new StateMachine(defaultVelocity, takeOffHeight);
        controlState = ControlState.READY_FOR_TAKE_OFF;
    }

    @Override
    public void disconnect() {
        controlState = ControlState.DISCONNECTED;
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

}
