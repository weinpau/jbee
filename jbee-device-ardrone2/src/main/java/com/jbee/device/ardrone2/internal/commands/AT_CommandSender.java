package com.jbee.device.ardrone2.internal.commands;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author weinpau
 */
public class AT_CommandSender extends Thread {

    BlockingQueue<AT_Command> commandQueue = new LinkedBlockingDeque<>();

    InetAddress inetAddress;
    int port;
    DatagramSocket socket;

    int sequenceNumber = 1;
    int timeout = 1000;
    boolean done;

    public AT_CommandSender(InetAddress inetAddress, int port) {
        setName("CommandSender");
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public void connect() throws IOException {
        done = false;
        socket = new DatagramSocket();
        start();
    }

    public void disconnect() throws IOException {
        if (!done) {
            done = true;
            interrupt();
        }

        if (socket == null) {
            throw new IOException("No socket connected");
        }

        if (socket.isConnected()) {
            socket.disconnect();
        }
        socket.close();
    }

    public void send(AT_Command command) throws InterruptedException {
        commandQueue.put(command);
    }

    @Override
    public void run() {
        while (!done) {
            try {
                AT_Command command = commandQueue.poll(timeout, TimeUnit.MILLISECONDS);
                if (command == null) {
                    command = new AT_COMWDG();
                }
                byte[] data = command.toBytes(sequenceNumber++);
                DatagramPacket p = new DatagramPacket(data, data.length, inetAddress, port);
                socket.send(p);
            } catch (IOException ex) {
                Logger.getLogger(AT_CommandSender.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
            }

        }

    }

}
