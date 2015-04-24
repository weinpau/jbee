package com.jbee.commands;

import com.jbee.units.YAW;

/**
 *
 * @author weinpau
 */
public interface RotationCommand extends Command {
 
    CommandPromise rotate(YAW yaw);
    
}
