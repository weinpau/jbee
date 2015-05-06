package com.jbee.commands;

import com.jbee.Direction;
import com.jbee.units.Distance;
import com.jbee.units.Speed;

/**
 *
 * @author weinpau
 */
public class FlyCommand extends AbstractCommand {

    private final Direction direction;
    private final Distance distance;
    private final Speed velocity;

    public FlyCommand(Direction direction, Distance distance, Speed velocity) {
        this.direction = direction;
        this.distance = distance;
        this.velocity = velocity;
    }

    public Direction getDirection() {
        return direction;
    }

    public Distance getDistance() {
        return distance;
    }

    public Speed getVelocity() {
        return velocity;
    }

}
