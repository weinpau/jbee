package com.jbee.device.simulation;

import com.jbee.GlobalVelocity;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;

/**
 *
 * @author weinpau
 */
class State {

    static final State INITIAL_STATE = new State(Position.ORIGIN, GlobalVelocity.ZERO, Angle.ZERO);

    final Position position;
    final GlobalVelocity velocity;
    final Angle yaw;

    public State(Position position, GlobalVelocity velocity, Angle yaw) {

        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
    }

    public Position getPosition() {
        return position;
    }

    public GlobalVelocity getVelocity() {
        return velocity;
    }

    public Angle getYaw() {
        return yaw;
    }

}
