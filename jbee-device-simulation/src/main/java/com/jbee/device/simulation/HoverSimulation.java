package com.jbee.device.simulation;

import com.jbee.Velocity;
import com.jbee.commands.HoverCommand;

/**
 *
 * @author weinpau
 */
class HoverSimulation implements CommandSimulation<HoverCommand> {

    @Override
    public State simulateState(State initialState, HoverCommand command, long time) {
        return new State(initialState.getPosition(), Velocity.ZERO, initialState.getYaw());
    }

    @Override
    public long calculateTimeSpent(State initialState, HoverCommand command) {
        return command.getDuration().toMillis();
    }

}
