        // MESSAGE PERFORMANCE_MSG_NAMES PACKING
package com.MAVLink.quantocopter;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        //import android.util.Log;
        
        /**
        * This message is used to analyse the Performance of the QuantocopterSystem
        */
        public class msg_performance_msg_names extends MAVLinkMessage{
        
        public static final int MAVLINK_MSG_ID_PERFORMANCE_MSG_NAMES = 240;
        public static final int MAVLINK_MSG_LENGTH = 255;
        private static final long serialVersionUID = MAVLINK_MSG_ID_PERFORMANCE_MSG_NAMES;
        
        
         	/**
        * last entry in names
        */
        public byte offset;
         	/**
        * array to store names
        */
        public byte names[] = new byte[254];
        
        
        /**
        * Generates the payload for a mavlink message for a message of this type
        * @return
        */
        public MAVLinkPacket pack(){
		MAVLinkPacket packet = new MAVLinkPacket();
		packet.len = MAVLINK_MSG_LENGTH;
		packet.sysid = 255;
		packet.compid = 190;
		packet.msgid = MAVLINK_MSG_ID_PERFORMANCE_MSG_NAMES;
        		packet.payload.putByte(offset);
        		 for (int i = 0; i < names.length; i++) {
                    packet.payload.putByte(names[i]);
                    }
        
		return packet;
        }
        
        /**
        * Decode a performance_msg_names message into this class fields
        *
        * @param payload The message to decode
        */
        public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        	    this.offset = payload.getByte();
        	     for (int i = 0; i < this.names.length; i++) {
                    this.names[i] = payload.getByte();
                    }
        
        }
        
        /**
        * Constructor for a new message, just initializes the msgid
        */
        public msg_performance_msg_names(){
    	msgid = MAVLINK_MSG_ID_PERFORMANCE_MSG_NAMES;
        }
        
        /**
        * Constructor for a new message, initializes the message with the payload
        * from a mavlink packet
        *
        */
        public msg_performance_msg_names(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_PERFORMANCE_MSG_NAMES;
        unpack(mavLinkPacket.payload);
        //Log.d("MAVLink", "PERFORMANCE_MSG_NAMES");
        //Log.d("MAVLINK_MSG_ID_PERFORMANCE_MSG_NAMES", toString());
        }
        
           /**
                        * Sets the buffer of this message with a string, adds the necessary padding
                        */
                        public void setNames(String str) {
                        int len = Math.min(str.length(), 254);
                        for (int i=0; i<len; i++) {
                        names[i] = (byte) str.charAt(i);
                        }
                        for (int i=len; i<254; i++) {			// padding for the rest of the buffer
                        names[i] = 0;
                        }
                        }
                        
                        /**
                        * Gets the message, formated as a string
                        */
                        public String getNames() {
                        String result = "";
                        for (int i = 0; i < 254; i++) {
                        if (names[i] != 0)
                        result = result + (char) names[i];
                        else
                        break;
                        }
                        return result;
                        
                        } 
        /**
        * Returns a string with the MSG name and data
        */
        public String toString(){
    	return "MAVLINK_MSG_ID_PERFORMANCE_MSG_NAMES -"+" offset:"+offset+" names:"+names+"";
        }
        }
        