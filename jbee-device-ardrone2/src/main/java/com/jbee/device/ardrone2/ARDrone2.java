package com.jbee.device.ardrone2;

import com.jbee.BeeBootstrapException;
import com.jbee.TargetDevice;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import java.util.concurrent.RunnableFuture;

/**
 *
 * @author weinpau
 */
public class ARDrone2 implements TargetDevice {

    @Override
    public String getId() {
        return "ar-drone2";
    }

    @Override
    public RunnableFuture<CommandResult> execute(Command command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bootstrap() throws BeeBootstrapException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disconnect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

}
