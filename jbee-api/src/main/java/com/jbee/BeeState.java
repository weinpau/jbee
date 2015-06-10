package com.jbee;

import com.jbee.positioning.Position;

/**
 *
 * @author weinpau
 */
public class BeeState {

    public static final BeeState START_STATE = new BeeState(0,
            Position.ORIGIN,
            Velocity.ZERO,
            AxisAngles.ZERO,
            new BatteryState(1, false),
            ControlState.DISCONNECTED);

    private final long timestamp;
    private final Position position;
    private final Velocity globalVelocity;
    private final AxisAngles principalAxes;
    private final BatteryState batteryState;
    private final ControlState controlState;

    BeeState(long timestamp, Position position, Velocity velocity, AxisAngles principalAxes, BatteryState batteryState, ControlState controlState) {
        this.timestamp = timestamp;
        this.position = position;
        this.globalVelocity = velocity;

        this.principalAxes = principalAxes;
        this.batteryState = batteryState;
        this.controlState = controlState;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Position getPosition() {
        return position;
    }

    public Velocity getGlobalVelocity() {
        return globalVelocity;
    }

    public AxisAngles getPrincipalAxes() {
        return principalAxes;
    }

    public BatteryState getBatteryState() {
        return batteryState;
    }

    public ControlState getControlState() {
        return controlState;
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

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("BeeState{").append("\n");
        stringBuilder.append("\t").append("timestamp=").append(timestamp).append("\n");
        stringBuilder.append("\t").append("controlState=").append(controlState).append("\n");
        stringBuilder.append("\t").append("batteryState=").append(batteryState).append("\n");
        stringBuilder.append("\t").append("position=").append(position).append("\n");
        stringBuilder.append("\t").append("velocity=").append(globalVelocity).append("\n");
        stringBuilder.append("\t").append("principalAxes=").append(principalAxes).append("\n");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

}
