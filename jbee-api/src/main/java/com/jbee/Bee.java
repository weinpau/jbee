package com.jbee;

/**
 *
 * @author weinpau
 */
public interface Bee {

    BeeControl control();

    BeeMonitor monitor();
    
    BeeWorld getWorld();

}
