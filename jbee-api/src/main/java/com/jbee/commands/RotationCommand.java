package com.jbee.commands;

import com.jbee.RotationDirection;
import com.jbee.units.Angle;

/**
 *
 * @author weinpau
 */
public class RotationCommand extends AbstractCommand {

    private final Angle angle;
    private final RotationDirection rotationDirection;
    private final boolean absolute;

    public RotationCommand(Angle angle, RotationDirection rotationDirection, boolean absolute) {
        this.angle = angle;
        this.rotationDirection = rotationDirection;
        this.absolute = absolute;
    }

    public Angle getAngle() {
        return angle;
    }

    public RotationDirection getRotationDirection() {
        return rotationDirection;
    }

    public boolean isAbsolute() {
        return absolute;
    }
    
    

}
