package com.jbee;

import com.jbee.units.Speed;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Objects;

/**
 * 
 * @author weinpau
 */
public class GlobalVelocity {

    public static final GlobalVelocity ZERO = new GlobalVelocity(Speed.ZERO, Speed.ZERO, Speed.ZERO);

    private final Speed x; 
    private final Speed y;
    private final Speed z;

    public GlobalVelocity(Speed x, Speed y, Speed z) {
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

    public GlobalVelocity withX(Speed x) {
        return new GlobalVelocity(x, y, z);
    }

    public GlobalVelocity withY(Speed y) {
        return new GlobalVelocity(x, y, z);
    }

    public GlobalVelocity withZ(Speed z) {
        return new GlobalVelocity(x, y, z);
    }

    public GlobalVelocity abs() {
        return new GlobalVelocity(x.abs(), y.abs(), z.abs());
    }

    public GlobalVelocity add(GlobalVelocity velocity) {
        return new GlobalVelocity(x.add(velocity.x), y.add(velocity.y), z.add(velocity.z));
    }

    public GlobalVelocity sub(GlobalVelocity velocity) {
        return add(velocity.multiply(-1));
    }

    public GlobalVelocity multiply(double factor) {
        if (!Double.isFinite(factor)) {
            throw new IllegalArgumentException("The factor must be a finite number.");
        }
        return new GlobalVelocity(x.multiply(factor), y.multiply(factor), z.multiply(factor));
    }

    public Speed totalSpeed() {
        return Speed.mps(sqrt(pow(x.mps(), 2) + pow(y.mps(), 2) + pow(z.mps(), 2)));
    }

    public boolean isZero() {
        return x.isZero() && y.isZero() && z.isZero();
    }

    @Override
    public String toString() {
        return "Velocity{x=" + x + ", y=" + y + ", z=" + z + ", total=" + totalSpeed() + "}";
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
        final GlobalVelocity other = (GlobalVelocity) obj;
        if (!Objects.equals(this.x, other.x)) {
            return false;
        }
        if (!Objects.equals(this.y, other.y)) {
            return false;
        }
        return Objects.equals(this.z, other.z);
    }

}
