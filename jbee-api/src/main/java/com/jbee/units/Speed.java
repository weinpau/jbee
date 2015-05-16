package com.jbee.units;

import com.jbee.utils.Numbers;
import java.time.Duration;

/**
 *
 * @author weinpau
 */
public final class Speed implements Comparable<Speed> {

    public static Speed ZERO = new Speed(0);

    private static final double kmph2mpsFactor = 3.6d;
    private static final double kn2mpsFactor = 900d / 463d;

    private final double mps;

    private Speed() {
        this(0);
    }

    private Speed(double mps) {
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

    public Speed multiply(double factor) {
        if (!Double.isFinite(factor)) {
            throw new IllegalArgumentException("The factor must be a finite number.");
        }
        return Speed.mps(mps * factor);
    }

    public Speed multiply(Speed speed) {
        return Speed.mps(mps * speed.mps);
    }

    public Distance multiply(Duration duration) {
        double meters = mps * duration.getSeconds() + mps * duration.getNano() / 1000_000_000d;
        return Distance.ofMeters(meters);
    }

    public Speed add(Speed speed) {
        return Speed.mps(mps + speed.mps);
    }

    public Speed sub(Speed speed) {
        return Speed.mps(mps - speed.mps);
    }

    public Speed abs() {
        return Speed.mps(Math.abs(mps));
    }

    public boolean isZero() {
        return mps == 0;
    }

    public boolean isNegative() {
        return mps < 0;
    }

    @Override
    public int compareTo(Speed o) {
        if (o == null) {
            return 0;
        }
        return Double.compare(mps, o.mps);
    }

    @Override
    public String toString() {
        return Numbers.format(mps, 3) + " m/s";
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
        final Speed other = (Speed) obj;
        return Double.doubleToLongBits(this.mps) == Double.doubleToLongBits(other.mps);
    }

    public static Speed mps(double mps) {
        if (!Double.isFinite(mps)) {
            throw new IllegalArgumentException("The speed must be a finite number.");
        }
        return new Speed(mps);
    }

    public static Speed kmph(double kmph) {
        return mps(kmph / kmph2mpsFactor);
    }

    public static Speed kn(double kn) {
        return mps(kn / kn2mpsFactor);
    }

}
