package com.jbee.device.pixhawk.connection.network;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erik Jähne
 */
public class NetworkReader extends Thread{
    
    Map<String, Consumer<MAVLinkPacket>> mavlinkConsumers = new HashMap();
    Parser mavParser = new Parser();
    
    DatagramSocket socket;
    InetAddress broadcastAddr;
    DataInputStream reader;
    
    int port;
    boolean done;

    /**
     * The Reader Part of the Network connection
     * @param broadcastAddr The Braoadcast Address to listen to
     * @param port 
     */
    public NetworkReader(InetAddress broadcastAddr, int port) {
        setName("Network Reader");
        this.broadcastAddr = broadcastAddr;
        this.port = port;
    }
    
    /**
     * Connect to the Broadcast Adress
     * @throws IOException 
     */
    void connect() throws IOException{        
        socket = new DatagramSocket(port);
        socket.setReuseAddress(true);
        socket.setBroadcast(true);
        done = false;
        start();
    }
    
    /**
     * Dissconect from the broadcast adress
     * @throws IOException 
     */
    void disconnect() throws IOException {
        if (!done) {
            done = true;
            interrupt();
            
        }

        if (socket == null) {
            throw new IOException("No socket connected");
        }
        if(socket.isConnected())
            socket.disconnect();
        socket.close();
    }
    
    /**
     * Register a MavlinkPacket Consumer class for receiving MavlinkPackages throug the Consumer callback
     * @param id a unique id of the Consumer to identify different Receivers. Its recomendet to use Class name
     * @param receiver The Consumer class for the callback
     */
    public void registerMavlinkPacketReceiver(String id, Consumer<MAVLinkPacket> receiver) {
        mavlinkConsumers.put(id, receiver);
    }

    /**
     * Removes the registerd Mavlink Receiver from the callback list
     * @param id the unique id of the Receiver
     */
    public void removeMavlinkReceiver(String id) {
        mavlinkConsumers.remove(id);
    }

    /**
     * Handle new Mavlink messages and pass them to all registed Consumers
     * @param packet the mavlink Pakcet
     */
    private void newMavlinkPacketReceived(MAVLinkPacket packet) {
        Iterator<Map.Entry<String, Consumer<MAVLinkPacket>>> iterator = mavlinkConsumers.entrySet().iterator();
        while (iterator.hasNext()) {
            try{
                iterator.next().getValue().accept(packet);
            }
            catch(IllegalMonitorStateException e){
                iterator.remove();
            }
            catch(Exception e){
                //skip
            }
        }
    }
    
    @Override
    @SuppressWarnings("UseSpecificCatch")
    public void run() {
        byte[] buffer = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buffer,buffer.length);
        while (!done) {
            try {
                socket.receive(dp);
                MAVLinkPacket packet;
                for (byte c : dp.getData()) {
                    packet = mavParser.mavlink_parse_char(c & 0xFF);
                    if(packet != null){
                        newMavlinkPacketReceived(packet);
                    }  
                }
            } catch (SocketException ex) {
                //ignoing because socket can be closed to return from this thread
            } catch (IOException ex) {
                Logger.getLogger(NetworkReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
