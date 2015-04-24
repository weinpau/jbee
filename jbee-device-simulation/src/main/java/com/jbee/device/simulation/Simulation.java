package com.jbee.device.simulation;

import com.jbee.BeeState;
import com.jbee.TargetDevice;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;

/**
 *
 * @author weinpau
 */
public class Simulation implements TargetDevice {

    @Override
    public String getId() {
        return "simulation";
    }

    @Override
    public BeeState getCurrentState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommandResult execute(Command command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
