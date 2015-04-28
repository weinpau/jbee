package com.jbee.device.simulation;

import com.jbee.BeeBootstrapException;
import com.jbee.BeeState;
import com.jbee.TargetDevice;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.units.Velocity;
import java.util.concurrent.RunnableFuture;

/**
 *
 * @author weinpau
 */
public class Simulation implements TargetDevice {

    Velocity defaultVelocity = Velocity.mps(1);
    
    StateMachine stateMachine = new StateMachine(defaultVelocity);
    
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
    }

}
