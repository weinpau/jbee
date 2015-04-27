package com.jbee.device.simulation;

import com.jbee.BeeBootstrapException;
import com.jbee.BeeState;
import com.jbee.TargetDevice;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;

/**
 *
 * @author weinpau
 */
public class Simulation implements TargetDevice {

    @Override
    public String getId() {
        return "simulation";
    }

    @Override
    public BeeState getCurrentState() {
        return BeeState.START_STATE;
    }

    @Override
    public CommandResult execute(Command command) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException interruptedException) {
        }
        return CommandResult.COMPLETED;
    }

    @Override
    public void bootstrap() throws BeeBootstrapException {
    }

}
