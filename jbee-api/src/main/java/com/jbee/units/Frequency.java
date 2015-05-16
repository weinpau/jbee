package com.jbee.units;

import com.jbee.utils.Numbers;
import java.time.Duration;

/**
 *
 * @author weinpau
 */
public class Frequency implements Comparable<Frequency> {

    public static final Frequency ZERO = new Frequency(0);

    static final long NANOS_PER_SECOND = 1000_000_000L;

    private final long mHz;

    private Frequency(long mHz) {
        this.mHz = mHz;
    }

    public Frequency abs() {
        return new Frequency(Math.abs(mHz));
    }

    public Frequency add(Frequency frequency) {
        return new Frequency(StrictMath.addExact(mHz, frequency.mHz));
    }

    public Frequency sub(Frequency frequency) {
        return new Frequency(StrictMath.subtractExact(mHz, frequency.mHz));
    }

    public Frequency multiply(double factor) {
        if (!Double.isFinite(factor)) {
            throw new IllegalArgumentException("The factor must be a finite number.");
        }
        return new Frequency(StrictMath.toIntExact(StrictMath.round(factor * (double) mHz)));
    }

    public int toHz() {
        return (int) mHz / 1000;
    }

    public long toMilliHz() {
        return mHz;
    }

    public Duration toCycleDuration() {
        if (mHz > 1000 * NANOS_PER_SECOND || mHz < -1000 * NANOS_PER_SECOND) {
            return Duration.ZERO;
        }
        long millis = (1_000_000 / mHz);
        long nanos = ((1000 * NANOS_PER_SECOND / mHz) - millis * 1_000_000L) % NANOS_PER_SECOND;

        return Duration.ofMillis(millis).plusNanos(nanos);
    }

    public boolean isZero() {
        return mHz == 0;
    }

    public boolean isNegative() {
        return mHz < 0;
    }

    @Override
    public int compareTo(Frequency o) {
        return Long.compare(mHz, o.mHz);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.mHz ^ (this.mHz >>> 32));
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
        final Frequency other = (Frequency) obj;
        return this.mHz == other.mHz;
    }

    @Override
    public String toString() {
        return Numbers.format(toHz(), 3) + " Hz";
    }

    public static Frequency ofHz(int Hz) {
        return new Frequency(StrictMath.multiplyExact(Hz, 1000L));
    }

    public static Frequency ofMilliHz(long mHz) {
        return new Frequency(mHz);
    }

}
