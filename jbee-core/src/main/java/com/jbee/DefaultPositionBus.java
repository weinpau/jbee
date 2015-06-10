package com.jbee;

import com.jbee.buses.AltitudeBus;
import com.jbee.buses.LatLonBus;
import com.jbee.buses.PositionBus;
import com.jbee.buses.GlobalVelocityBus;
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
            VelocityPositionEstimator positionEstimator = new VelocityPositionEstimator();
            
            busRegistry.get(GlobalVelocityBus.class).
                    orElseThrow(() -> new BeeBootstrapException("A translational velocity bus is missing.")).
                    subscripe(v -> {
                        
                        publish(positionEstimator.estimatePosition(v));
                    });
            
            busRegistry.get(AltitudeBus.class).
                    ifPresent(b -> b.subscripe(d -> {
                        publish(positionEstimator.estimatePosition(d));
                    }));
        } else {
            
            LatLonPositionEstimator positionEstimator = new LatLonPositionEstimator();
            
            busRegistry.get(LatLonBus.class).
                    orElseThrow(() -> new BeeBootstrapException("A GPS bus is missing.")).
                    subscripe(geo -> {
                        publish(positionEstimator.estimatePosition(origin, geo));
                    });
            
            busRegistry.get(AltitudeBus.class).
                    ifPresent(b -> b.subscripe(d -> {
                        publish(positionEstimator.estimatePosition(origin, d));
                    }));
        }
    }
    
    static class LatLonPositionEstimator {
        
        LatLon geo;
        Distance altitude;
        
        Position estimatePosition(LatLon origin, Distance altitude) {
            this.altitude = altitude;
            return createPosition(origin);
        }
        
        Position estimatePosition(LatLon origin, LatLon geo) {
            this.geo = geo;
            return createPosition(origin);
        }
        
        private Position createPosition(LatLon origin) {
            if (geo == null && altitude != null) {
                return Position.ORIGIN.withZ(altitude.toMeters());
            } else if (geo != null && altitude == null) {
                return geo.toPosition(origin);
            } else {
                return geo.toPosition(origin).withZ(altitude.toMeters());
            }
        }        
    }
    
    static class VelocityPositionEstimator {
        
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
