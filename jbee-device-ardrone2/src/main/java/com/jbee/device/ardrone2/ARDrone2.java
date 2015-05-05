package com.jbee.device.ardrone2;

import com.jbee.BatteryState;
import com.jbee.BeeBootstrapException;
import com.jbee.ControlState;
import com.jbee.TargetDevice;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.units.Frequency;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author weinpau
 */
public class ARDrone2 implements TargetDevice {

    Frequency transmissionRate = Frequency.ofHz(15);

    CommandSender commandSender;
    NavDataClient navdataClient;

    volatile ControlState controlState = ControlState.DISCONNECTED;
    volatile BatteryState batteryState = new BatteryState(.99, false);

    int timeout = 1000;
    int navdataPort = 5554;
    int controlPort = 5556;
    String host = "192.168.1.1";

    @Override
    public String getId() {
        return "ar-drone2";
    }

    @Override
    public RunnableFuture<CommandResult> execute(Command command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bootstrap() throws BeeBootstrapException {
        if (controlState != ControlState.DISCONNECTED) {
            throw new RuntimeException("Drone is not disconnected");
        }

        try {
            commandSender = new CommandSender(InetAddress.getByName(host), controlPort);
            commandSender.connect();

            navdataClient = new NavDataClient(InetAddress.getByName(host), navdataPort, timeout);
            navdataClient.connect();

            controlState = ControlState.READY_FOR_TAKE_OFF;

        } catch (IOException ex) {
            Logger.getLogger(ARDrone2.class.getName()).log(Level.SEVERE, null, ex);
            throw new BeeBootstrapException(ex);
        }
    }

    @Override
    public void disconnect() throws IOException {
        if (controlState != ControlState.READY_FOR_TAKE_OFF) {
            throw new RuntimeException("Drone is not connected");
        }
        navdataClient.disconnect();
        commandSender.disconnect();
        controlState = ControlState.DISCONNECTED;
    }

    @Override
    public ControlState getControlState() {
        return controlState;
    }

    @Override
    public BatteryState getBatteryState() {
        return batteryState;
    }

    @Override
    public Frequency getTransmissionRate() {
        return transmissionRate;
    }

}
