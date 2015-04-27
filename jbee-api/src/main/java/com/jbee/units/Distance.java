package com.jbee.units;

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
        return mm + " mm";
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
