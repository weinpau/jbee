package com.jbee.device.pixhawk.internal;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.enums.MAV_COMPONENT;
import com.jbee.device.pixhawk.mavlink.MavlinkModule;
import java.util.function.Consumer;


/**
 *
 * @author Erik Jähne
 */
public class PixhawkController extends PixhawkStateListener{
    
    final MavlinkModule mavlink;
    
    /**
     * The Pixhawk Representation to generate Commands and get state information trough the StateListener
     * @param module a Mavlik Module for generating mavlink messages for the commands
     */
    public PixhawkController(MavlinkModule module) {
        mavlink = module;
        mavlink.registerMavlinkPacketReceiver(PixhawkController.class.getName(), this);
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
        mavlink.sendTakeoffCommand(getTargetSysID(),(byte)MAV_COMPONENT.MAV_COMP_ID_ALL, yawAngle, latitude, longtitude, altitude);
    }
    /**
     * Send a TakeOff command to the Pixhawk at the current Position
     * @param yawAngle the desired yaw angle in rad
     * @param altitude the desired altitude in m
     */
    public void takeOff(float yawAngle, float altitude){
        mavlink.sendTakeoffCommand(getTargetSysID(),(byte)MAV_COMPONENT.MAV_COMP_ID_ALL,yawAngle, (float)(getGpsStatus().lat / 1e7) , (float)(getGpsStatus().lon / 1e7), altitude);
    }
    
    /**
     * Send a TakeOff command to the Pixhawk at the current Position and current Yaw
     * @param yawAngle the desired yaw angle in rad
     * @param altitude the desired altitude in m
     */
    public void takeOff(float altitude){
        mavlink.sendTakeoffCommand(getTargetSysID(),(byte)MAV_COMPONENT.MAV_COMP_ID_ALL,getAttitude().yaw, (float)(getGpsStatus().lat / 1e7) , (float)(getGpsStatus().lon / 1e7), altitude);
    }
    
    /**
     * Send a Land Command to the Pixhawk to land at a desired Position
     * @param yawAngle the desired yaw angle in rad
     * @param latitude the desired latitude after takeOff in deg.
     * @param longtitude the desired longtitude after takeoff in deg.
     */
    public void land(float yawAngle, float latitude, float longtitude){
        mavlink.sendLandCommand(getTargetSysID(),(byte)MAV_COMPONENT.MAV_COMP_ID_ALL, yawAngle, latitude, longtitude);
    }
    
        /**
     * Send a Land Command to the Pixhawk to land at the current Position
     * @param yawAngle the desired yaw angle in rad
     */
    public void land(float yawAngle){
        mavlink.sendLandCommand(getTargetSysID(),(byte)MAV_COMPONENT.MAV_COMP_ID_ALL,yawAngle, (float)(getGpsStatus().lat / 1e7) , (float)(getGpsStatus().lon / 1e7));
    }
    
    /**
     * Send a Land Command to the Pixhawk to land at the current Position with current Yaw
     * @param yawAngle the desired yaw angle in rad
     */
    public void land(){
        mavlink.sendLandCommand(getTargetSysID(),(byte)MAV_COMPONENT.MAV_COMP_ID_ALL,getAttitude().yaw, (float)(getGpsStatus().lat / 1e7) , (float)(getGpsStatus().lon / 1e7));
    }
    
    /**
     * Send a set_mission_count Command to the Pixhawk to set the Size of the Mission
     * This will be used to force the Pixhawk to update the Mission and request für eatch single Item
     * @param count the number of Items in the Mission
     */
    public void setMissionCount(int count){
        mavlink.sendMissionCountCommand(getTargetSysID(),(byte)MAV_COMPONENT.MAV_COMP_ID_ALL, count);
    }

        /**
     * Send a mission_clear_all Command to erase all Misssion Items on the Pixhawk
     */
    public void clearMission(){
        mavlink.sendMissionClearCommand(getTargetSysID(), (byte)MAV_COMPONENT.MAV_COMP_ID_ALL);
    }
    
    /**
     * Send a set_mode Command to the Pixhawk to set the flight mode
     * @param base_mode the desired base Mode
     * @param custom_mode the desired custom mode
     */
    public void setMode(int base_mode, int custom_mode) {
        mavlink.setMode(getTargetSysID(), (byte)MAV_COMPONENT.MAV_COMP_ID_ALL, (byte) (base_mode & 0xFF), (byte) (custom_mode & 0xFF));
    }

     /**
     * Sends a Target Position to the Pixhawk
     * @param x The X Coordinate of the Target in m relative to the start Position
     * @param y The Y Coordinate of the Target in m relative to the start Position
     * @param alt The Height of the Target in m relative to the start Position
     * @param velX The Velocity to move to the Target in X Direction
     * @param velY The Velocity to move to the Target in Y Direction
     * @param velZ The Velocity to climp to the Target
     * @param yaw The desired Yaw Angle
     * @param yawRate The desired Yaw Speed in rad/s
     * @param useYaw If true, the Yaw Angle will be set instead of the YawRate. If false the Yaw will be ignored and the YawRate will be set.
     * @param useVelocity If true, the X,Y,Z Corrdinates will not be used and the velocity is used. If false the Velocity will be ignored
     */
    public void setPositionTargetLocal(double x, double y, double alt, double velX, double velY, double velZ, double yaw, double yawRate,boolean useGlobalYaw,boolean useVelocity) {
        mavlink.setPositionTargetLocal(getTargetSysID(), (byte)MAV_COMPONENT.MAV_COMP_ID_ALL, x, y, alt, velX, velY, velZ, yaw, yawRate, useGlobalYaw,useVelocity);
    }

    
    
    
    
}