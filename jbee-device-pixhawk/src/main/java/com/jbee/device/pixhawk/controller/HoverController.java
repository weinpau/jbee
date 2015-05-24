package com.jbee.device.pixhawk.controller;

import com.jbee.commands.CommandResult;
import com.jbee.commands.HoverCommand;
import com.jbee.device.pixhawk.internal.Pixhawk;

/**
 *
 * @author Erik Jähne
 */
public class HoverController {

    private final Pixhawk pixhawk;

    public HoverController(Pixhawk pixhawk) {
        this.pixhawk = pixhawk;
    }

    public CommandResult execute(HoverCommand hoverCommand) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
