package com.jbee.commands;

import com.jbee.BeeControl;
import com.jbee.RotationDirection;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.time.Duration;

/**
 *
 * @author weinpau
 */
public class FlyCommand extends AbstractCommand {

    public enum Mode {

        FLY,
        ROTATE,
        FLY_AND_ROTATE,
        ROTATE_AND_FLY
    }

    private final Position position;

    private final Angle angle;
    private final RotationDirection rotationDirection;

    private final boolean realtivePosition;
    private final boolean realtiveRotation;
    private final Mode mode;

    private Speed speed;
    private RotationalSpeed rotationalSpeed;

    FlyCommand(Position position, Angle angle,
            RotationDirection rotationDirection, RotationalSpeed rotationalSpeed,
            boolean realtivePosition, boolean realtiveRotation,
            Speed speed, Mode mode) {
        this.position = position;
        this.angle = angle;
        this.rotationDirection = rotationDirection;
        this.rotationalSpeed = rotationalSpeed;
        this.realtivePosition = realtivePosition;
        this.realtiveRotation = realtiveRotation;
        this.speed = speed;
        this.mode = mode;
    }

    @Override
    public void init(int commandNumber, BeeControl executiveControl) {
        super.init(commandNumber, executiveControl);

        if (rotationalSpeed == null) {
            rotationalSpeed = executiveControl.getRotationalSpeed();
        }
        if (speed == null) {
            speed = executiveControl.getSpeed();
        }
    }

    public Position getPosition() {
        return position;
    }

    public Angle getAngle() {
        return angle;
    }

    public RotationDirection getRotationDirection() {
        return rotationDirection;
    }

    public RotationalSpeed getRotationalSpeed() {
        return rotationalSpeed;
    }

    public boolean isRealtivePosition() {
        return realtivePosition;
    }

    public boolean isRealtiveRotation() {
        return realtiveRotation;
    }

    public Speed getSpeed() {
        return speed;
    }

    public Mode getMode() {
        return mode;
    }

    public Angle calculateDeltaYAW(Angle initialYAW) {
        Angle yaw = getAngle();
        if (isRealtiveRotation()) {
            if (getRotationDirection() == RotationDirection.CW) {
                return yaw;
            } else {
                return yaw.multiply(-1);
            }
        } else {
            yaw = yaw.normalize();
            if ((initialYAW.compareTo(yaw) >= 0 && rotationDirection == RotationDirection.CW)
                    || (initialYAW.compareTo(yaw) <= 0 && rotationDirection == RotationDirection.CCW)) {
                return initialYAW.sub(yaw).normalize();
            } else {
                if (rotationDirection == RotationDirection.CW) {
                    return Angle.ofDegrees(360).sub(yaw.sub(initialYAW)).normalize();
                } else {
                    return initialYAW.sub(yaw).sub(Angle.ofDegrees(360)).normalize();
                }
            }
        }
    }

    public Position calculateTargetPosition(Position initialPosition, Angle initialYAW) {

        if (isRealtivePosition()) {
            double phi = initialYAW.toRadians();
            double x = getPosition().getX();
            double y = getPosition().getY();
            return initialPosition.addX(x * Math.cos(phi) - y * Math.sin(phi)).
                    addY(x * Math.sin(phi) + y * Math.cos(phi)).
                    addZ(getPosition().getZ());
        }
        return getPosition();

    }

    public Duration calculateDuration(Position initialPosition, Angle initialYAW) {
        Duration flyDuration = calculateFlyDuration(initialPosition);
        Duration rotationDuration = calculateRotationDuration(initialYAW);
        if (flyDuration.compareTo(rotationDuration) >= 0) {
            return flyDuration;
        } else {
            return rotationDuration;
        }
    }

    public Duration calculateFlyDuration(Position initialPosition) {
        Position p = Position.ORIGIN;
        if (!isRealtivePosition()) {
            p = initialPosition;
        }
        return getPosition().distance(p).divide(speed).abs();
    }

    public Duration calculateRotationDuration(Angle initialYAW) {
        Angle deltaYAW = calculateDeltaYAW(initialYAW);
        return deltaYAW.divide(rotationalSpeed.toAngularSpeed()).abs();
    }

}
