package com.jbee.device.pixhawk.controller;

import com.jbee.commands.CancelCommand;
import com.jbee.commands.CommandResult;
import com.jbee.device.pixhawk.internal.PixhawkController;

/**
 *
 * @author Erik Jähne
 */
public class CancelController {

    private final PixhawkController pixhawk;

    public CancelController(PixhawkController pixhawk) {
        this.pixhawk = pixhawk;
    }

    public CommandResult execute(CancelCommand cancelCommand) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
