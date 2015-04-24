package com.jbee.commands;

import com.jbee.Direction;
import com.jbee.units.Distance;
import com.jbee.units.Velocity;

/**
 *
 * @author weinpau
 */
public interface FlyCommand extends Command {

    void fly(Direction direction, Distance distance, Velocity velocity);

}
