        // MESSAGE REGISTER_ACCESS PACKING
package com.MAVLink.quantocopter;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        //import android.util.Log;
        
        /**
        * This message is used to register the MAV_ACCESS Leves to Quantocopter
        */
        public class msg_register_access extends MAVLinkMessage{
        
        public static final int MAVLINK_MSG_ID_REGISTER_ACCESS = 234;
        public static final int MAVLINK_MSG_LENGTH = 2;
        private static final long serialVersionUID = MAVLINK_MSG_ID_REGISTER_ACCESS;
        
        
         	/**
        * MAV_ACCESS Values(Can be combined with | operation)
        */
        public short access;
        
        
        /**
        * Generates the payload for a mavlink message for a message of this type
        * @return
        */
        public MAVLinkPacket pack(){
		MAVLinkPacket packet = new MAVLinkPacket();
		packet.len = MAVLINK_MSG_LENGTH;
		packet.sysid = 255;
		packet.compid = 190;
		packet.msgid = MAVLINK_MSG_ID_REGISTER_ACCESS;
        		packet.payload.putShort(access);
        
		return packet;
        }
        
        /**
        * Decode a register_access message into this class fields
        *
        * @param payload The message to decode
        */
        public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        	    this.access = payload.getShort();
        
        }
        
        /**
        * Constructor for a new message, just initializes the msgid
        */
        public msg_register_access(){
    	msgid = MAVLINK_MSG_ID_REGISTER_ACCESS;
        }
        
        /**
        * Constructor for a new message, initializes the message with the payload
        * from a mavlink packet
        *
        */
        public msg_register_access(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_REGISTER_ACCESS;
        unpack(mavLinkPacket.payload);
        //Log.d("MAVLink", "REGISTER_ACCESS");
        //Log.d("MAVLINK_MSG_ID_REGISTER_ACCESS", toString());
        }
        
          
        /**
        * Returns a string with the MSG name and data
        */
        public String toString(){
    	return "MAVLINK_MSG_ID_REGISTER_ACCESS -"+" access:"+access+"";
        }
        }
        