package com.jbee.commands;

import com.jbee.units.Angle;

/**
 *
 * @author weinpau
 */
public class RotationCommand extends AbstractCommand {

    private final Angle yaw;

    public RotationCommand(Angle yaw) {
        this.yaw = yaw;
    }

    public Angle getYAW() {
        return yaw;
    }

}
