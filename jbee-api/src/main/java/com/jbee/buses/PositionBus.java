package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.Priority;
import com.jbee.positioning.Position;

/**
 *
 * @author weinpau
 */
public class PositionBus extends Bus<Position> {

    public PositionBus() {
        super(Priority.MEDIUM);
    }

    public PositionBus(Priority priority) {
        super(priority);
    }
 
    
    
}
