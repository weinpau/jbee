package com.jbee.commands;

import com.jbee.units.Distance;

/**
 *
 * @author weinpau
 */
public interface TakeOffCommand extends Command {
    
    CommandResult takeOff(Distance height);
}
