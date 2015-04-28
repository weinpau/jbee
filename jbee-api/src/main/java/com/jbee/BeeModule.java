package com.jbee;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author weinpau
 */
public abstract class BeeModule {

    private final Set<Class<?>> components = new HashSet<>();

    protected void register(Class<?> component) {
        components.add(component);
    }

    public Set<Class> getClasses() {
        return Collections.unmodifiableSet(components);
    }

}
