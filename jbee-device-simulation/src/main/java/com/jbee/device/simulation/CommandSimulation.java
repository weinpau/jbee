package com.jbee.device.simulation;

import com.jbee.commands.Command;

/**
 *
 * @author weinpau
 */
interface CommandSimulation<T extends Command> {

    State simulateState(State initialState, T command, long time);

    long calculateTimeSpent(State initialState, T command);

}
