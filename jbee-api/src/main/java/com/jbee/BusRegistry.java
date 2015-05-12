package com.jbee;

import java.util.Collection;
import java.util.Optional;

/**
 *
 * @author weinpau
 */
public interface BusRegistry {

    Collection<Bus> getAll();

    <T extends Bus> Collection<T> getAll(Class<T> busType);

    default <T extends Bus> Optional<T> get(Class<T> busType) {
        return getAll(busType).stream().sorted().findFirst();
    }
}
