package com.jbee.device.simulation;

import com.jbee.Velocity;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;

/**
 *
 * @author weinpau
 */
class State {

    static final State INITIAL_STATE = new State(Position.ORIGIN, Velocity.ZERO, Angle.ZERO);

    final Position position;
    final Velocity velocity;
    final Angle yaw;

    public State(Position position, Velocity velocity, Angle yaw) {

        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
    }

    public Position getPosition() {
        return position;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public Angle getYaw() {
        return yaw;
    }

}
