package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.Priority;
import com.jbee.units.Velocity3D;

/**
 *
 * @author weinpau
 */
public class TranslationalVelocityBus extends Bus<Velocity3D> {

    public TranslationalVelocityBus() {
        super(Priority.MEDIUM);
    }

    public TranslationalVelocityBus(Priority priority) {
        super(priority);
    }
    
    
    
}
