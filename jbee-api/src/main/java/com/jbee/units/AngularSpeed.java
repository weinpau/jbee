package com.jbee.units;

import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class AngularSpeed implements Comparable<AngularSpeed> {

    public static AngularSpeed ZERO = new AngularSpeed(Angle.ZERO);

    private final Angle degreesPerSecond;

    private AngularSpeed(Angle degreesPerSecond) {
        this.degreesPerSecond = degreesPerSecond;
    }

    public Angle toDegreesPerSecond() {
        return degreesPerSecond;
    }

    public RotationalSpeed toRotationalSpeed() {
        return RotationalSpeed.ofAngularSpeed(this);
    }

    public AngularSpeed abs() {
        return new AngularSpeed(degreesPerSecond.abs());
    }

    public AngularSpeed add(AngularSpeed angularSpeed) {
        return new AngularSpeed(degreesPerSecond.add(angularSpeed.degreesPerSecond));
    }

    public AngularSpeed sub(AngularSpeed angularSpeed) {
        return add(multiply(-1));
    }

    public AngularSpeed multiply(double factor) {
        return new AngularSpeed(degreesPerSecond.multiply(factor));
    }

    public boolean isZero() {
        return degreesPerSecond.isZero();
    }

    public boolean isNegative() {
        return degreesPerSecond.isNegative();
    }

    public boolean equal(AngularSpeed angularSpeed) {
        return degreesPerSecond.equal(angularSpeed.degreesPerSecond);
    }

    @Override
    public int compareTo(AngularSpeed o) {
        return degreesPerSecond.compareTo(o.degreesPerSecond);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.degreesPerSecond);
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
        final AngularSpeed other = (AngularSpeed) obj;
        return Objects.equals(this.degreesPerSecond, other.degreesPerSecond);
    }

    @Override
    public String toString() {
        return degreesPerSecond + "/s";
    }

    public static AngularSpeed ofAnglePerSecond(Angle anglePerSecond) {
        return new AngularSpeed(anglePerSecond);
    }

    public static AngularSpeed ofRadiansPerSecond(double radians) {
        return ofAnglePerSecond(Angle.ofRadians(radians));
    }

    public static AngularSpeed ofDegreesPerSecond(double degrees) {
        return ofAnglePerSecond(Angle.ofDegrees(degrees));
    }

    public static AngularSpeed ofRotationalSpeed(RotationalSpeed rotationalSpeed) {
        return new AngularSpeed(Angle.ofDegrees(rotationalSpeed.rps() * 360));
    }

}
