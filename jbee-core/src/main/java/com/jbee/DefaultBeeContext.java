package com.jbee;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author weinpau
 */
class DefaultBeeContext implements BeeContext {

    final TargetDevice device;

    final Set<Provider> providers = new LinkedHashSet<>();

    DefaultBeeContext(TargetDevice device) {
        this.device = device;
    }

    @Override
    public Bee bootstrap() throws BeeBootstrapException {
        device.bootstrap();
        return new BeeImpl(device);
    }

    @Override
    public BeeContext register(Class<?>... component) {

        try {
            for (Class c : component) {
                if (Provider.class.isAssignableFrom(c)) {
                    providers.add((Provider) c.newInstance());
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Collection<Provider> getAllProviders() {
        return Collections.unmodifiableSet(providers);
    }

    @Override
    public Collection<Provider> getProviders(Class<? extends Provider> providerType) {
        return providers.stream().
                filter(p -> providerType.isAssignableFrom(p.getClass())).
                collect(Collectors.toSet());
    }

}
