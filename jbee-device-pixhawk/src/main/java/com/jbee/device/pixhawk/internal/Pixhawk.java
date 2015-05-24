package com.jbee.device.pixhawk.internal;

import com.MAVLink.MAVLinkPacket;
import com.jbee.device.pixhawk.mavlink.MavlinkModule;
import java.util.function.Consumer;


/**
 *
 * @author Erik Jähne
 */
public class Pixhawk extends PixhawkStateListener{
    
    final MavlinkModule mavlink;

    /**
     * The Pixhawk Representation to generate Commands and get state information trough the StateListener
     * @param module a Mavlik Module for generating mavlink messages for the commands
     */
    public Pixhawk(MavlinkModule module) {
        mavlink = module;
        mavlink.registerMavlinkPacketReceiver(Pixhawk.class.getName(), this);
    }

    /**
     * Register a MavlinkPacket Consumer class for receiving MavlinkPackages throug the Consumer callback
     * @param id a unique id of the Consumer to identify different Receivers. Its recomendet to use Class name
     * @param receiver The Consumer class for the callback
     */
    public void registerMavlinkPacketReceiver(String id, Consumer<MAVLinkPacket> receiver) {
        mavlink.registerMavlinkPacketReceiver(id, receiver);
    }

    /**
     * Removes the registerd Mavlink Receiver from the callback list
     * @param id the unique id of the Receiver
     */
    public void removeMavlinkReceiver(String id) {
        mavlink.removeMavlinkReceiver(id);
    }

    /**
     * Requests a Parameter Value from the Pixhawk. This will be done syncron
     * @param id a String ID of the Parameter
     * @return the value of the parameter
     */
    public float getParam(String id) {
        return mavlink.getParam(getTargetSysID(), getTargetCompID(), id);
    }

    /**
     * Send a TakeOff Command to the Pixhawk with a desired Postion
     * @param yawAngle the desired yaw angle in rad
     * @param latitude the desired latitude after takeOff in deg.
     * @param longtitude the desired longtitude after takeoff in deg.
     * @param altitude the desired altitude in m
     */
    public void takeOff(float yawAngle, float latitude, float longtitude, float altitude){
        mavlink.sendTakeoffCommand(getTargetSysID(),getTargetCompID(), yawAngle, latitude, longtitude, altitude);
    }
    /**
     * Send a TakeOff command to the Pixhawk at the current Position
     * @param yawAngle the desired yaw angle in rad
     * @param altitude the desired altitude in m
     */
    public void takeOff(float yawAngle, float altitude){
        mavlink.sendTakeoffCommand(getTargetSysID(),getTargetCompID(),yawAngle, (float)(getGpsStatus().lat / 1e7) , (float)(getGpsStatus().lon / 1e7), altitude);
    }
    
    /**
     * Send a Land Command to the Pixhawk to land at a desired Position
     * @param yawAngle the desired yaw angle in rad
     * @param latitude the desired latitude after takeOff in deg.
     * @param longtitude the desired longtitude after takeoff in deg.
     */
    public void land(float yawAngle, float latitude, float longtitude){
        mavlink.sendLandCommand(getTargetSysID(),getTargetCompID(), yawAngle, latitude, longtitude);
    }
    
        /**
     * Send a Land Command to the Pixhawk to land at the current Position
     * @param yawAngle the desired yaw angle in rad
     */
    public void land(float yawAngle){
        mavlink.sendLandCommand(getTargetSysID(),getTargetCompID(),yawAngle, (float)(getGpsStatus().lat / 1e7) , (float)(getGpsStatus().lon / 1e7));
    }
    
    /**
     * Send a set_mission_count Command to the Pixhawk to set the Size of the Mission
     * This will be used to force the Pixhawk to update the Mission and request für eatch single Item
     * @param count the number of Items in the Mission
     */
    public void setMissionCount(int count){
        mavlink.sendMissionCountCommand(getTargetSysID(),getTargetCompID(), count);
    }
}
;