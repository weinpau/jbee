package com.jbee.device.simulation;

import com.jbee.AxisVelocity;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;

/**
 *
 * @author weinpau
 */
class State {

    static final State INITIAL_STATE = new State(Position.ORIGIN, AxisVelocity.ZERO, Angle.ZERO);

    final Position position;
    final AxisVelocity velocity;
    final Angle yaw;

    public State(Position position, AxisVelocity velocity, Angle yaw) {

        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
    }

    public Position getPosition() {
        return position;
    }

    public AxisVelocity getVelocity() {
        return velocity;
    }

    public Angle getYaw() {
        return yaw;
    }

}
