package com.jbee;

import com.jbee.buses.PositionBus;
import com.jbee.positioning.Position;

/**
 *
 * @author weinpau
 */
class PositionEstimator extends PositionBus {
    
    
    Position currentPosition = Position.ORIGIN; 
    
    public void reset(Position position) {
        
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }
    
}
