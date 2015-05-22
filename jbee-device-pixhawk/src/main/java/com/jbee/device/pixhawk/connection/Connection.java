package com.jbee.device.pixhawk.connection;

import com.MAVLink.MAVLinkPacket;
import java.io.IOException;
import java.util.function.Consumer;

/**
 *
 * @author Erik Jähne
 */
public abstract class Connection {

    private int sequenceNumber = 0;
    
    /**
     * Connects the conection
     * @throws IOException if the conection coud not opend
     */
    public abstract void connect() throws IOException;
    /**
     * Disconnects the conection
     */
    public abstract void disconnect();
    /**
     * Register a MavlinkPacket Consumer class for receiving MavlinkPackages throug the Consumer callback
     * @param id a unique id of the Consumer to identify different Receivers. Its recomendet to use Class name
     * @param receiver The Consumer class for the callback
     */
    public abstract void registerMavlinkPacketReceiver(String id, Consumer<MAVLinkPacket> receiver);
    /**
     * Removes the registerd Mavlink Receiver from the callback list
     * @param id the unique id of the Receiver
     */
    public abstract void removeMavlinkReceiver(String id);
    /**
     * Sends a raw Mavlink Package without updating the sequence Number
     * @param packet the Packet to be sended
     */
    protected abstract void send(MAVLinkPacket packet);
    
    /**
     * Generate and return the nex Sequence Number between 0 an 255
     * @return 
     */
    private synchronized int getNextSeqNumber(){
        sequenceNumber++;
        return sequenceNumber %= 0xFF;
    }
    
    /**
     * Sends a Mavlink Package trough the connection with updated Sequence Number
     * @param packet The Packet to be sended
     */
    public void sendPackage(MAVLinkPacket packet){
        packet.seq = getNextSeqNumber();
        send(packet);
    }
}
