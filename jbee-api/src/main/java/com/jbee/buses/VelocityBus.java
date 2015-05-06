package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.Priority;
import com.jbee.Velocity;

/**
 *
 * @author weinpau
 */
public class VelocityBus extends Bus<Velocity> {

    public VelocityBus() {
        super(Priority.MEDIUM);
    }

    public VelocityBus(Priority priority) {
        super(priority);
    }
    
    
    
}
