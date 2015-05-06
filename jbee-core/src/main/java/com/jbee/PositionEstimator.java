package com.jbee;

import com.jbee.buses.AltitudeBus;
import com.jbee.buses.PositionBus;
import com.jbee.buses.TranslationalVelocityBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Velocity3D;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author weinpau
 */
final class PositionEstimator extends PositionBus {

    static final long NANOS_PER_SECOND = 1000_000_000L;

    Velocity3D translationalVelocity = Velocity3D.ZERO;
  
    long lastTime;
    double x, y, z;

    Double altitude;

    BeeContext context;

    PositionEstimator() {
    }

    PositionEstimator(BeeContext context) {
        this.context = context;
    }

    @Override
    public void bootstrap() throws BeeBootstrapException {
        context.getBus(TranslationalVelocityBus.class).
                orElseThrow(() -> new BeeBootstrapException("A translational velocity bus is missing.")).
                subscripe(v -> {
                    translationalVelocity = v;
                    publish(estimatePosition());
                });

        context.getBus(AltitudeBus.class).
                ifPresent(b -> b.subscripe(d -> {
                    altitude = d.toMeters();
                    publish(estimatePosition());
                }));
    }

    Position estimatePosition() {
        long now = System.nanoTime();
        if (lastTime == 0) {
            lastTime = now;
            return Position.ORIGIN;
        }
        double dt = (now - lastTime) / (double) NANOS_PER_SECOND;
        double vx = translationalVelocity.getXVelocity().mps();
        double vy = translationalVelocity.getYVelocity().mps();
        double vz = translationalVelocity.getZVelocity().mps();

        x += dt * vx;
        y += dt * vy;
        
        if (altitude == null) {
            z += dt * vz;
        } else {
            z = altitude;
        }
        lastTime = now;
        return new Position(x, y, z);
    }

}
