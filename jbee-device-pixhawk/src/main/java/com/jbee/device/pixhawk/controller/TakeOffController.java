package com.jbee.device.pixhawk.controller;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_global_position_int;
import com.MAVLink.common.msg_mission_ack;
import com.MAVLink.common.msg_mission_item_reached;
import com.MAVLink.common.msg_mission_request;
import com.MAVLink.enums.MAV_MODE_FLAG;
import com.MAVLink.enums.MAV_STATE;
import com.jbee.commands.CommandResult;
import com.jbee.commands.TakeOffCommand;
import com.jbee.device.pixhawk.internal.Pixhawk;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erik Jähne
 */
public class TakeOffController implements Consumer<MAVLinkPacket>{

    private int height = 5;//m
    private Integer state = 0;
    private final Boolean ready = true;
    private CommandResult result;
    
    private final Pixhawk pixhawk;

    public TakeOffController(Pixhawk pixhawk) {
        this.pixhawk = pixhawk;
    }
    
    public CommandResult execute(TakeOffCommand takeOffCommand) {
            result = CommandResult.NOT_EXECUTED;
            int base_mode =  pixhawk.getHeartbeat().base_mode & 0xFF;

            state = 0;
            pixhawk.registerMavlinkPacketReceiver(TakeOffController.class.getName(), this);
            pixhawk.performingTakeOff = true;
            pixhawk.setMode(base_mode | MAV_MODE_FLAG.MAV_MODE_FLAG_AUTO_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_GUIDED_ENABLED | MAV_MODE_FLAG.MAV_MODE_FLAG_SAFETY_ARMED , 0);
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
            pixhawk.removeMavlinkReceiver(TakeOffController.class.getName());
            pixhawk.performingTakeOff = false;
            pixhawk.setMode(base_mode, 0);
       return result;
    }

    @Override
    public void accept(MAVLinkPacket t) {
        switch(state){
            case 0:{
                if(t.msgid == msg_mission_request.MAVLINK_MSG_ID_MISSION_REQUEST){
                    msg_global_position_int gpsStatus = pixhawk.getGpsStatus();
                    pixhawk.takeOff(0, (float)(gpsStatus.lat / 1e7), (float)(gpsStatus.lon  / 1e7), height);
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
