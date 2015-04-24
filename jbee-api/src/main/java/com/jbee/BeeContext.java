package com.jbee;

import java.lang.reflect.Constructor;
import java.util.Collection;

/**
 *
 * @author weinpau
 */
public interface BeeContext {

    Bee bootstrap() throws BeeBootstrapException;

    BeeContext registerProvider(Provider provider);

    Collection<Provider> getAllProviders();

    Collection<Provider> getProviders(Class<? extends Provider> providerType); 

    @SuppressWarnings("UseSpecificCatch")
    public static BeeContext of(TargetDevice device) {
        try {
            String defaultContext = "com.jbee.core.DefaultBeeContext";
            Constructor<?> constructor = Class.forName(defaultContext).getDeclaredConstructor(TargetDevice.class);
            constructor.setAccessible(true);
            return BeeContext.class.cast(constructor.newInstance(device));
        } catch (Exception exception) {
            throw new RuntimeException("Default implementation can not be instantiated.", exception);
        }
    }

}
