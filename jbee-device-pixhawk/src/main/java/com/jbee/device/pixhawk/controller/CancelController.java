package com.jbee.device.pixhawk.controller;

import com.jbee.commands.CancelCommand;
import com.jbee.commands.CommandResult;
import com.jbee.device.pixhawk.internal.PixhawkController;

/**
 *
 * @author Erik Jähne
 */
public class CancelController extends BasicController{

    private final PixhawkController pixhawk;

    public CancelController(PixhawkController pixhawk) {
        this.pixhawk = pixhawk;
    }

    public CommandResult execute(CancelCommand cancelCommand) {
        setCancle(true);
        return CommandResult.COMPLETED;
    }

    @Override
    public void onCanle() {
        
    }
    
    

}
