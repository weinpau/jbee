package com.jbee;

import com.jbee.positioning.BeeWorld;

/**
 *
 * @author weinpau
 */
public interface Bee {

    BeeControl control();

    BeeMonitor monitor();
    
    BeeWorld getWorld();

}
