package com.jbee;

import com.jbee.units.Angle;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class AxisAngles {

    public static final AxisAngles ZERO = new AxisAngles(Angle.ZERO, Angle.ZERO, Angle.ZERO);

    private final Angle yaw, roll, pitch;

    public AxisAngles(Angle yaw, Angle roll, Angle pitch) {
        this.yaw = yaw;
        this.roll = roll;
        this.pitch = pitch;
    }

    public Angle getPitch() {
        return pitch;
    }

    public Angle getYaw() {
        return yaw;
    }

    public Angle getRoll() {
        return roll;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.yaw);
        hash = 97 * hash + Objects.hashCode(this.roll);
        hash = 97 * hash + Objects.hashCode(this.pitch);
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
        final AxisAngles other = (AxisAngles) obj;
        if (!Objects.equals(this.yaw, other.yaw)) {
            return false;
        }
        if (!Objects.equals(this.roll, other.roll)) {
            return false;
        }
        return Objects.equals(this.pitch, other.pitch);
    }

    @Override
    public String toString() {
        return "PrincipalAxes{" + "yaw=" + yaw + ", roll=" + roll + ", pitch=" + pitch + '}';
    }

}
