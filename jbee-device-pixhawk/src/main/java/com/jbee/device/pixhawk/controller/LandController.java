package com.jbee.device.pixhawk.controller;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_mission_ack;
import com.MAVLink.common.msg_mission_item_reached;
import com.MAVLink.common.msg_mission_request;
import com.jbee.commands.CommandResult;
import com.jbee.commands.LandCommand;
import com.jbee.device.pixhawk.internal.Pixhawk;
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
    private final Pixhawk pixhawk;

    public LandController(Pixhawk pixhawk) {
        this.pixhawk = pixhawk;
    }

    public CommandResult execute(LandCommand landCommand) {
        synchronized(ready){
            pixhawk.performingLand = true;
            state = 0;
            result = CommandResult.NOT_EXECUTED;
            pixhawk.registerMavlinkPacketReceiver(LandController.class.getName(), this);
            pixhawk.setMissionCount(0);
            try {
                ready.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(TakeOffController.class.getName()).log(Level.SEVERE, null, ex);
            }
            pixhawk.removeMavlinkReceiver(LandController.class.getName());
            pixhawk.performingLand = false;
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
