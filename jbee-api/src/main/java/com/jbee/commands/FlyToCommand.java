package com.jbee.commands;

import com.jbee.Position;
import com.jbee.units.Velocity;
import com.jbee.units.YAW;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class FlyToCommand implements Command {

    private final Position position;
    private final YAW yaw;
    private final Velocity velocity;

    public FlyToCommand(Position position, YAW yaw, Velocity velocity) {
        this.position = position;
        this.yaw = yaw;
        this.velocity = velocity;
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
        if (!Objects.equals(this.velocity, other.velocity)) {
            return false;
        }
        return true;
    }

}
