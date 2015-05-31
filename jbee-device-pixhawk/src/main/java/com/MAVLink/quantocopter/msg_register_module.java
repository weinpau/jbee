        // MESSAGE REGISTER_MODULE PACKING
package com.MAVLink.quantocopter;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        //import android.util.Log;
        
        /**
        * This message is used to register the Module to Quantocopter
        */
        public class msg_register_module extends MAVLinkMessage{
        
        public static final int MAVLINK_MSG_ID_REGISTER_MODULE = 235;
        public static final int MAVLINK_MSG_LENGTH = 66;
        private static final long serialVersionUID = MAVLINK_MSG_ID_REGISTER_MODULE;
        
        
         	/**
        * MAV_ACCESS Values(Can be combined with | operation)
        */
        public short access;
         	/**
        * Name of the Module
        */
        public byte name[] = new byte[64];
        
        
        /**
        * Generates the payload for a mavlink message for a message of this type
        * @return
        */
        public MAVLinkPacket pack(){
		MAVLinkPacket packet = new MAVLinkPacket();
		packet.len = MAVLINK_MSG_LENGTH;
		packet.sysid = 255;
		packet.compid = 190;
		packet.msgid = MAVLINK_MSG_ID_REGISTER_MODULE;
        		packet.payload.putShort(access);
        		 for (int i = 0; i < name.length; i++) {
                    packet.payload.putByte(name[i]);
                    }
        
		return packet;
        }
        
        /**
        * Decode a register_module message into this class fields
        *
        * @param payload The message to decode
        */
        public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        	    this.access = payload.getShort();
        	     for (int i = 0; i < this.name.length; i++) {
                    this.name[i] = payload.getByte();
                    }
        
        }
        
        /**
        * Constructor for a new message, just initializes the msgid
        */
        public msg_register_module(){
    	msgid = MAVLINK_MSG_ID_REGISTER_MODULE;
        }
        
        /**
        * Constructor for a new message, initializes the message with the payload
        * from a mavlink packet
        *
        */
        public msg_register_module(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_REGISTER_MODULE;
        unpack(mavLinkPacket.payload);
        //Log.d("MAVLink", "REGISTER_MODULE");
        //Log.d("MAVLINK_MSG_ID_REGISTER_MODULE", toString());
        }
        
           /**
                        * Sets the buffer of this message with a string, adds the necessary padding
                        */
                        public void setName(String str) {
                        int len = Math.min(str.length(), 64);
                        for (int i=0; i<len; i++) {
                        name[i] = (byte) str.charAt(i);
                        }
                        for (int i=len; i<64; i++) {			// padding for the rest of the buffer
                        name[i] = 0;
                        }
                        }
                        
                        /**
                        * Gets the message, formated as a string
                        */
                        public String getName() {
                        String result = "";
                        for (int i = 0; i < 64; i++) {
                        if (name[i] != 0)
                        result = result + (char) name[i];
                        else
                        break;
                        }
                        return result;
                        
                        } 
        /**
        * Returns a string with the MSG name and data
        */
        public String toString(){
    	return "MAVLINK_MSG_ID_REGISTER_MODULE -"+" access:"+access+" name:"+name+"";
        }
        }
        