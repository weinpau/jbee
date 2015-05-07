package com.jbee.commands;

import com.jbee.RotationDirection;
import com.jbee.units.Angle;
import com.jbee.units.RotationalSpeed;

/**
 *
 * @author weinpau
 */
public class RotationCommand extends AbstractCommand {

    private final Angle angle;
    private final RotationDirection rotationDirection;
    private final RotationalSpeed rotationalSpeed;
    private final boolean absolute;

    RotationCommand(Angle angle, RotationDirection rotationDirection, RotationalSpeed speed, boolean absolute) {
        this.angle = angle;
        this.rotationDirection = rotationDirection;
        this.absolute = absolute;
        this.rotationalSpeed = speed;
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

    public boolean isAbsolute() {
        return absolute;
    }

}
