package com.jbee.positioning;

import com.jbee.units.Distance;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 *
 * @author weinpau
 */
public class Position {

    public static final Position ORIGIN = new Position(0, 0, 0);

    private final double x, y, z;

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Position withX(double x) {
        return new Position(x, y, z);
    }

    public Position withY(double y) {
        return new Position(x, y, z);
    }

    public Position withZ(double z) {
        return new Position(x, y, z);
    }

    public Position move(double deltaX, double deltaY, double deltaZ) {
        return new Position(x + deltaX, y + deltaY, z + deltaZ);
    }

    public Distance distance(Position position) {
        return Distance.ofMilimeters((int) (1000 * sqrt(pow(x - position.x, 2) + pow(y - position.y, 2) + pow(z - position.z, 2))));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
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
        final Position other = (Position) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return Double.doubleToLongBits(this.z) == Double.doubleToLongBits(other.z);
    }

}
