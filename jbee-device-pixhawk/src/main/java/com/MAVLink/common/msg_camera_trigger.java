        // MESSAGE CAMERA_TRIGGER PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        //import android.util.Log;
        
        /**
        * Camera-IMU triggering and synchronisation message.
        */
        public class msg_camera_trigger extends MAVLinkMessage{
        
        public static final int MAVLINK_MSG_ID_CAMERA_TRIGGER = 112;
        public static final int MAVLINK_MSG_LENGTH = 12;
        private static final long serialVersionUID = MAVLINK_MSG_ID_CAMERA_TRIGGER;
        
        
         	/**
        * Timestamp for the image frame in microseconds
        */
        public long time_usec;
         	/**
        * Image frame sequence
        */
        public int seq;
        
        
        /**
        * Generates the payload for a mavlink message for a message of this type
        * @return
        */
        public MAVLinkPacket pack(){
		MAVLinkPacket packet = new MAVLinkPacket();
		packet.len = MAVLINK_MSG_LENGTH;
		packet.sysid = 255;
		packet.compid = 190;
		packet.msgid = MAVLINK_MSG_ID_CAMERA_TRIGGER;
        		packet.payload.putLong(time_usec);
        		packet.payload.putInt(seq);
        
		return packet;
        }
        
        /**
        * Decode a camera_trigger message into this class fields
        *
        * @param payload The message to decode
        */
        public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        	    this.time_usec = payload.getLong();
        	    this.seq = payload.getInt();
        
        }
        
        /**
        * Constructor for a new message, just initializes the msgid
        */
        public msg_camera_trigger(){
    	msgid = MAVLINK_MSG_ID_CAMERA_TRIGGER;
        }
        
        /**
        * Constructor for a new message, initializes the message with the payload
        * from a mavlink packet
        *
        */
        public msg_camera_trigger(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_CAMERA_TRIGGER;
        unpack(mavLinkPacket.payload);
        //Log.d("MAVLink", "CAMERA_TRIGGER");
        //Log.d("MAVLINK_MSG_ID_CAMERA_TRIGGER", toString());
        }
        
            
        /**
        * Returns a string with the MSG name and data
        */
        public String toString(){
    	return "MAVLINK_MSG_ID_CAMERA_TRIGGER -"+" time_usec:"+time_usec+" seq:"+seq+"";
        }
        }
        