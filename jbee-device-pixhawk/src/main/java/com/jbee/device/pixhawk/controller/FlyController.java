package com.jbee.device.pixhawk.controller;

import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.device.pixhawk.internal.PixhawkController;

/**
 *
 * @author Erik Jähne
 */
public class FlyController {

    private final PixhawkController pixhawk;

    public FlyController(PixhawkController pixhawk) {
        this.pixhawk = pixhawk;
    }
    
    public CommandResult execute(FlyCommand flyCommand) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        
    }
    
    
    
}