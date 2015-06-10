package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.AxisAngles;
import com.jbee.Priority;

/**
 *
 * @author weinpau
 */
public class AxisAnglesBus extends Bus<AxisAngles> {

    public AxisAnglesBus() {
        super(Priority.MEDIUM);
    }

    public AxisAnglesBus(Priority priority) {
        super(priority);
    }

}
