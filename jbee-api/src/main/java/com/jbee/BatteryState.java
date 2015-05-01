package com.jbee;

/**
 *
 * @author weinpau
 */
public final class BatteryState implements Comparable<BatteryState> {
    
    private final boolean tooLow;
    private final double percent;

    public BatteryState(double percent, boolean tooLow) {
        if (percent < 0 || percent > 1) {
            throw new IllegalArgumentException("Battery state of charge must be between 0 and 1.");
        }

        this.tooLow = tooLow;
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    public boolean isTooLow() {
        return tooLow;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.tooLow ? 1 : 0);
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.percent) ^ (Double.doubleToLongBits(this.percent) >>> 32));
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
        final BatteryState other = (BatteryState) obj;
        if (this.tooLow != other.tooLow) {
            return false;
        }
        return Double.doubleToLongBits(this.percent) == Double.doubleToLongBits(other.percent);
    }

    @Override
    public int compareTo(BatteryState o) {
        return Double.compare(percent, o.percent);
    }

}
