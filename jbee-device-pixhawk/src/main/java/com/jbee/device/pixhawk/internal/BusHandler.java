/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.jbee.device.pixhawk.internal;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_attitude;
import com.MAVLink.common.msg_global_position_int;
import com.jbee.PrincipalAxes;
import com.jbee.buses.AltitudeBus;
import com.jbee.buses.GlobalVelocityBus;
import com.jbee.buses.LatLonBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.positioning.LatLon;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import java.util.function.Consumer;

/**
 *
 * @author Trader
 */
public class BusHandler implements Consumer<MAVLinkPacket>{
    
    PrincipalAxesBus axeAngleBus = new PrincipalAxesBus();
    LatLonBus posBus = new LatLonBus();
    AltitudeBus altBus = new AltitudeBus();
    GlobalVelocityBus velBus = new GlobalVelocityBus();

    @Override
    public void accept(MAVLinkPacket t) {
        switch(t.msgid){
            case msg_attitude.MAVLINK_MSG_ID_ATTITUDE:{
                msg_attitude msg = new msg_attitude(t);
                axeAngleBus.publish(new PrincipalAxes(Angle.ofRadians(msg.yaw), Angle.ofRadians(msg.roll), Angle.ofRadians(msg.pitch)));
            } break;
            case msg_global_position_int.MAVLINK_MSG_ID_GLOBAL_POSITION_INT:{
                msg_global_position_int msg = new msg_global_position_int(t);
                posBus.publish(new LatLon(msg.lat * 1e-7, msg.lon * 1e-7));
                altBus.publish(Distance.ofMillimeters(msg.relative_alt));
            }
        }
    }

    public PrincipalAxesBus getAxeAngleBus() {
        return axeAngleBus;
    }

    public LatLonBus getPosBus() {
        return posBus;
    }

    public AltitudeBus getAltBus() {
        return altBus;
    }

    public GlobalVelocityBus getVelBus() {
        return velBus;
    }
    
    
    
}
