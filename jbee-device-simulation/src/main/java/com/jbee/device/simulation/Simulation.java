package com.jbee.device.simulation;

import com.jbee.BeeBootstrapException;
import com.jbee.BeeState;
import com.jbee.TargetDevice;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.units.Distance;
import com.jbee.units.Velocity;
import java.util.concurrent.RunnableFuture;

/**
 *
 * @author weinpau
 */
public class Simulation implements TargetDevice {

    Velocity defaultVelocity = Velocity.mps(1);
    Distance takeOffHeight = Distance.ofMeters(2);

    StateMachine stateMachine;

    @Override
    public String getId() {
        return "simulation";
    }

    @Override
    public BeeState getCurrentState() {
        return stateMachine.getCurrentState();
    }

    @Override
    public RunnableFuture<CommandResult> execute(Command command) {
        return stateMachine.execute(command);
    }

    @Override
    public void bootstrap() throws BeeBootstrapException {
        stateMachine = new StateMachine(defaultVelocity, takeOffHeight);
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

}
