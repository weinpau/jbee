package com.jbee;

import com.jbee.positioning.Position;
import com.jbee.units.Velocity;
import com.jbee.units.Angle;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class BeeState {

    public static final BeeState START_STATE = new BeeState(0, Position.ORIGIN, Velocity.ZERO, Angle.ZERO);

    private final long timestamp;
    private final Position position;
    private final Velocity velocity;
    private final Angle yaw;

    BeeState(long timestamp, Position position, Velocity velocity, Angle yaw) {
        this.timestamp = timestamp;
        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Position getPosition() {
        return position;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public Angle getYAW() {
        return yaw;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
        hash = 89 * hash + Objects.hashCode(this.position);
        hash = 89 * hash + Objects.hashCode(this.velocity);
        hash = 89 * hash + Objects.hashCode(this.yaw);
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
        final BeeState other = (BeeState) obj;
        if (this.timestamp != other.timestamp) {
            return false;
        }
        if (!Objects.equals(this.position, other.position)) {
            return false;
        }
        if (!Objects.equals(this.velocity, other.velocity)) {
            return false;
        }
        return Objects.equals(this.yaw, other.yaw);
    }

}
