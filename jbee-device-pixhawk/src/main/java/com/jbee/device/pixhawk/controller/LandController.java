package com.jbee.device.pixhawk.controller;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_mission_ack;
import com.MAVLink.common.msg_mission_item_reached;
import com.MAVLink.common.msg_mission_request;
import com.MAVLink.enums.MAV_MODE_FLAG;
import com.jbee.commands.CommandResult;
import com.jbee.commands.Commands;
import com.jbee.commands.LandCommand;
import com.jbee.device.pixhawk.internal.PixhawkController;
import com.jbee.units.Distance;
import com.jbee.units.Speed;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erik Jähne
 */
public class LandController extends BasicController{

    private final Boolean ready = true;
    private int state;
    private CommandResult result;
    private final PixhawkController pixhawk;

    public LandController(PixhawkController pixhawk) {
        this.pixhawk = pixhawk;
    }

    public CommandResult execute(LandCommand landCommand) { 
        FlyController fly = new FlyController(pixhawk);
        pixhawk.performingLand = true;
        double height = -pixhawk.getLocalPosition().z;
        if(height > 2){
            result = fly.execute(Commands.down(Distance.ofMeters(height-2)).build());
            if(result != CommandResult.COMPLETED) return result;
        }
        result = fly.execute(Commands.down(Distance.ofMeters(-pixhawk.getLocalPosition().z)).with(Speed.mps(0.1)).build());
        pixhawk.performingLand = false;
        if(pixhawk.getLocalPosition().z > -10){
            int base_mode =  pixhawk.getHeartbeat().base_mode & 0xFF;
            pixhawk.setMode(base_mode & (~MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED) , 0);
        }
        return result;
    }
}
