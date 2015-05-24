package com.jbee.device.pixhawk.connection.network;

import com.MAVLink.MAVLinkPacket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erik Jähne
 */
public class NetworkWriter extends Thread{
    
    BlockingQueue<MAVLinkPacket> mavlinkQueue = new LinkedBlockingDeque<>();

    InetAddress inetAddress;
    int port;
    Socket socket;
    DataOutputStream writer;

    int sequenceNumber = 1;
    boolean done;
    private final long timeout = 1000;

    /**
     * The Write rPart of the Network Connection
     * @param inetAddress the Adress to conect to
     * @param port the port to connect to
     */
    public NetworkWriter(InetAddress inetAddress, int port) {
        setName("Network Writer");
        this.inetAddress = inetAddress;
        this.port = port;
    }
    
    /**
     * Connects the Connection
     * @throws IOException 
     */
    public void connect() throws IOException {
        socket = new Socket();
        done = false;
        socket.bind(new InetSocketAddress(port));
        socket.setReuseAddress(true);
        socket.setSendBufferSize(1);
        socket.setTcpNoDelay(true);
        socket.connect(new InetSocketAddress(inetAddress, port));
        writer = new DataOutputStream(socket.getOutputStream());
        start(); 
        
    }
    
    /**
     * dissconects the Connection
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
        socket.close();
    }
    
    /**
     * Send a mavlink Package trough the connection
     * @param packet the mavlink packet to be sended
     */
    public void send(MAVLinkPacket packet) {
        try {
            mavlinkQueue.put(packet);
        } catch (InterruptedException ex) {
            Logger.getLogger(NetworkWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (!done) {
            try {
                MAVLinkPacket packet = mavlinkQueue.poll(timeout, TimeUnit.MILLISECONDS);
                if (packet == null) {
                    continue;
                }
                writer.write(packet.encodePacket());
                writer.flush();
            } catch (IOException ex) {
                Logger.getLogger(NetworkWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
            }

        }

    }
}
