        // MESSAGE UNREGISTER_MODULE PACKING
package com.MAVLink.quantocopter;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        //import android.util.Log;
        
        /**
        * This message is used to unregister the Module to Quantocopter
        */
        public class msg_unregister_module extends MAVLinkMessage{
        
        public static final int MAVLINK_MSG_ID_UNREGISTER_MODULE = 236;
        public static final int MAVLINK_MSG_LENGTH = 1;
        private static final long serialVersionUID = MAVLINK_MSG_ID_UNREGISTER_MODULE;
        
        
         	/**
        * set true to unregister
        */
        public byte unregister;
        
        
        /**
        * Generates the payload for a mavlink message for a message of this type
        * @return
        */
        public MAVLinkPacket pack(){
		MAVLinkPacket packet = new MAVLinkPacket();
		packet.len = MAVLINK_MSG_LENGTH;
		packet.sysid = 255;
		packet.compid = 190;
		packet.msgid = MAVLINK_MSG_ID_UNREGISTER_MODULE;
        		packet.payload.putByte(unregister);
        
		return packet;
        }
        
        /**
        * Decode a unregister_module message into this class fields
        *
        * @param payload The message to decode
        */
        public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        	    this.unregister = payload.getByte();
        
        }
        
        /**
        * Constructor for a new message, just initializes the msgid
        */
        public msg_unregister_module(){
    	msgid = MAVLINK_MSG_ID_UNREGISTER_MODULE;
        }
        
        /**
        * Constructor for a new message, initializes the message with the payload
        * from a mavlink packet
        *
        */
        public msg_unregister_module(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_UNREGISTER_MODULE;
        unpack(mavLinkPacket.payload);
        //Log.d("MAVLink", "UNREGISTER_MODULE");
        //Log.d("MAVLINK_MSG_ID_UNREGISTER_MODULE", toString());
        }
        
          
        /**
        * Returns a string with the MSG name and data
        */
        public String toString(){
    	return "MAVLINK_MSG_ID_UNREGISTER_MODULE -"+" unregister:"+unregister+"";
        }
        }
        