package com.jbee.commands;

import com.jbee.positioning.Position;
import com.jbee.units.Velocity;
import com.jbee.units.YAW;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class FlyToCommand implements Command {

    private final Position position;

    private final Velocity velocity;
    private final YAW yaw;

    public FlyToCommand(Position position, Velocity velocity, YAW yaw) {
        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
    }

    public FlyToCommand(Position position, Velocity velocity) {
        this(position, velocity, null);
    }

    public Position getPosition() {
        return position;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public YAW getYAW() {
        return yaw;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.position);
        hash = 79 * hash + Objects.hashCode(this.yaw);
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
        final FlyToCommand other = (FlyToCommand) obj;
        if (!Objects.equals(this.position, other.position)) {
            return false;
        }
        if (!Objects.equals(this.yaw, other.yaw)) {
            return false;
        }
        return Objects.equals(this.velocity, other.velocity);
    }

}
