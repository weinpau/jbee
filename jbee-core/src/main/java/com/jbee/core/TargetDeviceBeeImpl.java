package com.jbee.core;

import com.jbee.Bee;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeControl;
import com.jbee.TargetDevice;

/**
 *
 * @author weinpau
 */
class TargetDeviceBeeImpl implements Bee {

    TargetDevice device;

    TargetDeviceBeeImpl(TargetDevice device) {
        this.device = device;
    }

    @Override
    public BeeControl bootstrap() throws BeeBootstrapException {
        System.out.println("bootstrapping " + device.getId());
        return null;
    }

}
