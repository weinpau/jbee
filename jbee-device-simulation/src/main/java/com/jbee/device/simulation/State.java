package com.jbee.device.simulation;

import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Velocity;

/**
 *
 * @author weinpau
 */
class State {

    static final State START_STATE = new State(Position.ORIGIN, Velocity.ZERO, Angle.ZERO);

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

    public Angle getYAW() {
        return yaw;
    }

}
