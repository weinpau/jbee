package com.jbee.device.simulation;

import com.jbee.BeeBootstrapException;
import com.jbee.BeeState;
import com.jbee.TargetDevice;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

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
    public RunnableFuture<CommandResult> execute(Command command) {
        return new FutureTask<>(() -> {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
            }
            return CommandResult.COMPLETED;

        });
    }

    @Override
    public void bootstrap() throws BeeBootstrapException {
    }

}
