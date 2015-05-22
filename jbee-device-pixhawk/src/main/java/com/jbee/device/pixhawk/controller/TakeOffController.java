package com.jbee.device.pixhawk.controller;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_gps_raw_int;
import com.MAVLink.common.msg_mission_ack;
import com.MAVLink.common.msg_mission_item_reached;
import com.MAVLink.common.msg_mission_request;
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

    private int height = 2;//m
    private Integer state = 0;
    private Boolean ready;
    private CommandResult result;
    
    private final Pixhawk pixhawk;

    public TakeOffController(Pixhawk pixhawk) {
        this.pixhawk = pixhawk;
    }
    
    public CommandResult execute(TakeOffCommand takeOffCommand) {
       synchronized(ready){
           pixhawk.performingTakeOff = true;
            state = 0;
            result = CommandResult.NOT_EXECUTED;
            ready = false;
            pixhawk.registerMavlinkPacketReceiver(TakeOffController.class.getName(), this);
            pixhawk.setMissionCount(0);
            try {
                ready.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(TakeOffController.class.getName()).log(Level.SEVERE, null, ex);
            }
            pixhawk.removeMavlinkReceiver(TakeOffController.class.getName());
            pixhawk.performingTakeOff = false;
       }
       return result;
    }

    @Override
    public void accept(MAVLinkPacket t) {
        switch(state){
            case 0:{
                if(t.msgid == msg_mission_request.MAVLINK_MSG_ID_MISSION_REQUEST){
                    msg_gps_raw_int gpsStatus = pixhawk.getGpsStatus();
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
                    ready = true;
                    ready.notifyAll();
                }
            }
        }
    }
}
