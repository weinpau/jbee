package com.jbee.buses;

import com.jbee.BeeState;
import com.jbee.Bus;
import com.jbee.Priority;

/**
 *
 * @author weinpau
 */
public class BeeStateBus extends Bus<BeeState> {

    public BeeStateBus() {
        super(Priority.MEDIUM);
    }

    public BeeStateBus(Priority priority) {
        super(priority);
    }

}
