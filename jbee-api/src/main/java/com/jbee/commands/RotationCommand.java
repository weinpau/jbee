package com.jbee.commands;

import com.jbee.units.YAW;

/**
 *
 * @author weinpau
 */
public class RotationCommand extends AbstractCommand {

    private final YAW yaw;

    public RotationCommand(YAW yaw) {
        this.yaw = yaw;
    }

    public YAW getYAW() {
        return yaw;
    }

}
