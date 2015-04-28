package com.jbee;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author weinpau
 */
public abstract class BeeModule {

    private final Set<Object> components = new HashSet<>();

    protected void register(Object component) {
        components.add(component);
    }

    public Set<Object> getComponents() {
        return Collections.unmodifiableSet(components);
    }

}
