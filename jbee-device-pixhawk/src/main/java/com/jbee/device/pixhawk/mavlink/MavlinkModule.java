package com.jbee.device.pixhawk.mavlink;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.common.msg_mission_clear_all;
import com.MAVLink.common.msg_mission_count;
import com.MAVLink.common.msg_mission_item;
import com.MAVLink.common.msg_param_request_read;
import com.MAVLink.common.msg_param_value;
import com.MAVLink.common.msg_set_mode;
import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.enums.MAV_FRAME;
import com.MAVLink.enums.MAV_QC_REGISTER_RESULT;
import com.MAVLink.quantocopter.msg_register_module;
import com.MAVLink.quantocopter.msg_register_result;
import com.MAVLink.quantocopter.msg_unregister_module;
import com.jbee.device.pixhawk.connection.Connection;
import java.util.function.Consumer;

/**
 *
 * @author Erik Jähne
 */
public class MavlinkModule implements Consumer<MAVLinkPacket>{
    
    private final int sysid;
    private final int compid;
    
    final private Object mutex = new Object();
    private int registerResult;
    private float param;
    
    private final Connection connection;

    /**
     * Register a MavlinkPacket Consumer class for receiving MavlinkPackages throug the Consumer callback
     * @param id a unique id of the Consumer to identify different Receivers. Its recomendet to use Class name
     * @param receiver The Consumer class for the callback
     */
    public void registerMavlinkPacketReceiver(String id, Consumer<MAVLinkPacket> receiver) {
        connection.registerMavlinkPacketReceiver(id, receiver);
    }
    /**
     * Removes the registerd Mavlink Receiver from the callback list
     * @param id the unique id of the Receiver
     */
    public void removeMavlinkReceiver(String id) {
        connection.removeMavlinkReceiver(id);
    }

    /**
     * Transforms a MavlinkMessage to a Mavlinkpacket and adding system and component IDs
     * @param msg the message to transform
     * @return the msg as MAvlinkpacket with updatet sys and component IDs
     */
    private MAVLinkPacket createPackage(MAVLinkMessage msg){
        MAVLinkPacket result = msg.pack();
        result.sysid = sysid;
        result.compid = compid;
        return result;
    }
        
    /**
     * Creates a new MavlinkModule for comunicating with the Pixhawk
     * @param sysid the System ID of this Module
     * @param compid the component ID of this Module
     * @param connection the connection for sending Messages to the Pixhawk
     */
    public MavlinkModule(int sysid, int compid,Connection connection) {
        this.sysid = sysid;
        this.compid = compid;
        this.connection = connection;
    }

    /**
     * Helper function to copy Strings to byte arrays
     * @param dest the destination byte array
     * @param s the string to be copied to byte array
     */
    private void stringToByteArray(byte[] dest,String s){
        int i = 0;
        for (; i < Math.min(s.length(),dest.length-1); i++) {
            dest[i] = (byte)s.charAt(i);
        }
        dest[i] = 0;
        
    }
    
    /**
     * Requests a Parameter Value from the Pixhawk. This will be done syncron
     * @param targetSysID the system ID of the pixhawk
     * @param targetCompID the component id of the pixhawk
     * @param id a String ID of the Parameter
     * @return the value of the parameter
     */
    public float getParam(byte targetSysID, byte targetCompID,String id){
        msg_param_request_read msg = new msg_param_request_read();
        stringToByteArray(msg.param_id, id);
        msg.param_index = -1;
        msg.target_component = targetCompID;
        msg.target_system = targetSysID;
        
        param = -1;
        synchronized(mutex){
            connection.registerMavlinkPacketReceiver(MavlinkModule.class.getName(), this);
            connection.sendPackage(createPackage(msg));
            try {
                mutex.wait(1000);
            } catch (InterruptedException ex) {}
            finally {
                connection.removeMavlinkReceiver(MavlinkModule.class.getName());
            }
        }
        return param;
    }
    
    /**
     * Register this MavlinkModule to the Mavlink Server on the Raspberry
     * @param name a name to identify this module
     * @param accessLevel the desired MAV_QC_ACCESS Level for access control
     * @return MAV_QC_REGISTER_RESULT to validate the registration
     */
    public int registerModule(String name,int accessLevel){
        msg_register_module msg = new msg_register_module();
        msg.access = (short)accessLevel;
        //msg.name = new byte[20];
        stringToByteArray(msg.name,name);
        registerResult = MAV_QC_REGISTER_RESULT.MAV_QC_REGISTER_RESULT_FAILD_OTHER;
        synchronized(mutex){
            connection.registerMavlinkPacketReceiver(MavlinkModule.class.getName(), this);
            connection.sendPackage(createPackage(msg));
            try {
                mutex.wait(2000);
            } catch (InterruptedException ex) {}
            finally {
                connection.removeMavlinkReceiver(MavlinkModule.class.getName());
            }
        }
        return registerResult;
    }
    
