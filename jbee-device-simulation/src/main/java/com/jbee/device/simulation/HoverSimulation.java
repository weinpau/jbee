package com.jbee.device.simulation;

import com.jbee.GlobalVelocity;
import com.jbee.commands.HoverCommand;

/**
 *
 * @author weinpau
 */
class HoverSimulation implements CommandSimulation<HoverCommand> {

    @Override
    public State simulateState(State initialState, HoverCommand command, long time) {
        return new State(initialState.getPosition(), GlobalVelocity.ZERO, initialState.getYaw());
    }

    @Override
    public long calculateDuration(State initialState, HoverCommand command) {
        return command.getDuration().toMillis();
    }

}
