package com.jbee;

/**
 *
 * @author weinpau
 */
public final class BatteryState implements Comparable<BatteryState> {
    
    private final boolean tooLow;
    private final double level;

    public BatteryState(double level, boolean tooLow) {
        if (level < 0 || level > 1) {
            throw new IllegalArgumentException("Battery level must be between 0 and 1.");
        }

        this.tooLow = tooLow;
        this.level = level;
    }

    public double getLevel() {
        return level;
    }

    public boolean isTooLow() {
        return tooLow;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.tooLow ? 1 : 0);
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.level) ^ (Double.doubleToLongBits(this.level) >>> 32));
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
        return Double.doubleToLongBits(this.level) == Double.doubleToLongBits(other.level);
    }

    @Override
    public int compareTo(BatteryState o) {
        return Double.compare(level, o.level);
    }

}
