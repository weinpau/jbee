package com.jbee.commands;

import com.jbee.BeeControl;

/**
 * Interface for a command.
 *
 * @author weinpau
 */
public interface Command {

    /**
     * Initialization of the command with the given number and control.
     *
     * @param commandNumber number of the command
     * @param executiveControl executive control
     */
    void init(int commandNumber, BeeControl executiveControl);

    /**
     * Returns the number of the command.
     *
     * @return the number of the command
     */
    int getCommandNumber();

    /**
     * Cancels the command
     *
     * @return {@code true}, if the cancellation was successful
     */
    boolean cancel();

}
