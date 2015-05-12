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
class DefaultBusRegistry implements BusRegistry {

    final Set<Bus> buses = new LinkedHashSet<>();

    public void register(Bus bus) {
        buses.add(bus);
    }

    @Override
    public Collection<Bus> getAll() {
        return Collections.unmodifiableSet(buses);
    }

    @Override
    public <T extends Bus> Collection<T> getAll(Class<T> busType) {
        Collection<T> result = new HashSet<>();
        buses.stream().
                filter(b -> busType.isAssignableFrom(b.getClass())).
                forEach(b -> result.add((T) b));
        return result;
    }

}
