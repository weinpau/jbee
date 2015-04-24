package com.jbee;

import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;

/**
 *
 * @author weinpau
 */
public interface TargetDevice {

    String getId();

    BeeState getCurrentState();

    CommandResult execute(Command command);

}
