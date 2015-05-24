        // MESSAGE REGISTER_RESULT PACKING
package com.MAVLink.quantocopter;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        //import android.util.Log;
        
        /**
        * This message is used to give Feadback to the registration from Quantocopter
        */
        public class msg_register_result extends MAVLinkMessage{
        
        public static final int MAVLINK_MSG_ID_REGISTER_RESULT = 237;
        public static final int MAVLINK_MSG_LENGTH = 3;
        private static final long serialVersionUID = MAVLINK_MSG_ID_REGISTER_RESULT;
        
        
         	/**
        * System ID
        */
        public byte target_system;
         	/**
        * Component ID
        */
        public byte target_component;
         	/**
        * MAV_QC_REGISTER_RESULT of registration
        */
        public byte RESULT;
        
        
        /**
        * Generates the payload for a mavlink message for a message of this type
        * @return
        */
        public MAVLinkPacket pack(){
		MAVLinkPacket packet = new MAVLinkPacket();
		packet.len = MAVLINK_MSG_LENGTH;
		packet.sysid = 255;
		packet.compid = 190;
		packet.msgid = MAVLINK_MSG_ID_REGISTER_RESULT;
        		packet.payload.putByte(target_system);
        		packet.payload.putByte(target_component);
        		packet.payload.putByte(RESULT);
        
		return packet;
        }
        
        /**
        * Decode a register_result message into this class fields
        *
        * @param payload The message to decode
        */
        public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        	    this.target_system = payload.getByte();
        	    this.target_component = payload.getByte();
        	    this.RESULT = payload.getByte();
        
        }
        
        /**
        * Constructor for a new message, just initializes the msgid
        */
        public msg_register_result(){
    	msgid = MAVLINK_MSG_ID_REGISTER_RESULT;
        }
        
        /**
        * Constructor for a new message, initializes the message with the payload
        * from a mavlink packet
        *
        */
        public msg_register_result(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_REGISTER_RESULT;
        unpack(mavLinkPacket.payload);
        //Log.d("MAVLink", "REGISTER_RESULT");
        //Log.d("MAVLINK_MSG_ID_REGISTER_RESULT", toString());
        }
        
              
        /**
        * Returns a string with the MSG name and data
        */
        public String toString(){
    	return "MAVLINK_MSG_ID_REGISTER_RESULT -"+" target_system:"+target_system+" target_component:"+target_component+" RESULT:"+RESULT+"";
        }
        }
        