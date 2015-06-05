package com.jbee.device.pixhawk.controller;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.enums.MAV_MODE_FLAG;
import com.jbee.commands.CommandResult;
import com.jbee.commands.Commands;
import com.jbee.commands.TakeOffCommand;
import com.jbee.device.pixhawk.internal.PixhawkController;
import com.jbee.units.Distance;
import java.util.function.Consumer;

/**
 *
 * @author Erik Jähne
 */
public class TakeOffController extends BasicController{

    private final static int HEIGHT = 3;//m
    private CommandResult result;
    
    private final PixhawkController pixhawk;

    public TakeOffController(PixhawkController pixhawk) {
        this.pixhawk = pixhawk;
    }
    
     public CommandResult execute(TakeOffCommand takeOffCommand) {
        FlyController fly = new FlyController(pixhawk);
        int base_mode =  pixhawk.getHeartbeat().base_mode & 0xFF;
        int mode = base_mode;
        base_mode |= MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED | MAV_MODE_FLAG.MAV_MODE_FLAG_AUTO_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_GUIDED_ENABLED;
        pixhawk.performingTakeOff = true;
        if(base_mode != mode){
            pixhawk.setMode(base_mode, 0);
        }
        result = fly.execute(Commands.up(Distance.ofMeters(HEIGHT)).build());
        pixhawk.performingTakeOff = false;
        return result;
     }
}
