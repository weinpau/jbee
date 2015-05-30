package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.Priority;
import com.jbee.AxisVelocity;

/**
 *
 * @author weinpau
 */
public class AxisVelocityBus extends Bus<AxisVelocity> {

    public AxisVelocityBus() {
        super(Priority.MEDIUM);
    }

    public AxisVelocityBus(Priority priority) {
        super(priority);
    }
    
    
    
}
