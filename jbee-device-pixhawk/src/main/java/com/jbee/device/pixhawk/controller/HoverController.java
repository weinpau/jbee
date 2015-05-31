package com.jbee.device.pixhawk.controller;

import com.MAVLink.enums.MAV_MODE_FLAG;
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
        int baseMode = pixhawk.getHeartbeat().base_mode;
        baseMode |= MAV_MODE_FLAG.MAV_MODE_FLAG_STABILIZE_ENABLED |MAV_MODE_FLAG.MAV_MODE_FLAG_AUTO_ENABLED;
        pixhawk.setMode(baseMode, 0);
        return CommandResult.COMPLETED;
    }
    
    
    
}
