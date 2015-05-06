package com.jbee;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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

    DefaultBeeContext(TargetDevice device) {
        this.device = device;
        register(new PositionEstimator(this));
    }

    @Override
    public Bee bootstrap() throws BeeBootstrapException {
        if (bee != null) {
            throw new BeeBootstrapException("Bee has already been created.");
        }
        device.bootstrap();
        bootstrapBuses();

        bee = new DefaultBee(device, new BeeStateBus(this));
        return bee;
    }

    void bootstrapBuses() throws BeeBootstrapException {
        for (Bus b : getAllBuses()) {
            b.bootstrap();
        }
    }

    @Override
    public final BeeContext register(Object... components) {
        for (Object comp : components) {
            if (comp instanceof Bus) {
                buses.add((Bus) comp);
            }
        }
        return this;
    }

    @Override
    public Collection<Bus> getAllBuses() {
        return Collections.unmodifiableSet(buses);
    }

    @Override
    public <T extends Bus> Collection<T> getBuses(Class<T> busType) {
        Collection<T> result = new HashSet<>();
        buses.stream().
                filter(b -> busType.isAssignableFrom(b.getClass())).
                forEach(b -> result.add((T) b));
        return result;

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
