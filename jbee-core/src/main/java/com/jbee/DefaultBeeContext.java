package com.jbee;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
        return new BeeImpl(device, new StateFactory(this));
    }

    @Override
    public BeeContext register(Object... components) {
        for (Object comp : components) {
            if (comp instanceof Provider) {
                providers.add((Provider) comp);
            }
        }
        return this;
    }

    @Override
    public Collection<Provider> getAllProviders() {
        return Collections.unmodifiableSet(providers);
    }

    @Override
    public <P extends Provider> Collection<P> getProviders(Class<P> providerType) {
        Collection<P> result = new HashSet<>();
        providers.stream().
                filter(p -> providerType.isAssignableFrom(p.getClass())).
                forEach(p -> result.add((P) p));
        return result;

    }

}
