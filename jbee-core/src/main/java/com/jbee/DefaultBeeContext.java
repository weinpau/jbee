package com.jbee;

import com.jbee.buses.LatLonBus;
import com.jbee.positioning.LatLon;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author weinpau
 */
class DefaultBeeContext implements BeeContext {

    boolean closed = false;
    Bee bee;

    final TargetDevice device;

    final Set<Bus> buses = new LinkedHashSet<>();

    final DefaultBusRegistry busRegistry = new DefaultBusRegistry();

    DefaultBeeContext(TargetDevice device) {
        this.device = device;
        register(new PositionEstimator());
        register(new DefaultBeeStateBus());
    }

    @Override
    public Bee bootstrap() throws BeeBootstrapException {
        if (bee != null) {
            throw new BeeBootstrapException("Bee has already been created.");
        }
        bootstrapBuses();
        device.bootstrap(busRegistry);
        bee = new DefaultBee(device, busRegistry, determinePosition());
        return bee;
    }

    void bootstrapBuses() throws BeeBootstrapException {
        for (Bus b : busRegistry.getAll()) {
            b.bootstrap(device, busRegistry);
        }
    }

    LatLon determinePosition() {
        for (LatLonBus bus : busRegistry.getAll(LatLonBus.class)) {
            LatLon position = bus.getLastKnownValue().orElse(null);
            if (position != null) {
                return position;
            }
        }
        return null;
    }

    @Override
    public final BeeContext register(Object... components) {
        for (Object comp : components) {
            if (comp instanceof Bus) {
                busRegistry.register((Bus) comp);
            }
        }
        return this;
    }

    @Override
    public TargetDevice getTargetDevice() {
        return device;
    }

    @Override
    public void close() {
        if (bee == null) {
            throw new RuntimeException("Context can not be closed.");
        }
        if (!closed) {
            bee.close();
            closed = true;
        }
    }

}
