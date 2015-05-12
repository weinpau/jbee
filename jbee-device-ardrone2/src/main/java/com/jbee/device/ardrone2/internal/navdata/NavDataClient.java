package com.jbee.device.ardrone2.internal.navdata;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author weinpau
 */
public class NavDataClient extends Thread {

    static final byte[] TRIGGER_BYTES = {0x01, 0x00, 0x00, 0x00};

    Map<String, Consumer<NavData>> navDataConsumers = new ConcurrentHashMap<>();

    DatagramChannel channel;
    Selector selector;

    InetAddress inetAddress;
    int port, timeout;

    boolean done;

    NavDataParser parser = new NavDataParser();

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
            done = false;
            start();
        }
    }

    public void disconnect() throws IOException {
        if (!done) {
            done = true;
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
    @SuppressWarnings("UseSpecificCatch")
    public void run() {
        while (!done) {

            try {
                int length = readDataBlock(buffer);
                if (length > 0) {
                    NavData data = parser.parse(ByteBuffer.wrap(buffer, 0, length));
                    newNavDataReceived(data);
                }
            } catch (Exception ex) {
                Logger.getLogger(NavDataClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void onNavDataReceived(String id, Consumer<NavData> receiver) {
        navDataConsumers.put(id, receiver);
    }

    public void removeNavDataReceiver(String id) {
        navDataConsumers.remove(id);
    }

    void newNavDataReceived(NavData navdata) {
        navDataConsumers.forEach((k, v) -> v.accept(navdata));
    }

    int readDataBlock(byte[] buffer) throws IOException {
        int length = 0;
        selector.select(timeout);
        Set<SelectionKey> readyKeys = null;
        try {
            readyKeys = selector.selectedKeys();
        } catch (ClosedSelectorException e) {
            return 0;
        }
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
