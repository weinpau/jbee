        // MESSAGE PERFORMANCE_MSG PACKING
package com.MAVLink.quantocopter;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
        //import android.util.Log;
        
        /**
        * This message is used to analyse the Performance of the QuantocopterSystem
        */
        public class msg_performance_msg extends MAVLinkMessage{
        
        public static final int MAVLINK_MSG_ID_PERFORMANCE_MSG = 239;
        public static final int MAVLINK_MSG_LENGTH = 42;
        private static final long serialVersionUID = MAVLINK_MSG_ID_PERFORMANCE_MSG;
        
        
         	/**
        * start time of calculation in ns
        */
        public long nsec;
         	/**
        * start time of calculation in s 
        */
        public int sec;
         	/**
        * array to store timedelays
        */
        public int timedelays[] = new int[7];
         	/**
        * unique id of this message
        */
        public byte id;
         	/**
        * last entry in timedelays
        */
        public byte lastdelay;
        
        
        /**
        * Generates the payload for a mavlink message for a message of this type
        * @return
        */
        public MAVLinkPacket pack(){
		MAVLinkPacket packet = new MAVLinkPacket();
		packet.len = MAVLINK_MSG_LENGTH;
		packet.sysid = 255;
		packet.compid = 190;
		packet.msgid = MAVLINK_MSG_ID_PERFORMANCE_MSG;
        		packet.payload.putLong(nsec);
        		packet.payload.putInt(sec);
        		 for (int i = 0; i < timedelays.length; i++) {
                    packet.payload.putInt(timedelays[i]);
                    }
        		packet.payload.putByte(id);
        		packet.payload.putByte(lastdelay);
        
		return packet;
        }
        
        /**
        * Decode a performance_msg message into this class fields
        *
        * @param payload The message to decode
        */
        public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();
        	    this.nsec = payload.getLong();
        	    this.sec = payload.getInt();
        	     for (int i = 0; i < this.timedelays.length; i++) {
                    this.timedelays[i] = payload.getInt();
                    }
        	    this.id = payload.getByte();
        	    this.lastdelay = payload.getByte();
        
        }
        
        /**
        * Constructor for a new message, just initializes the msgid
        */
        public msg_performance_msg(){
    	msgid = MAVLINK_MSG_ID_PERFORMANCE_MSG;
        }
        
        /**
        * Constructor for a new message, initializes the message with the payload
        * from a mavlink packet
        *
        */
        public msg_performance_msg(MAVLinkPacket mavLinkPacket){
        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.msgid = MAVLINK_MSG_ID_PERFORMANCE_MSG;
        unpack(mavLinkPacket.payload);
        //Log.d("MAVLink", "PERFORMANCE_MSG");
        //Log.d("MAVLINK_MSG_ID_PERFORMANCE_MSG", toString());
        }
        
                  
        /**
        * Returns a string with the MSG name and data
        */
        public String toString(){
    	return "MAVLINK_MSG_ID_PERFORMANCE_MSG -"+" nsec:"+nsec+" sec:"+sec+" timedelays:"+timedelays+" id:"+id+" lastdelay:"+lastdelay+"";
        }
        }
        