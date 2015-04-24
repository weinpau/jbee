package com.jbee.core;

import com.jbee.Bee;
import com.jbee.BeeContext;
import com.jbee.BeeBootstrapException;
import com.jbee.Provider;
import com.jbee.TargetDevice;
import java.util.Collection;

/**
 *
 * @author weinpau
 */
class DefaultBeeContext implements BeeContext {

    TargetDevice device;

    DefaultBeeContext(TargetDevice device) {
        this.device = device;
    }

    @Override
    public Bee bootstrap() throws BeeBootstrapException {
        System.out.println("bootstrapping " + device.getId());
        return null;
    }

    @Override
    public BeeContext registerProvider(Provider provider) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Provider> getAllProviders() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Provider> getProviders(Class<? extends Provider> providerType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
