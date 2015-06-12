package com.jbee.commands;

import com.jbee.Direction;
import com.jbee.RotationDirection;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;

/**
 *
 * @author weinpau
 */
public final class FlyCommandBuilder {

    Position position = Position.ORIGIN;

    Angle angle = Angle.ZERO;
    RotationDirection rotationDirection = RotationDirection.CW;

    boolean realtivePosition = true;
    boolean realtiveRotation = true;

    Speed speed;
    RotationalSpeed rotationalSpeed;

    FlyCommandBuilder() {
    }

    public RotationalSpeedStep rotate(Angle angle, RotationDirection rotationDirection) {
        this.angle = angle;
        this.rotationDirection = rotationDirection;
        this.realtiveRotation = true;
        return new RotationalSpeedStep(this);
    }

    public RotationalSpeedStep rotateTo(Angle angle, RotationDirection rotationDirection) {
        this.angle = angle.normalize();
        this.rotationDirection = rotationDirection;
        this.realtiveRotation = false;
        return new RotationalSpeedStep(this);
    }

    public SpeedStep flyTo(Position position) {
        this.position = position;
        this.realtivePosition = false;
        return new SpeedStep(this);
    }

    public RelativePositionStep fly(Direction direction, Distance distance) {
        return new RelativePositionStep(this).fly(direction, distance);
    }

    public RelativePositionStep forward(Distance distance) {
        return new RelativePositionStep(this).forward(distance);
    }

    public RelativePositionStep right(Distance distance) {
        return new RelativePositionStep(this).right(distance);
    }

    public RelativePositionStep left(Distance distance) {
        return new RelativePositionStep(this).left(distance);
    }

    public RelativePositionStep backward(Distance distance) {
        return new RelativePositionStep(this).backward(distance);
    }

    public RelativePositionStep up(Distance distance) {
        return new RelativePositionStep(this).up(distance);
    }

    public RelativePositionStep down(Distance distance) {
        return new RelativePositionStep(this).down(distance);
    }

    public static class RelativePositionStep extends RotationStep {

        RelativePositionStep(FlyCommandBuilder commandBuilder) {
            super(commandBuilder);
            commandBuilder.realtivePosition = true;
        }

        public RotationStep with(Speed speed) {
            commandBuilder.speed = speed;
            return new RotationStep(commandBuilder);

        }

        public RelativePositionStep forward(Distance distance) {

            commandBuilder.position = commandBuilder.position.addY(distance.toMeters());
            return this;
        }

        public RelativePositionStep right(Distance distance) {
            commandBuilder.position = commandBuilder.position.addX(distance.toMeters());
            return this;
        }

        public RelativePositionStep left(Distance distance) {
            return right(distance.multiply(-1));
        }

        public RelativePositionStep backward(Distance distance) {
            return forward(distance.multiply(-1));
        }

        public RelativePositionStep up(Distance distance) {
            commandBuilder.position = commandBuilder.position.addZ(distance.toMeters());
            return this;
        }

        public RelativePositionStep down(Distance distance) {
            return up(distance.multiply(-1));
        }

        public RelativePositionStep fly(Direction direction, Distance distance) {
            switch (direction) {
                case BACKWARD:
                    return backward(distance);
                case DOWN:
                    return down(distance);
                case FORWARD:
                    return forward(distance);
                case LEFT:
                    return left(distance);
                case RIGHT:
                    return right(distance);
                case UP:
                    return up(distance);
            }
            return this;
        }

    }

    public static class SpeedStep extends RotationStep {

        SpeedStep(FlyCommandBuilder commandBuilder) {
            super(commandBuilder);
        }

        public RotationStep with(Speed speed) {
            commandBuilder.speed = speed;
            return this;
        }

    }

    public static class RotationStep extends BuildStep {

        RotationStep(FlyCommandBuilder commandBuilder) {
            super(commandBuilder);
        }

        public RotationalSpeedStep andRotate(Angle angle, RotationDirection rotationDirection) {
            commandBuilder.angle = angle;
            commandBuilder.rotationDirection = rotationDirection;
            commandBuilder.realtiveRotation = true;
            return new RotationalSpeedStep(commandBuilder);
        }

        public RotationalSpeedStep andRotateTo(Angle angle, RotationDirection rotationDirection) {
            commandBuilder.angle = angle.normalize();
            commandBuilder.rotationDirection = rotationDirection;
            commandBuilder.realtiveRotation = false;
            return new RotationalSpeedStep(commandBuilder);
        }
    }

    public static class RotationalSpeedStep extends BuildStep {

        RotationalSpeedStep(FlyCommandBuilder commandBuilder) {
            super(commandBuilder);
        }

        public BuildStep with(RotationalSpeed rotationalSpeed) {
            commandBuilder.rotationalSpeed = rotationalSpeed;
            return this;
        }

    }

    public static class BuildStep {

        FlyCommandBuilder commandBuilder;

        BuildStep(FlyCommandBuilder commandBuilder) {
            this.commandBuilder = commandBuilder;
        }

        public FlyCommand build() {
            return new FlyCommand(
                    commandBuilder.position,
                    commandBuilder.angle,
                    commandBuilder.rotationDirection,
                    commandBuilder.rotationalSpeed,
                    commandBuilder.realtivePosition,
                    commandBuilder.realtiveRotation,
                    commandBuilder.speed);
        }

    }

}
