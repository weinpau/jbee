package com.jbee.positioning;

import com.jbee.units.Distance;
import static java.lang.Math.*;
import java.text.DecimalFormat;

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

    public Position addX(double x) {
        return new Position(this.x + x, y, z);
    }

    public Position addY(double y) {
        return new Position(x, this.y + y, z);
    }

    public Position addZ(double z) {
        return new Position(x, y, this.z + z);
    }

    public Position abs() {
        return new Position(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public Position add(Position position) {
        return new Position(x + position.x, y + position.y, z + position.z);
    }

    public Position sub(Position position) {
        return new Position(x - position.x, y - position.y, z - position.z);
    }

    public Position multiply(double factor) {
        return new Position(x * factor, y * factor, z * factor);
    }

    public Position normalize() {
        double length = sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2));
        if (length == 0) {
            return new Position(0, 0, 0);
        }
        return multiply(1d / length);
    }

    public Distance distance(Position position) {
        return Distance.ofMilimeters((int) (1000 * sqrt(pow(x - position.x, 2) + pow(y - position.y, 2) + pow(z - position.z, 2))));
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.###");
        return "Position{" + "x=" + df.format(x) + ", y=" + df.format(y) + ", z=" + df.format(z) + '}';
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
