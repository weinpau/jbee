package com.jbee.device.pixhawk.connection.network;

import com.MAVLink.MAVLinkPacket;
import com.jbee.device.pixhawk.connection.Connection;
import java.io.IOException;
import java.net.InetAddress;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erik Jähne
 */
public class NetworkConnection extends Connection{
        
    NetworkReader reader;
    NetworkWriter writer;
    
    /**
     * Network Implementation of the Conection Class for using TCP and Broadcast Connection
     * @param address The TCP Address fro sending Data to the Pixhawk
     * @param broadcastAdress The UDP Broadcast Adress to receive Data from the Pixhawk
     * @param port The TCP Port where the Mavlink Server is running
     * @param timeout A timeout for opening the conection
     */
    public NetworkConnection(InetAddress address,InetAddress broadcastAdress, int port, int timeout) {
        reader = new NetworkReader(broadcastAdress, port+1);
        writer = new NetworkWriter(address, port);
    }

    @Override
    public void connect() throws IOException{
            reader.connect();
            writer.connect();
    }
    
    @Override
    public void disconnect(){
        try {
            reader.disconnect();
            writer.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(NetworkConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    @Override
    public void registerMavlinkPacketReceiver(String id, Consumer<MAVLinkPacket> receiver) {
        reader.registerMavlinkPacketReceiver(id, receiver);
    }

    @Override
    public void removeMavlinkReceiver(String id) {
        reader.removeMavlinkReceiver(id);
    }

    @Override
    protected void send(MAVLinkPacket packet) {
        writer.send(packet);
    }
    
    
}
