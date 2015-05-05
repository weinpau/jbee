package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.Priority;
import com.jbee.units.Distance;

/**
 *
 * @author weinpau
 */
public class HeightBus extends Bus<Distance> {

    public HeightBus() {
        super(Priority.MEDIUM);
    }

    public HeightBus(Priority priority) {
        super(priority);
    }

}
