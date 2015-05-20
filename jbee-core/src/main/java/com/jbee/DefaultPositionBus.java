package com.jbee;

import com.jbee.buses.AltitudeBus;
import com.jbee.buses.LatLonBus;
import com.jbee.buses.PositionBus;
import com.jbee.buses.VelocityBus;
import com.jbee.positioning.LatLon;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;

/**
 *
 * @author weinpau
 */
final class DefaultPositionBus extends PositionBus {

    @Override
    public void bootstrap(TargetDevice device, BusRegistry busRegistry) throws BeeBootstrapException {

        LatLon origin = determineOrigin(busRegistry);

        if (origin == null) {
            PositionEstimator positionEstimator = new PositionEstimator();

            busRegistry.get(VelocityBus.class).
                    orElseThrow(() -> new BeeBootstrapException("A translational velocity bus is missing.")).
                    subscripe(v -> {

                        publish(positionEstimator.estimatePosition(v));
                    });

            busRegistry.get(AltitudeBus.class).
                    ifPresent(b -> b.subscripe(d -> {
                        publish(positionEstimator.estimatePosition(d));
                    }));
        } else {

            busRegistry.get(LatLonBus.class).
                    orElseThrow(() -> new BeeBootstrapException("A GPS bus is missing.")).
                    subscripe(geo -> {
                        publish(geo.toPosition(origin));
                    });
        }
    }

    static class PositionEstimator {

        static final long NANOS_PER_SECOND = 1000_000_000L;

        long lastTime;
        double x, y, z;

        Position estimatePosition(Distance altitude) {
            z = altitude.toMeters();
            return new Position(x, y, z);
        }

        Position estimatePosition(Velocity velocity) {
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
            z += dt * vz;
            lastTime = now;

            return new Position(x, y, z);
        }
    }

    private LatLon determineOrigin(BusRegistry busRegistry) throws BeeBootstrapException {

        OriginDeterminer originDeterminer = new OriginDeterminer(busRegistry);
        if (originDeterminer.isDeterminable()) {
            try {
                return originDeterminer.determineOrigin();
            } catch (InterruptedException e) {
                throw new BeeBootstrapException(e);
            }
        } else {
            return null;
        }
    }

}
