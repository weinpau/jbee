package com.jbee;

/**
 *
 * @author weinpau
 */
public interface Bee {

    BeeControl bootstrap() throws BeeBootstrapException;
    
}
