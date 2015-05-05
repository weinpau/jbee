package com.jbee.units;

import java.time.Duration;

/**
 *
 * @author weinpau
 */
public class Frequency implements Comparable<Frequency> {

    public static final Frequency ZERO = new Frequency(0);

    
    private static final long second2NanoFactor = 1000_000_000_000L;
    
    private final long mHz;

    private Frequency(long mHz) {
        this.mHz = mHz;
    }

    public Frequency add(Frequency frequency) {
        return new Frequency(StrictMath.addExact(mHz, frequency.mHz));
    }

    public Frequency sub(Frequency frequency) {
        return new Frequency(StrictMath.subtractExact(mHz, frequency.mHz));
    }

    public Frequency multiply(double factor) {
        return new Frequency(StrictMath.toIntExact(StrictMath.round(factor * (double) mHz)));
    }

    public int toHz() {
        return (int) mHz / 1000;
    }
    
    public long toMilliHz() {
        return mHz;
    }

    public Duration toCycleDuration() {
        if (mHz > second2NanoFactor || mHz < -second2NanoFactor) {
            return Duration.ZERO;
        }
        long millis = (1_000_000 / mHz);
        long nanos = ((second2NanoFactor / mHz) - millis * 1_000_000L) % 1000_000_000L;
        
        return Duration.ofMillis(millis).plusNanos(nanos);
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

   

    public static Frequency ofHz(int Hz) {
        return new Frequency(StrictMath.multiplyExact(Hz, 1000L));
    }
    
      public static Frequency ofMilliHz(long mHz) {
        return new Frequency(mHz);
    }

}
