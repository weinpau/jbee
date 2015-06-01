package com.jbee.device.pixhawk.controller;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_mission_ack;
import com.MAVLink.common.msg_mission_item_reached;
import com.MAVLink.common.msg_mission_request;
import com.MAVLink.enums.MAV_MODE_FLAG;
import com.jbee.commands.CommandResult;
import com.jbee.commands.LandCommand;
import com.jbee.device.pixhawk.internal.PixhawkController;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erik Jähne
 */
public class LandController implements Consumer<MAVLinkPacket>{

    private final Boolean ready = true;
    private int state;
    private CommandResult result;
    private final PixhawkController pixhawk;

    public LandController(PixhawkController pixhawk) {
        this.pixhawk = pixhawk;
    }

    public CommandResult execute(LandCommand landCommand) {
            pixhawk.performingLand = true;
            state = 0;
            result = CommandResult.NOT_EXECUTED;
            int base_mode =  pixhawk.getHeartbeat().base_mode & 0xFF;
            pixhawk.setMode(base_mode | MAV_MODE_FLAG.MAV_MODE_FLAG_AUTO_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_GUIDED_ENABLED , 0);
            pixhawk.registerMavlinkPacketReceiver(LandController.class.getName(), this);
            pixhawk.clearMission();
            pixhawk.setMissionCount(1);
            try {
                synchronized(ready){
                    ready.wait();
               }
            } catch (InterruptedException ex) {
                Logger.getLogger(TakeOffController.class.getName()).log(Level.SEVERE, null, ex);
            }
            pixhawk.clearMission();
            pixhawk.removeMavlinkReceiver(LandController.class.getName());
            pixhawk.performingLand = false;
            if(pixhawk.getGpsStatus().alt < 100){
                pixhawk.setMode(base_mode & (~MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED) , 0);
            }
       return result;
    }

    @Override
    public void accept(MAVLinkPacket t) {
        switch(state){
            case 0:{
                if(t.msgid == msg_mission_request.MAVLINK_MSG_ID_MISSION_REQUEST){
                    pixhawk.land(0);
                    result = CommandResult.CANCELLED;
                    state = 1;
                }
            }break;
            case 1:{
                if(t.msgid == msg_mission_ack.MAVLINK_MSG_ID_MISSION_ACK){
                    state = 2;
                }
            }break;
            case 2:{
                if(t.msgid == msg_mission_item_reached.MAVLINK_MSG_ID_MISSION_ITEM_REACHED){
                    result = CommandResult.COMPLETED;
                    state = 0;
                    synchronized(ready){
                        ready.notifyAll();
                    }   
                }
            }
        }
    }   
}
