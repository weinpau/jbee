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
        Angle deltaYAW = calculateDeltaYAW(initialState, command);
        Angle yaw = initialState.getYAW().add(deltaYAW.multiply(progress)).normalize();
        if (command.getRotationDirection() == RotationDirection.CLOCKWISE) {
            yaw = initialState.getYAW().sub(deltaYAW.multiply(progress)).normalize();
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
        Angle deltaYAW = calculateDeltaYAW(initialState, command);
        return Math.abs(deltaYAW.divide(command.getRotationalSpeed().toAngularSpeed()).toMillis());
    }

    Angle calculateDeltaYAW(State initialState, FlyCommand command) {
        Angle yaw = initialState.getYAW();
        Angle commandYAW = command.getAngle();
        if (command.isRealtiveRotation()) {
            if (command.getRotationDirection() == RotationDirection.CLOCKWISE) {
                return commandYAW;
            } else {
                return commandYAW.multiply(-1);
            }
        } else {
            commandYAW = commandYAW.normalize();
            if ((yaw.compareTo(commandYAW) >= 0 && command.getRotationDirection() == RotationDirection.CLOCKWISE)
                    || (yaw.compareTo(commandYAW) <= 0 && command.getRotationDirection() == RotationDirection.COUNTERCLOCKWISE)) {
                return yaw.sub(commandYAW).normalize();
            } else {
                if (command.getRotationDirection() == RotationDirection.CLOCKWISE) {
                    return Angle.ofDegrees(360).sub(commandYAW.sub(yaw)).normalize();
                } else {
                    return yaw.sub(commandYAW).sub(Angle.ofDegrees(360)).normalize();
                }
            }
        }
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
        Position b = command.getPosition();
        if (command.isRealtivePosition()) {
            double phi = initialState.getYAW().toRadians();
            double x = command.getPosition().getX();
            double y = command.getPosition().getY();
            b = a.addX(x * Math.cos(phi) - y * Math.sin(phi)).
                    addY(x * Math.sin(phi) + y * Math.cos(phi)).
                    addZ(command.getPosition().getZ());
        }

        return b.sub(a).
                normalize().
                multiply(b.distance(a).toMeters()).
                multiply(progress).
                add(a);

    }

}
