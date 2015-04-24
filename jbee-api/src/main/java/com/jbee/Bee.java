package com.jbee;

import java.lang.reflect.Constructor;

/**
 *
 * @author weinpau
 */
public interface Bee {
    
    BeeControl bootstrap() throws BeeBootstrapException;
    
    @SuppressWarnings("UseSpecificCatch")
    public static Bee of(TargetDevice device) {
        try {
            String defaultBeeImpl = "com.jbee.core.TargetDeviceBeeImpl";
            Constructor<?> constructor = Class.forName(defaultBeeImpl).getDeclaredConstructor(TargetDevice.class);
            constructor.setAccessible(true);
            return Bee.class.cast(constructor.newInstance(device));
        } catch (Exception exception) {
            throw new RuntimeException("Default implementation can not be instantiated.", exception);
        }
    }
    
}
