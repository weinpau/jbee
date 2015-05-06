package com.jbee.device.simulation;

import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Speed;

/**
 *
 * @author weinpau
 */
class State {

    static final State START_STATE = new State(Position.ORIGIN, Speed.ZERO, Angle.ZERO);

    final Position position;
    final Speed speed;
    final Angle yaw;

    public State(Position position, Speed speed, Angle yaw) {

        this.position = position;
        this.speed = speed;
        this.yaw = yaw;
    }

    public Position getPosition() {
        return position;
    }

    public Speed getSpeed() {
        return speed;
    }

    public Angle getYAW() {
        return yaw;
    }

}
