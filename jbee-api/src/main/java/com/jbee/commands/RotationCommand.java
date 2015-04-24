package com.jbee.commands;

import com.jbee.units.YAW;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class RotationCommand implements Command {

    private final YAW yaw;

    public RotationCommand(YAW yaw) {
        this.yaw = yaw;
    }

    public YAW getYAW() {
        return yaw;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.yaw);
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
        final RotationCommand other = (RotationCommand) obj;
        if (!Objects.equals(this.yaw, other.yaw)) {
            return false;
        }
        return true;
    }

}
