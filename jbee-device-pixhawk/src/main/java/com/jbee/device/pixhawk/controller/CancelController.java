package com.jbee.device.pixhawk.controller;

import com.jbee.commands.CancelCommand;
import com.jbee.commands.CommandResult;
import com.jbee.device.pixhawk.internal.Pixhawk;

/**
 *
 * @author Erik Jähne
 */
public class CancelController {

    private final Pixhawk pixhawk;

    public CancelController(Pixhawk pixhawk) {
        this.pixhawk = pixhawk;
    }

    public CommandResult execute(CancelCommand cancelCommand) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
