package com.jbee.device.simulation;

import com.jbee.BeeState;
import com.jbee.positioning.Position;
import com.jbee.units.Velocity;
import com.jbee.units.YAW;

/**
 *
 * @author weinpau
 */
public class SimulationState implements BeeState {

    private final long timestamp;
    private final Position position;
    private final Velocity velocity;
    private final YAW yaw;

    public SimulationState(Position position, Velocity velocity, YAW yaw) {
        this.timestamp = System.currentTimeMillis();
        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
    }

    public SimulationState(long timestamp, Position position, Velocity velocity, YAW yaw) {
        this.timestamp = timestamp;
        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Velocity getVelocity() {
        return velocity;
    }

    @Override
    public YAW getYAW() {
        return yaw;
    }

}
