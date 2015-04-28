package com.jbee;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.stream.Stream;

/**
 *
 * @author weinpau
 */
public interface BeeContext {

    Bee bootstrap() throws BeeBootstrapException;

    BeeContext register(Class<?>... component);

    Collection<Provider> getAllProviders();

    Collection<Provider> getProviders(Class<? extends Provider> providerType);

    @SuppressWarnings("UseSpecificCatch")
    static BeeContext of(TargetDevice device, BeeModule... module) {
        try {
            String defaultContext = "com.jbee.DefaultBeeContext";
            Constructor<?> constructor = Class.forName(defaultContext).getDeclaredConstructor(TargetDevice.class);
            constructor.setAccessible(true);
            BeeContext context = BeeContext.class.cast(constructor.newInstance(device));
            Stream.of(module).forEach(m -> context.register(m.getClasses().toArray(new Class[0])));
            if (device instanceof BeeModule) {
                context.register(((BeeModule) device).getClasses().toArray(new Class[0]));
            }
            return context;
        } catch (Exception exception) {
            throw new RuntimeException("Default implementation can not be instantiated.", exception);
        }
    }

}
