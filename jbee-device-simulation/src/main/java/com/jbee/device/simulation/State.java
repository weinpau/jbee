package com.jbee.device.simulation;

import com.jbee.positioning.Position;
import com.jbee.units.Velocity;
import com.jbee.units.YAW;

/**
 *
 * @author weinpau
 */
public class State {

    public static final State START_STATE = new State(Position.ORIGIN, Velocity.ZERO, YAW.ZERO);

    private final Position position;
    private final Velocity velocity;
    private final YAW yaw;

    public State(Position position, Velocity velocity, YAW yaw) {

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

    public YAW getYAW() {
        return yaw;
    }

}
