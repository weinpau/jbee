package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.Priority;
import com.jbee.units.Distance;

/**
 *
 * @author weinpau
 */
public class AltitudeBus extends Bus<Distance> {

    public AltitudeBus() {
        super(Priority.MEDIUM);
    }

    public AltitudeBus(Priority priority) {
        super(priority);
    }

}
