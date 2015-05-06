package com.jbee;

import com.jbee.units.Speed;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class Velocity {

    public static final Velocity ZERO = new Velocity(Speed.ZERO, Speed.ZERO, Speed.ZERO);

    private final Speed x;
    private final Speed y;
    private final Speed z;

    public Velocity(Speed x, Speed y, Speed z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Speed getX() {
        return x;
    }

    public Speed getY() {
        return y;
    }

    public Speed getZ() {
        return z;
    }

  
    public Velocity withX(Speed x) {
        return new Velocity(x, y, z);
    }

    public Velocity withY(Speed y) {
        return new Velocity(x, y, z);
    }

    public Velocity withZ(Speed z) {
        return new Velocity(x, y, z);
    }

    public Velocity multiply(double factor) {
        return new Velocity(x.multiply(factor), y.multiply(factor), z.multiply(factor));
    }

    public Speed totalSpeed() {
        return Speed.mps(sqrt(pow(x.mps(), 2) + pow(y.mps(), 2) + pow(z.mps(), 2)));
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", z=" + z + ", total=" + totalSpeed();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.x);
        hash = 23 * hash + Objects.hashCode(this.y);
        hash = 23 * hash + Objects.hashCode(this.z);
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
        if (!Objects.equals(this.x, other.x)) {
            return false;
        }
        if (!Objects.equals(this.y, other.y)) {
            return false;
        }
        return Objects.equals(this.z, other.z);
    }

}
