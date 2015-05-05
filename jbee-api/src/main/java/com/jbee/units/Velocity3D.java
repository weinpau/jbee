package com.jbee.units;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class Velocity3D {

    public static final Velocity3D ZERO = new Velocity3D(Velocity.ZERO, Velocity.ZERO, Velocity.ZERO);
    
    private final Velocity x;
    private final Velocity y;
    private final Velocity z;

    public Velocity3D(Velocity x, Velocity y, Velocity z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Velocity getXVelocity() {
        return x;
    }

    public Velocity getYVelocity() {
        return y;
    }

    public Velocity getZVelocity() {
        return z;
    }

    public Velocity3D withXVelocity(Velocity x) {
        return new Velocity3D(x, y, z);
    }

    public Velocity3D withYVelocity(Velocity y) {
        return new Velocity3D(x, y, z);
    }

    public Velocity3D withZVelocity(Velocity z) {
        return new Velocity3D(x, y, z);
    }

    public Velocity3D multiply(double factor) {
        return new Velocity3D(x.multiply(factor), y.multiply(factor), z.multiply(factor));
    }

    public Velocity totalVelocity() {
        return Velocity.mps(sqrt(pow(x.mps(), 2) + pow(y.mps(), 2) + pow(z.mps(), 2)));
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
        final Velocity3D other = (Velocity3D) obj;
        if (!Objects.equals(this.x, other.x)) {
            return false;
        }
        if (!Objects.equals(this.y, other.y)) {
            return false;
        }
        return Objects.equals(this.z, other.z);
    }

}
