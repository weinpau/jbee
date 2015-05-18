package com.jbee.commands;

import com.jbee.BeeControl;
import com.jbee.BeeState;
import com.jbee.RotationDirection;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class FlyCommand extends AbstractCommand {

    private final Position position;

    private final Angle angle;
    private final RotationDirection rotationDirection;

    private final boolean realtivePosition;
    private final boolean realtiveRotation;

    private Speed speed;
    private RotationalSpeed rotationalSpeed;

    FlyCommand(Position position, Angle angle, RotationDirection rotationDirection, RotationalSpeed rotationalSpeed, boolean realtivePosition, boolean realtiveRotation, Speed speed) {
        this.position = position;
        this.angle = angle;
        this.rotationDirection = rotationDirection;
        this.rotationalSpeed = rotationalSpeed;
        this.realtivePosition = realtivePosition;
        this.realtiveRotation = realtiveRotation;
        this.speed = speed;
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

    public Angle calculateDeltaYAW(Angle initialYAW) {
        Angle yaw = getAngle();
        if (isRealtiveRotation()) {
            if (getRotationDirection() == RotationDirection.CLOCKWISE) {
                return yaw;
            } else {
                return yaw.multiply(-1);
            }
        } else {
            yaw = yaw.normalize();
            if ((initialYAW.compareTo(yaw) >= 0 && rotationDirection == RotationDirection.CLOCKWISE)
                    || (initialYAW.compareTo(yaw) <= 0 && rotationDirection == RotationDirection.COUNTERCLOCKWISE)) {
                return initialYAW.sub(yaw).normalize();
            } else {
                if (rotationDirection == RotationDirection.CLOCKWISE) {
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.position);
        hash = 37 * hash + Objects.hashCode(this.angle);
        hash = 37 * hash + Objects.hashCode(this.rotationDirection);
        hash = 37 * hash + Objects.hashCode(this.rotationalSpeed);
        hash = 37 * hash + (this.realtivePosition ? 1 : 0);
        hash = 37 * hash + (this.realtiveRotation ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.speed);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FlyCommand other = (FlyCommand) obj;
        if (!Objects.equals(this.position, other.position)) {
            return false;
        }
        if (!Objects.equals(this.angle, other.angle)) {
            return false;
        }
        if (this.rotationDirection != other.rotationDirection) {
            return false;
        }
        if (!Objects.equals(this.rotationalSpeed, other.rotationalSpeed)) {
            return false;
        }
        if (this.realtivePosition != other.realtivePosition) {
            return false;
        }
        if (this.realtiveRotation != other.realtiveRotation) {
            return false;
        }
        return Objects.equals(this.speed, other.speed);
    }

}
