package com.jbee.device.simulation;

import com.jbee.RotationDirection;
import com.jbee.AxisVelocity;
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

        AxisVelocity velocity = calculateVelocity(initialState, command, progress);

        return new State(position, velocity, yaw);
    }

    double calculateProgress(State initialState, FlyCommand command, long time) {
        long timeSpent = calculateDuration(initialState, command);

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
    public long calculateDuration(State initialState, FlyCommand command) {
        return command.calculateDuration(initialState.getPosition(), initialState.getYaw()).toMillis();
    }

    AxisVelocity calculateVelocity(State initialState, FlyCommand command, double progress) {

        if (command.getSpeed().isZero()) {
            return AxisVelocity.ZERO;
        }

        long flyDuration = command.calculateFlyDuration(initialState.getPosition()).toMillis();
        long totalDuration = command.calculateDuration(initialState.getPosition(), initialState.getYaw()).toMillis();
        
        double speedFactor = (double) flyDuration / (double) totalDuration;
        
        Position a = initialState.getPosition();
        Position b = calculatePosition(initialState, command, progress);

        Position p = b.sub(a).
                normalize().
                multiply(command.getSpeed().multiply(speedFactor).mps());

        return new AxisVelocity(
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
