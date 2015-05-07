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

    private final int mm;

    private Distance() {
        this(0);
    }

    private Distance(int mm) {
        this.mm = mm;
    }

    public int toMilimeters() {
        return mm;
    }

    public double toCentimeters() {
        return mm / 10d;
    }

    public double toMeters() {
        return mm / 1000d;
    }

    public Distance add(Distance distance) {
        return new Distance(StrictMath.addExact(mm, distance.mm));
    }

    public Duration divide(Speed speed) {
        double seconds = toMeters() / speed.mps();
        long millis = (long) seconds * 1000;
        long nanos = (long) ((seconds - Math.floor(seconds)) * 1000_000_000L);
        return Duration.ofMillis(millis).plusNanos(nanos);

    }

    public boolean lessThan(Distance distance) {
        return mm < distance.mm;
    }

    public boolean greaterThan(Distance distance) {
        return mm > distance.mm;
    }

    public boolean isZero() {
        return mm == 0;
    }

    public static Distance ofMilimeters(int milimeters) {
        return new Distance(milimeters);
    }

    public static Distance ofCentimeters(int centimeters) {
        return new Distance(StrictMath.multiplyExact(centimeters, 10));
    }

    public static Distance ofMeters(int meters) {
        return new Distance(StrictMath.multiplyExact(meters, 1000));
    }

    @Override
    public int compareTo(Distance o) {
        if (o == null) {
            return 0;
        }
        return Integer.compare(mm, o.mm);
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(3);
        return nf.format(toMeters()) + " m";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.mm;
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
        return this.mm == other.mm;
    }

}