    /**
     * Unregisters the Module at the MavlinkServer on the Raspberry
     * @return MAV_QC_REGISTER_RESULT to validate the unregistration
     */
    public int unregisterModule(){
        msg_unregister_module msg = new msg_unregister_module();
        registerResult = MAV_QC_REGISTER_RESULT.MAV_QC_REGISTER_RESULT_FAILD_OTHER;
        synchronized(mutex){
            connection.registerMavlinkPacketReceiver(MavlinkModule.class.getName(), this);
            connection.sendPackage(createPackage(msg));
            try {
                mutex.wait(2000);
            } catch (InterruptedException ex) {}
            finally {
                connection.removeMavlinkReceiver(MavlinkModule.class.getName());
            }
        }
        return registerResult;
    }
    
    @Override
    public void accept(MAVLinkPacket t) {
        if(t.msgid == msg_register_result.MAVLINK_MSG_ID_REGISTER_RESULT){
            msg_register_result msg = new msg_register_result(t);
            registerResult = msg.RESULT;
            synchronized(mutex){
                mutex.notifyAll();
            }
        }
        else if(t.msgid == msg_param_value.MAVLINK_MSG_ID_PARAM_VALUE){
            msg_param_value msg = new msg_param_value(t);
            param = msg.param_value;
            synchronized(mutex){
                mutex.notifyAll();
            }
        }
    }

    /**
     * Send a Land Command to the Pixhawk
     * @param targetSysID the system id of the pixhawk
     * @param targetCompID the component id of the pixhawk
     * @param yawAngle the desired yaw angle in rad
     * @param latitude the desired latitude after takeOff in deg.
     * @param longtitude the desired longtitude after takeoff in deg.
     */
    public void sendLandCommand(byte targetSysID, byte targetCompID, float yawAngle, float latitude, float longtitude) {
        msg_mission_item msg = new msg_mission_item();
        msg.autocontinue = (byte) 0;
        msg.command = (short) MAV_CMD.MAV_CMD_NAV_LAND;
        msg.current = (byte) 1;
        msg.frame = MAV_FRAME.MAV_FRAME_GLOBAL;
        msg.seq = 0;
        msg.target_component = targetCompID;
        msg.target_system =  targetSysID;
        msg.param1 = 0;
        msg.param2 = 0;
        msg.param3 = 0;
        msg.param4 = yawAngle;
        msg.x = latitude;
        msg.y = longtitude;
        msg.z = 0;
        connection.sendPackage(createPackage(msg));
    }

        /**
     * Send a TakeOff Command to the Pixhawk
     * @param targetSysID the system id of the pixhawk
     * @param targetCompID the component id of the pixhawk
     * @param yawAngle the desired yaw angle in rad
     * @param latitude the desired latitude after takeOff in deg.
     * @param longtitude the desired longtitude after takeoff in deg.
     * @param altitude  the desired altitude to climb to
     */
    public void sendTakeoffCommand(byte targetSysID, byte targetCompID,float yawAngle, float latitude, float longtitude, float altitude) {
        msg_mission_item msg = new msg_mission_item();
        msg.autocontinue = (byte) 0;
        msg.command = (short) MAV_CMD.MAV_CMD_NAV_TAKEOFF;
        msg.current = (byte) 1;
        msg.frame = MAV_FRAME.MAV_FRAME_GLOBAL;
        msg.seq = 0;
        msg.target_component = targetCompID;
        msg.target_system =  targetSysID;
        msg.param1 = 0;
        msg.param2 = 0;
        msg.param3 = 0;
        msg.param4 = yawAngle;
        msg.x = latitude;
        msg.y = longtitude;
        msg.z = altitude;
        connection.sendPackage(createPackage(msg));
    }

    /**
     * Send a set_mission_count Command to the Pixhawk to set the Size of the Mission
     * This will be used to force the Pixhawk to update the Mission and request für eatch single Item
     * @param targetSysID the system id of the pixhawk
     * @param targetCompID the component id of the pixhawk
     * @param count the number of Items in the Mission
     */
    public void sendMissionCountCommand(byte targetSysID, byte targetCompID,int count) {
        msg_mission_count msg = new msg_mission_count();
        msg.count = (short) count;
        msg.target_component = targetCompID;
        msg.target_system =  targetSysID;
        connection.sendPackage(createPackage(msg));
    }

    /**
     * Send a mission_clear_all Command to erase all Misssion Items on the Pixhawk
     * @param targetSysID the system id of the pixhawk
     * @param targetCompID the component id of the pixhawk
     */
    public void sendMissionClearCommand(byte targetSysID, byte targetCompID){
        msg_mission_clear_all msg = new msg_mission_clear_all();
        msg.target_system = targetSysID;
        msg.target_component = targetCompID;
        connection.sendPackage(createPackage(msg));
    }
    /**
     * Send a set_mode Command to the Pixhawk to set the flight mode
     * @param targetSysID the system id of the pixhawk
     * @param targetCompID the component id of the pixhawk
     * @param base_mode the desired base Mode
     * @param custom_mode the desired custom mode
     */
    public void setMode(byte targetSysID, byte targetCompID,byte base_mode,byte custom_mode) {
        
        msg_set_mode msg = new msg_set_mode();
        msg.base_mode = base_mode;
        msg.custom_mode = custom_mode;
        msg.target_system = targetSysID;
        connection.sendPackage(createPackage(msg));
    }
}
