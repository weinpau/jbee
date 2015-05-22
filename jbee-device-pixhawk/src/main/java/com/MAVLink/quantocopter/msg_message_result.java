        // MESSAGE MESSAGE_RESULT PACKING
package com.MAVLink.quantocopter;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        //import android.util.Log;
        
        /**
        * This message is used to give Feadback to failed Messages
        */
        public class msg_message_result extends MAVLinkMessage{
        
        public static final int MAVLINK_MSG_ID_MESSAGE_RESULT = 238;
        public static final int MAVLINK_MSG_LENGTH = 65;
        private static final long serialVersionUID = MAVLINK_MSG_ID_MESSAGE_RESULT;
        
        
         	/**
        * MAV_QC_MESSAGE_RESULT of Message
        */
        public byte MAV_QC_MESSAGE_RESULT;
         	/**
        * Failure detail
        */
        public byte message[] = new byte[64];
        
        
        /**
        * Generates the payload for a mavlink message for a message of this type
        * @return
        */
        public MAVLinkPacket pack(){
		MAVLinkPacket packet = new MAVLinkPacket();
		packet.len = MAVLINK_MSG_LENGTH;
		packet.sysid = 255;
		packet.compid = 190;
		packet.msgid = MAVLINK_MSG_ID_MESSAGE_RESULT;
        		packet.payload.putByte(MAV_QC_MESSAGE_RESULT);
        		 for (int i = 0; i < message.length; i++) {
                    packet.payload.putByte(message[i]);
                    }
        
		return packet;
        }
        
        /**
        * Decode a message_result message into this class fields
        *
        * @param payload The message to decode
        */
        public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        	    this.MAV_QC_MESSAGE_RESULT = payload.getByte();
        	     for (int i = 0; i < this.message.length; i++) {
                    this.message[i] = payload.getByte();
                    }
        
        }
        
        /**
        * Constructor for a new message, just initializes the msgid
        */
        public msg_message_result(){
    	msgid = MAVLINK_MSG_ID_MESSAGE_RESULT;
        }
        
        /**
        * Constructor for a new message, initializes the message with the payload
        * from a mavlink packet
        *
        */
        public msg_message_result(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_MESSAGE_RESULT;
        unpack(mavLinkPacket.payload);
        //Log.d("MAVLink", "MESSAGE_RESULT");
        //Log.d("MAVLINK_MSG_ID_MESSAGE_RESULT", toString());
        }
        
           /**
                        * Sets the buffer of this message with a string, adds the necessary padding
                        */
                        public void setMessage(String str) {
                        int len = Math.min(str.length(), 64);
                        for (int i=0; i<len; i++) {
                        message[i] = (byte) str.charAt(i);
                        }
                        for (int i=len; i<64; i++) {			// padding for the rest of the buffer
                        message[i] = 0;
                        }
                        }
                        
                        /**
                        * Gets the message, formated as a string
                        */
                        public String getMessage() {
                        String result = "";
                        for (int i = 0; i < 64; i++) {
                        if (message[i] != 0)
                        result = result + (char) message[i];
                        else
                        break;
                        }
                        return result;
                        
                        } 
        /**
        * Returns a string with the MSG name and data
        */
        public String toString(){
    	return "MAVLINK_MSG_ID_MESSAGE_RESULT -"+" MAV_QC_MESSAGE_RESULT:"+MAV_QC_MESSAGE_RESULT+" message:"+message+"";
        }
        }
        