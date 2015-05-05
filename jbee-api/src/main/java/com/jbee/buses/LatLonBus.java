package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.Priority;
import com.jbee.positioning.LatLon;

/**
 *
 * @author weinpau
 */
public class LatLonBus extends Bus<LatLon> {

    public LatLonBus() {
        super(Priority.MEDIUM);
    }

    public LatLonBus(Priority priority) {
        super(priority);
    }

    
    
}
