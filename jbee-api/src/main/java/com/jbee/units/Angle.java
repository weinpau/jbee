package com.jbee.units;

/**
 *
 * @author weinpau
 */
public final class Angle {

    public static final Angle ZERO = new Angle(0);

    private final double radians;

    private Angle() {
        this.radians = 0;
    }

    private Angle(double radians) {
        this.radians = radians % (2 * Math.PI);
    }

    public Angle turnRound() {
        return ofRadians(radians + Math.PI);
    }

    public Angle add(Angle yaw) {
        return new Angle(radians + yaw.toRadians());
    }

    public Angle sub(Angle yaw) {
        return new Angle(radians - yaw.toRadians());
    }

    public Angle multiply(double factor) {
        return new Angle(radians * factor);
    }

    public Angle abs() {
        return new Angle(Math.abs(radians));
    }

    public double toRadians() {
        return radians;
    }

    public double toDegrees() {
        return Math.toDegrees(radians);
    }

    public static Angle ofRadians(double radians) {
        return new Angle(radians);
    }

    public static Angle ofDegrees(double degrees) {
        return ofRadians(Math.toRadians(degrees));

    }

}
