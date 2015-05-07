package com.jbee.units;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author weinpau
 */
public class RotationalSpeed implements Comparable<RotationalSpeed> {

    public static final RotationalSpeed ZERO = new RotationalSpeed(0);

    private final double rpm;

    private RotationalSpeed(double rpm) {
        this.rpm = rpm;
    }

    public double rps() {
        return rpm * 60;
    }

    public double rpm() {
        return rpm;
    }

    public RotationalSpeed abs() {
        return new RotationalSpeed(Math.abs(rpm));
    }

    public AngularSpeed toAngularSpeed() {
        return AngularSpeed.ofRotationalSpeed(this);
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(3);
        return nf.format(rpm) + " rpm";
    }

    @Override
    public int compareTo(RotationalSpeed o) {
        return Double.compare(rpm, o.rpm);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.rpm) ^ (Double.doubleToLongBits(this.rpm) >>> 32));
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
        final RotationalSpeed other = (RotationalSpeed) obj;
        return Double.doubleToLongBits(this.rpm) == Double.doubleToLongBits(other.rpm);
    }

    public static RotationalSpeed rps(double rps) {
        return new RotationalSpeed(rps / 60);
    }

    public static RotationalSpeed rpm(double rpm) {
        return new RotationalSpeed(rpm);
    }

    public static RotationalSpeed ofAngularSpeed(AngularSpeed angularSpeed) {
        return rps(angularSpeed.toDegreesPerSecond().toDegrees() / 360);
    }

}
