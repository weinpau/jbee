package com.jbee.buses;

import com.jbee.Bus;
import com.jbee.PrincipalAxes;
import com.jbee.Priority;

/**
 *
 * @author weinpau
 */
public class PrincipalAxesBus extends Bus<PrincipalAxes> {

    public PrincipalAxesBus() {
        super(Priority.MEDIUM);
    }

    public PrincipalAxesBus(Priority priority) {
        super(priority);
    }

}
