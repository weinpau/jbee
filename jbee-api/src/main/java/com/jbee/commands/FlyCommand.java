package com.jbee.commands;

import com.jbee.Direction;
import com.jbee.units.Distance;
import com.jbee.units.Velocity;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class FlyCommand implements Command {
    
    private final Direction direction;
    private final Distance distance;
    private final Velocity velocity;

    public FlyCommand(Direction direction, Distance distance, Velocity velocity) {
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

    public Velocity getVelocity() {
        return velocity;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.direction);
        hash = 79 * hash + Objects.hashCode(this.distance);
        hash = 79 * hash + Objects.hashCode(this.velocity);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FlyCommand other = (FlyCommand) obj;
        if (this.direction != other.direction) {
            return false;
        }
        if (!Objects.equals(this.distance, other.distance)) {
            return false;
        }
        return Objects.equals(this.velocity, other.velocity);
    }

}
