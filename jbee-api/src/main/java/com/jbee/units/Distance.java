package com.jbee.units;

/**
 *
 * @author weinpau
 */
public final class Distance {

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
        return mm / 10f;
    }

    public double toMeters() {
        return mm / 1000f;
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
