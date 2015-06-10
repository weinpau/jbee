package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.Priority;
import com.jbee.GlobalVelocity;

/**
 *
 * @author weinpau
 */
public class GlobalVelocityBus extends Bus<GlobalVelocity> {

    public GlobalVelocityBus() {
        super(Priority.MEDIUM);
    }

    public GlobalVelocityBus(Priority priority) {
        super(priority);
    }
    
    
    
}
