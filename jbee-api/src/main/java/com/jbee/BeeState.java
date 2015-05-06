package com.jbee;

import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Velocity3D;

/**
 *
 * @author weinpau
 */
public class BeeState {

    public static final BeeState START_STATE = new BeeState(0,
            Position.ORIGIN,
            Velocity3D.ZERO,
            PrincipalAxes.ZERO,
            new BatteryState(1, false),
            ControlState.DISCONNECTED);

    private final long timestamp;
    private final Position position;
    private final Velocity3D translationalVelocity;
    private final PrincipalAxes principalAxes;
    private final BatteryState batteryState;
    private final ControlState controlState;

    BeeState(long timestamp, Position position, Velocity3D translationalVelocity, PrincipalAxes principalAxes, BatteryState batteryState, ControlState controlState) {
        this.timestamp = timestamp;
        this.position = position;
        this.translationalVelocity = translationalVelocity;

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

    public Velocity3D getTranslationalVelocity() {
        return translationalVelocity;
    }

    public PrincipalAxes getPrincipalAxes() {
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

}
