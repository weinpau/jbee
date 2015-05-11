package com.jbee.units;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;

/**
 *
 * @author weinpau
 */
public final class Distance implements Comparable<Distance> {

    public static final Distance ZERO = new Distance(0);

    private final double meters;

    private Distance() {
        this(0);
    }

    private Distance(double meters) {
        this.meters = meters;

    }

    public double toMillimeters() {
        return meters * 1000d;
    }

    public double toCentimeters() {
        return meters * 100d;
    }

    public double toMeters() {
        return meters;
    }

    public Distance add(Distance distance) {
        return new Distance(meters + distance.meters);
    }

    public Distance sub(Distance distance) {
        return add(distance.multiply(-1));
    }

    public Distance multiply(double factor) {
        if (!Double.isFinite(factor)) {
            throw new IllegalArgumentException("The factor must be a finite number.");
        }
        return ofMeters(meters * factor);
    }

    public Duration divide(Speed speed) {
        double seconds = meters / speed.mps();
        long ms = (long) seconds * 1000;
        long ns = (long) ((seconds - Math.floor(seconds)) * 1000_000_000L);
        return Duration.ofMillis(ms).plusNanos(ns);
    }

    public boolean lessThan(Distance distance) {
        return compareTo(distance) == -1;
    }

    public boolean greaterThan(Distance distance) {
        return compareTo(distance) == 1;
    }

    public boolean isZero() {
        return meters == 0;
    }

    public static Distance ofMeters(double meters) {
        return new Distance(meters);
    }

    public static Distance ofCentimeters(double centimeters) {
        return new Distance(centimeters / 100d);
    }

    public static Distance ofMillimeters(double millimeters) {
        return new Distance(millimeters / 1000d);
    }

    @Override
    public int compareTo(Distance o) {
        if (o == null) {
            return 0;
        }
        return Double.compare(meters, o.meters);
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(3);
        return nf.format(toMeters()) + " m";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.meters) ^ (Double.doubleToLongBits(this.meters) >>> 32));
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
        final Distance other = (Distance) obj;
        return Double.doubleToLongBits(this.meters) == Double.doubleToLongBits(other.meters);
    }

}
