package com.jbee;

import com.jbee.buses.AltitudeBus;
import com.jbee.buses.PositionBus;
import com.jbee.buses.VelocityBus;
import com.jbee.positioning.Position;

/**
 *
 * @author weinpau
 */
final class PositionEstimator extends PositionBus {

    static final long NANOS_PER_SECOND = 1000_000_000L;

    Velocity velocity = Velocity.ZERO;
  
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
        context.getBus(VelocityBus.class).
                orElseThrow(() -> new BeeBootstrapException("A translational velocity bus is missing.")).
                subscripe(v -> {
                    velocity = v;
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
        double vx = velocity.getX().mps();
        double vy = velocity.getY().mps();
        double vz = velocity.getZ().mps();

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