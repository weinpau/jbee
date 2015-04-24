package com.jbee.commands;

import com.jbee.units.YAW;

/**
 *
 * @author weinpau
 */
public interface RotationCommand extends Command {
 
    CommandResult rotate(YAW yaw);
    
}
