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
    DefaultBee bee;

    final TargetDevice device;

    final Set<Bus> buses = new LinkedHashSet<>();

    final DefaultBusRegistry busRegistry = new DefaultBusRegistry();

    DefaultBeeContext(TargetDevice device) {
        this.device = device;
        register(new DefaultPositionBus());
        register(new DefaultBeeStateBus());
    }

    @Override
    public Bee bootstrap() throws BeeBootstrapException {
        if (bee != null) {
            throw new BeeBootstrapException("Bee has already been created.");
        }
        bootstrapBuses();
        device.bootstrap(busRegistry);
        bee = new DefaultBee(device);
        bee.init(busRegistry);
        return bee;
    }

    void bootstrapBuses() throws BeeBootstrapException {
        for (Bus b : busRegistry.getAll()) {
            b.bootstrap(device, busRegistry);
        }
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
