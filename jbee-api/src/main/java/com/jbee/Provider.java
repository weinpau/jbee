package com.jbee;

/**
 *
 * @author weinpau
 * @param <T>
 */
public interface Provider<T> {

    T get();

    default void bootstrap() throws BeeBootstrapException {

    }
;

}
