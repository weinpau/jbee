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
    FlyCommand.Mode mode = FlyCommand.Mode.FLY;

    FlyCommandBuilder() {
    }

    public RotationalSpeedStep rotate(Angle angle, RotationDirection rotationDirection) {
        this.angle = angle;
        this.rotationDirection = rotationDirection;
        this.realtiveRotation = true;
        this.mode = FlyCommand.Mode.ROTATE;
        return new RotationalSpeedStep(this);
    }

    public RotationalSpeedStep rotateTo(Angle angle, RotationDirection rotationDirection) {
        this.angle = angle.normalize();
        this.rotationDirection = rotationDirection;
        this.realtiveRotation = false;
        this.mode = FlyCommand.Mode.ROTATE;
        return new RotationalSpeedStep(this);
    }

    public SpeedStep flyTo(Position position) {
        this.position = position;
        this.realtivePosition = false;
        return new SpeedStep(this);
    }

    public SpeedStep fly(Distance right, Distance forward, Distance up) {

        this.position = new Position(right.toMeters(), forward.toMeters(), up.toMeters());
        this.realtivePosition = true;
        return new SpeedStep(this);

    }

    public SpeedStep fly(Direction direction, Distance distance) {
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
        return new SpeedStep(this);
    }

    public SpeedStep forward(Distance distance) {
        return fly(Distance.ZERO, distance, Distance.ZERO);
    }

    public SpeedStep right(Distance distance) {
        return fly(distance, Distance.ZERO, Distance.ZERO);
    }

    public SpeedStep left(Distance distance) {
        return right(distance.multiply(-1));
    }

    public SpeedStep backward(Distance distance) {
        return forward(distance.multiply(-1));
    }

    public SpeedStep up(Distance distance) {
        return fly(Distance.ZERO, Distance.ZERO, distance);
    }

    public SpeedStep down(Distance distance) {
        return up(distance.multiply(-1));
    }

    public static class SpeedStep extends FlyRotationStep {

        SpeedStep(FlyCommandBuilder commandBuilder) {
            super(commandBuilder);
        }

        public FlyRotationStep with(Speed speed) {
            commandBuilder.speed = speed;
            return this;
        }

    }

    public static class FlyRotationStep extends BuildStep {

        FlyRotationStep(FlyCommandBuilder commandBuilder) {
            super(commandBuilder);
        }

        public RotationalSpeedFinalStep andRotate(Angle angle, RotationDirection rotationDirection) {
            commandBuilder.angle = angle;
            commandBuilder.rotationDirection = rotationDirection;
            commandBuilder.realtiveRotation = true;
            commandBuilder.mode = FlyCommand.Mode.FLY_AND_ROTATE;
            return new RotationalSpeedFinalStep(commandBuilder);
        }

        public RotationalSpeedFinalStep andRotateTo(Angle angle, RotationDirection rotationDirection) {
            commandBuilder.angle = angle.normalize();
            commandBuilder.rotationDirection = rotationDirection;
            commandBuilder.realtiveRotation = false;
            commandBuilder.mode = FlyCommand.Mode.FLY_AND_ROTATE;
            return new RotationalSpeedFinalStep(commandBuilder);
        }
    }

    public static class RotationalSpeedStep extends RotationFlyStep {

        RotationalSpeedStep(FlyCommandBuilder commandBuilder) {
            super(commandBuilder);
        }

        public RotationFlyStep with(RotationalSpeed rotationalSpeed) {
            commandBuilder.rotationalSpeed = rotationalSpeed;
            return this;
        }

    }

    public static class RotationFlyStep extends BuildStep {

        RotationFlyStep(FlyCommandBuilder commandBuilder) {
            super(commandBuilder);
        }

        public SpeedFinalStep andFlyTo(Position position) {
            commandBuilder.mode = FlyCommand.Mode.ROTATE_AND_FLY;
            commandBuilder.position = position;
            commandBuilder.realtivePosition = false;
            return new SpeedFinalStep(commandBuilder);

        }

        public SpeedFinalStep andFly(Distance right, Distance forward, Distance up) {
            commandBuilder.mode = FlyCommand.Mode.ROTATE_AND_FLY;
            commandBuilder.position = new Position(right.toMeters(), forward.toMeters(), up.toMeters());
            commandBuilder.realtivePosition = true;
            return new SpeedFinalStep(commandBuilder);

        }

        public SpeedFinalStep andFly(Direction direction, Distance distance) {
            switch (direction) {
                case BACKWARD:
                    return andBackward(distance);
                case DOWN:
                    return andDown(distance);
                case FORWARD:
                    return andForward(distance);
                case LEFT:
                    return andLeft(distance);
                case RIGHT:
                    return andRight(distance);
                case UP:
                    return andUp(distance);
            }
            return new SpeedFinalStep(commandBuilder);
        }

        public SpeedFinalStep andForward(Distance distance) {
            return andFly(Distance.ZERO, distance, Distance.ZERO);
        }

        public SpeedFinalStep andRight(Distance distance) {
            return andFly(distance, Distance.ZERO, Distance.ZERO);
        }

        public SpeedFinalStep andLeft(Distance distance) {
            return andRight(distance.multiply(-1));
        }

        public SpeedFinalStep andBackward(Distance distance) {
            return andForward(distance.multiply(-1));
        }

        public SpeedFinalStep andUp(Distance distance) {
            return andFly(Distance.ZERO, Distance.ZERO, distance);
        }

        public SpeedFinalStep andDown(Distance distance) {
            return andUp(distance.multiply(-1));
        }

    }

    public static class SpeedFinalStep extends BuildStep {

        SpeedFinalStep(FlyCommandBuilder commandBuilder) {
            super(commandBuilder);
        }

        public BuildStep with(Speed speed) {
            commandBuilder.speed = speed;
            return this;
        }

    }

    public static class RotationalSpeedFinalStep extends BuildStep {

        RotationalSpeedFinalStep(FlyCommandBuilder commandBuilder) {
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
                    commandBuilder.speed,
                    commandBuilder.mode);
        }

    }

}
