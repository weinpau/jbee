package com.jbee.commands;

import com.jbee.units.YAW;

/**
 *
 * @author weinpau
 */
public interface RotationCommand extends Command {
 
    void rotate(YAW yaw);
    
}
