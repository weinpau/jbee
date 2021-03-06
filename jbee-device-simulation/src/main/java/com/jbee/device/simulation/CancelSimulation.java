package com.jbee.device.simulation;

import com.jbee.Velocity;
import com.jbee.commands.CancelCommand;

/**
 *
 * @author weinpau
 */
class CancelSimulation implements CommandSimulation<CancelCommand> {
    
    @Override
    public State simulateState(State initialState, CancelCommand command, long time) {
        return new State(initialState.getPosition(), Velocity.ZERO, initialState.getYaw());
    }

    @Override
    public long calculateDuration(State initialState, CancelCommand command) {
        return 0;

    }

}
