package com.jbee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author weinpau
 */
class DefaultBusRegistry implements BusRegistry {

    final List<Bus> buses = new ArrayList<>();

    public void register(Bus bus) {
        buses.add(0, bus);
    }

    @Override
    public Collection<Bus> getAll() {
        return Collections.unmodifiableCollection(buses);
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
