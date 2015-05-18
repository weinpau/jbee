package com.jbee.device.simulation;

import com.jbee.RotationDirection;
import com.jbee.Velocity;
import com.jbee.commands.FlyCommand;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Speed;

/**
 *
 * @author weinpau
 */
class FlySimulation implements CommandSimulation<FlyCommand> {

    @Override
    public State simulateState(State initialState, FlyCommand command, long time) {
        double progress = calculateProgress(initialState, command, time);

        Angle yaw = calculateYAW(initialState, command, progress);
        Position position = calculatePosition(initialState, command, progress);

        Velocity velocity = calculateVelocity(initialState, command, progress);

        return new State(position, velocity, yaw);
    }

    double calculateProgress(State initialState, FlyCommand command, long time) {
        long timeSpent = calculateFlyTimeSpent(initialState, command);

        if (timeSpent > 0) {
            return time / (double) timeSpent;
        }
        return 1;
    }

    Angle calculateYAW(State initialState, FlyCommand command, double progress) {
        Angle deltaYAW = command.calculateDeltaYAW(initialState.getYaw());
        Angle yaw = initialState.getYaw().add(deltaYAW.multiply(progress)).normalize();
        if (command.getRotationDirection() == RotationDirection.CLOCKWISE) {
            yaw = initialState.getYaw().sub(deltaYAW.multiply(progress)).normalize();
        }
        return yaw;
    }

    @Override
    public long calculateTimeSpent(State initialState, FlyCommand command) {
        return Math.max(
                calculateFlyTimeSpent(initialState, command),
                calculateRotationTimeSpent(initialState, command));
    }

    long calculateFlyTimeSpent(State initialState, FlyCommand command) {
        Position startPosition = Position.ORIGIN;
        if (!command.isRealtivePosition()) {
            startPosition = initialState.getPosition();
        }
        return Math.abs(command.getPosition().distance(startPosition).divide(command.getSpeed()).toMillis());
    }

    long calculateRotationTimeSpent(State initialState, FlyCommand command) {
        Angle deltaYAW = command.calculateDeltaYAW(initialState.getYaw());
        return Math.abs(deltaYAW.divide(command.getRotationalSpeed().toAngularSpeed()).toMillis());
    }

    Velocity calculateVelocity(State initialState, FlyCommand command, double progress) {

        if (command.getSpeed().isZero()) {
            return Velocity.ZERO;
        }

        Position a = initialState.getPosition();
        Position b = calculatePosition(initialState, command, progress);

        Position p = b.sub(a).
                normalize().
                multiply(command.getSpeed().mps());

        return new Velocity(
                Speed.mps(p.getX()),
                Speed.mps(p.getY()),
                Speed.mps(p.getZ()));
    }

    Position calculatePosition(State initialState, FlyCommand command, double progress) {

        if (command.getSpeed().isZero()) {
            return initialState.getPosition();
        }

        Position a = initialState.getPosition();
        Position b = command.calculateTargetPosition(a, initialState.getYaw());

        return b.sub(a).
                normalize().
                multiply(b.distance(a).toMeters()).
                multiply(progress).
                add(a);

    }

}
