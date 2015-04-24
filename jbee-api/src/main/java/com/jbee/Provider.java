package com.jbee;

/**
 *
 * @author weinpau
 * @param <T>
 */
public interface Provider<T> {

    void bootstrap(Bee bee) throws BeeBootstrapException;

    T get();

}
