package com.jbee;

import com.jbee.positioning.Position;
import com.jbee.units.Velocity;
import com.jbee.units.Angle;

/**
 *
 * @author weinpau
 */
public class BeeState {

    public static final BeeState START_STATE = new BeeState(0, Position.ORIGIN, Velocity.ZERO, Angle.ZERO, new BatteryState(1, false));

    private final long timestamp;
    private final Position position;
    private final Velocity velocity;
    private final Angle yaw;
    private final BatteryState batteryState;

    BeeState(long timestamp, Position position, Velocity velocity, Angle yaw, BatteryState batteryState) {
        this.timestamp = timestamp;
        this.position = position;
        this.velocity = velocity;
       
        this.yaw = yaw;
        this.batteryState = batteryState;
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

    public BatteryState getBatteryState() {
        return batteryState;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
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
        return this.timestamp == other.timestamp;
    }

}
