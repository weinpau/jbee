package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.Priority;
import com.jbee.Velocity;

/**
 *
 * @author weinpau
 */
public class GlobalVelocityBus extends Bus<Velocity> {

    public GlobalVelocityBus() {
        super(Priority.MEDIUM);
    }

    public GlobalVelocityBus(Priority priority) {
        super(priority);
    }
    
    
    
}
