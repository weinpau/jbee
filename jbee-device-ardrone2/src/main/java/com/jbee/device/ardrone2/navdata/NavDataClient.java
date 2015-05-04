package com.jbee.device.ardrone2.navdata;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author weinpau
 */
public class NavDataClient extends Thread {

    List<Consumer<NavData>> navDataConsumers = new ArrayList<>();

    DatagramChannel channel;
    Selector selector;

    InetAddress inetAddress;
    int port, timeout;

    static final byte[] TRIGGER_BYTES = {0x01, 0x00, 0x00, 0x00};

    byte[] buffer = new byte[8192];

    public NavDataClient(InetAddress inetAddress, int port, int timeout) {
        setName("NavData Client");
        this.inetAddress = inetAddress;
        this.port = port;
        this.timeout = timeout;
    }

    public void connect() throws IOException {
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(port));
        channel.connect(new InetSocketAddress(inetAddress, port));

        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        if (channel.isConnected()) {
            start();
        }
    }

    public void disconnect() throws IOException {
        if (!isInterrupted()) {
            interrupt();
        }
        if (selector.isOpen()) {
            selector.close();
        }
        if (!channel.socket().isClosed()) {
            channel.socket().close();
        }
        if (channel.isConnected()) {
            channel.disconnect();
        }
        channel.close();
    }

    @Override
    public void run() {

        while (true) {

            try {
                int length = readDataBlock(buffer);
                NavData data = NavData.parse(ByteBuffer.wrap(buffer, 0, length));
                newNavDataReceived(data);
            } catch (IOException | NavDataParseException ex) {
                Logger.getLogger(NavDataClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void onNavDataReceived(Consumer<NavData> receiver) {
        navDataConsumers.add(receiver);
    }

    void newNavDataReceived(NavData navdata) {
        navDataConsumers.forEach(c -> c.accept(navdata));
    }

    int readDataBlock(byte[] buffer) throws IOException {
        int length = 0;
        selector.select(timeout);
        Set<SelectionKey> readyKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = readyKeys.iterator();

        while (iterator.hasNext()) {
            SelectionKey key = (SelectionKey) iterator.next();
            iterator.remove();

            if (key.isWritable()) {
                channel.write(ByteBuffer.wrap(TRIGGER_BYTES));
                channel.register(selector, SelectionKey.OP_READ);
            } else if (key.isReadable()) {
                return channel.read(ByteBuffer.wrap(buffer));
            }
        }

        return length;
    }

}
