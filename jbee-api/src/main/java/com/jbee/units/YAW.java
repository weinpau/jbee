package com.jbee.units;

/**
 *
 * @author weinpau
 */
public final class YAW {

    public static final YAW ZERO = new YAW(0);

    private final double radians;

    private YAW() {
        this.radians = 0;
    }

    private YAW(double radians) {
        this.radians = radians;
    }

    public YAW turnRound() {
        return ofRadians(radians + Math.PI);
    }

    public double toRadians() {
        return radians;
    }

    public double toDegrees() {
        return Math.toDegrees(radians);
    }

    public static YAW ofRadians(double radians) {   
        return new YAW(radians % (2 * Math.PI));
    }

    public static YAW ofDegrees(double degrees) {
        return ofRadians(Math.toRadians(degrees));

    }

}
