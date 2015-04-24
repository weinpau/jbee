package com.jbee.device.simulation;

import com.jbee.TargetDevice;

/**
 *
 * @author weinpau
 */
public class Simulation implements TargetDevice {

    @Override
    public String getId() {
        return "simulation";
    }

}
