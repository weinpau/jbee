package com.jbee.units;

import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author weinpau
 */
public final class Angle implements Comparable<Angle> {

    public static final Angle ZERO = new Angle(0);

    private final double radians;

    private Angle(double radians) {
        this.radians = radians;
    }

    public Angle turnRound() {
        return normalize().add(ofRadians(Math.PI));
    }

    public Angle add(Angle yaw) {
        return new Angle(radians + yaw.toRadians());
    }

    public Angle sub(Angle yaw) {
        return new Angle(radians - yaw.toRadians());
    }

    public Angle multiply(double factor) {
        if (!Double.isFinite(factor)) {
            throw new IllegalArgumentException("The factor must be a finite number.");
        }
        return new Angle(radians * factor);
    }

    public Angle normalize() {
        double normalized = radians / Math.PI;
        return new Angle((normalized - (int) normalized) * Math.PI);
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

    @Override
    public int compareTo(Angle o) {
        return Double.compare(radians, o.radians);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.radians) ^ (Double.doubleToLongBits(this.radians) >>> 32));
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
        final Angle other = (Angle) obj;
        return Double.doubleToLongBits(this.radians) == Double.doubleToLongBits(other.radians);
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(3);
        return nf.format(toDegrees()) + "\u00b0";
    }

    public static Angle ofRadians(double radians) {
        if (!Double.isFinite(radians)) {
            throw new IllegalArgumentException("The angle must be a finite number.");
        }
        return new Angle(radians);
    }

    public static Angle ofDegrees(double degrees) {
        return ofRadians(Math.toRadians(degrees));

    }

}
