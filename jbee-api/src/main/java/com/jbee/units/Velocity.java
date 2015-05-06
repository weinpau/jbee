package com.jbee.units;

/**
 *
 * @author weinpau
 */
public final class Velocity implements Comparable<Velocity> {

    public static Velocity ZERO = new Velocity(0);

    private static final double kmph2mpsFactor = 3.6d;
    private static final double kn2mpsFactor = 900d / 463d;

    private final double mps;

    private Velocity() {
        this(0);
    }

    private Velocity(double mps) {
        this.mps = mps;
    }

    public double mps() {
        return mps;
    }

    public double kmph() {
        return mps * kmph2mpsFactor;
    }

    public double kn() {
        return mps * kn2mpsFactor;
    }

    public Velocity multiply(double factor) {
        if (!Double.isFinite(factor)) {
            throw new IllegalArgumentException("The factor must be a finite number.");
        }
        return Velocity.mps(mps * factor);
    }

    public Velocity multiply(Velocity velocity) {
        return Velocity.mps(mps * velocity.mps);
    }

    public Velocity add(Velocity velocity) {
        return Velocity.mps(mps + velocity.mps);
    }

    public Velocity sub(Velocity velocity) {
        return Velocity.mps(mps - velocity.mps);
    }

    public Velocity abs() {
        return Velocity.mps(Math.abs(mps));
    }

    public static Velocity mps(double mps) {
        if (!Double.isFinite(mps)) {
            throw new IllegalArgumentException("The velocity must be a finite number.");
        }
        return new Velocity(mps);
    }

    public static Velocity kmph(double kmph) {
        return mps(kmph / kmph2mpsFactor);
    }

    public static Velocity kn(double kn) {
        return mps(kn / kn2mpsFactor);
    }

    @Override
    public int compareTo(Velocity o) {
        if (o == null) {
            return 0;
        }
        return Double.compare(mps, o.mps);
    }

    @Override
    public String toString() {
        return mps + " m/s";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.mps) ^ (Double.doubleToLongBits(this.mps) >>> 32));
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
        final Velocity other = (Velocity) obj;
        return Double.doubleToLongBits(this.mps) == Double.doubleToLongBits(other.mps);
    }

}
