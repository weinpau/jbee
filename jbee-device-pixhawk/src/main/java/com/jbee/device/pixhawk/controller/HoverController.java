package com.jbee.device.pixhawk.controller;

import com.MAVLink.enums.MAV_MODE_FLAG;
import com.jbee.commands.CommandResult;
import com.jbee.commands.HoverCommand;
import com.jbee.device.pixhawk.internal.PixhawkController;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erik Jähne
 */
public class HoverController {

    private final PixhawkController pixhawk;

    public HoverController(PixhawkController pixhawk) {
        this.pixhawk = pixhawk;
    }

    public CommandResult execute(HoverCommand hoverCommand) {
        int baseMode = pixhawk.getHeartbeat().base_mode;
        baseMode |= MAV_MODE_FLAG.MAV_MODE_FLAG_STABILIZE_ENABLED |MAV_MODE_FLAG.MAV_MODE_FLAG_AUTO_ENABLED;
        pixhawk.setMode(baseMode, 0);
        try {
            Thread.sleep(hoverCommand.getDuration().toMillis());
        } catch (InterruptedException ex) {
            Logger.getLogger(HoverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return CommandResult.COMPLETED;
    }
    
    
    
}
