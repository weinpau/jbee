package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.Priority;
import com.jbee.units.Angle;

/**
 *
 * @author weinpau
 */
public class YAWBus extends Bus<Angle> {

    public YAWBus() {
        super(Priority.MEDIUM);
    }

    public YAWBus(Priority priority) {
        super(priority);
    }

}
