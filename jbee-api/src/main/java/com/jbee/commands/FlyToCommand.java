package com.jbee.commands;

import com.jbee.Position;
import com.jbee.units.Velocity;
import com.jbee.units.YAW;

/**
 *
 * @author weinpau
 */
public interface FlyToCommand extends Command {

    CommandPromise flyTo(Position position, YAW yaw, Velocity velocity);

}
