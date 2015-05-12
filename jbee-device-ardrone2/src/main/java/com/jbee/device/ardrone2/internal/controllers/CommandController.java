package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;

/**
 *
 * @author weinpau
 * @param <T>
 */
public interface CommandController<T extends Command> {

    CommandResult execute(T command);

}
