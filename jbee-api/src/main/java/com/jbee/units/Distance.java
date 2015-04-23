package com.jbee.units;

/**
 *
 * @author weinpau
 */
public final class Distance {

    private final long mm;

    private Distance() {
        this(0);
    }

    private Distance(long mm) {
        this.mm = mm;
    }

    public long toMilimeters() {
        return mm;
    }

    public double toCentimeters() {
        return mm / 10d;
    }

    public double toMeters() {
        return mm / 1000d;
    }

    public static Distance ofMilimeters(long milimeters) {
        return new Distance(milimeters);
    }

    public static Distance ofCentimeters(double centimeter) {
        return new Distance((long) (centimeter * 10));
    }

    public static Distance ofMeters(double meters) {
        return new Distance((long) (meters * 1000));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.mm ^ (this.mm >>> 32));
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
