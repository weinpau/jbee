package com.jbee.device.pixhawk.internal;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_attitude;
import com.MAVLink.common.msg_battery_status;
import com.MAVLink.common.msg_gps_raw_int;
import com.MAVLink.common.msg_heartbeat;
import com.MAVLink.common.msg_highres_imu;
import com.MAVLink.common.msg_local_position_ned;
import com.MAVLink.common.msg_position_target_global_int;
import com.MAVLink.common.msg_sys_status;
import com.MAVLink.enums.MAV_AUTOPILOT;
import com.jbee.BeeBootstrapException;
import com.jbee.device.pixhawk.mavlink.MavlinkModule;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erik Jähne
 */
public class PixhawkStateListener implements Consumer<MAVLinkPacket>{
   
    public boolean performingLand = false;
    public boolean performingTakeOff = false;
    private boolean isConnected = false;
    private Timer watchdogTimer;    
    private msg_heartbeat heartbeat;
    private msg_battery_status battery;
    private msg_sys_status sysStatus;
    private msg_gps_raw_int gpsStatus;
    private msg_attitude attitude;
    private msg_local_position_ned localPosition;
    private msg_highres_imu imu;
    private msg_position_target_global_int targetPos;
        
    private final Object connectionMutex = new Object();
    private TimerTask timoutTask = new TimerTask() {

        @Override
        public void run() {
            isConnected = false;
            connectionMutex.notifyAll();
            //TODO connection lost
        }
    };

    @Override
    public void accept(MAVLinkPacket t) {
        if(heartbeat != null && heartbeat.sysid != t.sysid) return;
        switch(t.msgid){
            case msg_heartbeat.MAVLINK_MSG_ID_HEARTBEAT:{
                if(heartbeat == null){
                    msg_heartbeat temp = new msg_heartbeat(t);
                    if(temp.autopilot == MAV_AUTOPILOT.MAV_AUTOPILOT_PX4){
                        heartbeat = new msg_heartbeat(t);
                        isConnected = true;
                        connectionMutex.notifyAll();
                    }
                }else if(t.sysid == heartbeat.sysid){
                    heartbeat.unpack(t.payload);
                    if(isConnected == false){
                        connectionMutex.notifyAll();
                        //TODO reconnected
                    }
                    watchdogTimer.cancel();
                    watchdogTimer.schedule(timoutTask, 1100);
                }
            }break;
            case msg_battery_status.MAVLINK_MSG_ID_BATTERY_STATUS:{
                if(battery == null)
                    battery = new msg_battery_status(t);
                else battery.unpack(t.payload);
            }break;
            case msg_sys_status.MAVLINK_MSG_ID_SYS_STATUS:{
                if(sysStatus == null)
                    sysStatus = new msg_sys_status(t);
                else sysStatus.unpack(t.payload);
            }break;
            case msg_gps_raw_int.MAVLINK_MSG_ID_GPS_RAW_INT:{
                if(gpsStatus == null)
                    gpsStatus = new msg_gps_raw_int(t);
                else gpsStatus.unpack(t.payload);
            }break;
            case msg_attitude.MAVLINK_MSG_ID_ATTITUDE:{
                if(attitude == null)
                    attitude = new msg_attitude(t);
                else attitude.unpack(t.payload);
            }break;
            case msg_local_position_ned.MAVLINK_MSG_ID_LOCAL_POSITION_NED:{
                if(localPosition == null)
                    localPosition = new msg_local_position_ned(t);
                else localPosition.unpack(t.payload);
            }break;
            case msg_highres_imu.MAVLINK_MSG_ID_HIGHRES_IMU:{
                if(imu == null)
                    imu = new msg_highres_imu(t);
                else imu.unpack(t.payload);
            }break;
            case msg_position_target_global_int.MAVLINK_MSG_ID_POSITION_TARGET_GLOBAL_INT:{
                if(targetPos == null)
                    targetPos = new msg_position_target_global_int(t);
                else targetPos.unpack(t.payload);
            }break;
        }
    }

    /**
     * Get the system ID of the connected Pixhawk
     * @return the system id
     */
    public byte getTargetSysID(){
        if(heartbeat != null)
            return (byte) heartbeat.sysid;
        else return 0;
    }
    /**
     * get the component id of the coneccted pixhawk
     * @return 
     */
    public byte getTargetCompID(){
        if(heartbeat != null)
            return (byte)heartbeat.compid;
        else return 0;
    }
    /**
     * Get the connection state
     * @return true if we have a connection
     */
    public boolean isConnected() {
        return isConnected;
    }
    /**
     * get the raw GPS Data
     * @return the raw gps data expressed as deg * 1e7
     */
    public msg_gps_raw_int getGpsStatus() {
        return gpsStatus;
    }
    /**
     * get the current battery Status
     * @return the battery status
     */
    public msg_battery_status getBattery() {
        return battery;
    }
    /**
     * get the current System Status
     * @return the system status
     */
    public msg_sys_status getSysStatus() {
        return sysStatus;
    }
    /**
     * get the current attitude like yaw/pitch/roll
     * @return the attitude data
     */
    public msg_attitude getAttitude() {
        return attitude;
    }
    /**
     * get local position in NED frame
     * @return the local position
     */
    public msg_local_position_ned getLocalPosition() {
        return localPosition;
    }
    /**
     * get the raw IMU data like gyro/acc/mag
     * @return the imu data
     */
    public msg_highres_imu getImu() {
        return imu;
    }
    /**
     * get the current target Position where the Pixhawk will fly to
     * @return the target position
     */
    public msg_position_target_global_int getTargetPos() {
        return targetPos;
    }
    
    /**
     * get the current heartbeat message
     * @return the heartbeat message
     */
    public msg_heartbeat getHeartbeat() {
        return heartbeat;
    }
    
    /**
     * Wait for the first Heartbeat Packet to check if the pixhawk ist connected
     * @throws BeeBootstrapException 
     */
    public void waitForConnection() throws BeeBootstrapException{
        if(isConnected) return;
        watchdogTimer.schedule(timoutTask, 2000);
        synchronized(connectionMutex){
            try {
                connectionMutex.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(MavlinkModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(!isConnected){
            throw new BeeBootstrapException("System not found or not connected. Please Check connection and Power State of the System!");
        }else watchdogTimer.cancel();
    }

}
