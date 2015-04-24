package com.jbee;

import com.jbee.positioning.Position;
import com.jbee.units.Velocity;
import com.jbee.units.YAW;

/**
 *
 * @author weinpau
 */
public interface BeeState {

    long getTimestamp();

    Position getPosition();

    Velocity getVelocity();
    
    YAW getYAW();

}
