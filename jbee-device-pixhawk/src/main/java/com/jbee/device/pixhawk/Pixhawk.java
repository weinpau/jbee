package com.jbee.device.pixhawk;

import com.MAVLink.common.msg_heartbeat;
import com.MAVLink.common.msg_sys_status;
import com.MAVLink.enums.MAV_QC_ACCESS;
import com.MAVLink.enums.MAV_QC_REGISTER_RESULT;
import com.MAVLink.enums.MAV_STATE;
import com.jbee.BatteryState;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeModule;
import com.jbee.BusRegistry;
import com.jbee.ControlState;
import com.jbee.TargetDevice;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.device.pixhawk.connection.network.NetworkConnection;
import com.jbee.device.pixhawk.internal.BusHandler;
import com.jbee.device.pixhawk.internal.CommandDispatcher;
import com.jbee.device.pixhawk.internal.PixhawkController;
import com.jbee.device.pixhawk.mavlink.MavlinkModule;
import com.jbee.units.Frequency;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;


/**
 *
 * @author Erik Jähne
 */
public class Pixhawk extends BeeModule implements TargetDevice{

    CommandDispatcher commandDispatcher;
    NetworkConnection connection;
    MavlinkModule myModule;
    PixhawkController pixhawk;

    BusHandler busHandler = new BusHandler();
    
    public Pixhawk() {
        register(busHandler.getAltBus());
        register(busHandler.getAxeAngleBus());
        register(busHandler.getPosBus());
        register(busHandler.getVelBus());
    }
    
    @Override
    public String getId() {
        return "px4-v2";
    }

    @Override
    public RunnableFuture<CommandResult> execute(Command command) {
        return new FutureTask<>(() -> {
            return commandDispatcher.dispatch(command,myModule,connection);
        });
    }

    @Override
    public void bootstrap(BusRegistry busRegistry) throws BeeBootstrapException {
        try {
            //init network
            connection = new NetworkConnection(
                    InetAddress.getByName("192.168.2.26"),
                    InetAddress.getByName("192.168.2.255"),
                    8080, 1000);
            connection.connect();
        } catch (UnknownHostException ex) {
            connection.disconnect();
            throw new BeeBootstrapException(ex.getMessage());
        } catch (IOException ex) {
            connection.disconnect();
            throw new BeeBootstrapException(ex.getMessage());
        }
        
        connection.registerMavlinkPacketReceiver(BusHandler.class.getName(), busHandler);
        
        myModule = new MavlinkModule(5, 0, connection);
        pixhawk = new PixhawkController(myModule);
        
        
        commandDispatcher = new CommandDispatcher(pixhawk);
        
        int result = 0;
        for (int i = 0; i < 5; i++) {
            result = myModule.registerModule("JBee Demo",MAV_QC_ACCESS.MAV_QC_ACCESS_ALL);
            if(result == MAV_QC_REGISTER_RESULT.MAV_QC_REGISTER_RESULT_REGISTER_OK)
                break;
            System.out.println("Failed to register, try again");
            //Tray again
        }
        if(result == MAV_QC_REGISTER_RESULT.MAV_QC_REGISTER_RESULT_FAILD_EXISTING){
            connection.disconnect();
            throw new BeeBootstrapException("This Module is already registerd");
        }
        if(result == MAV_QC_REGISTER_RESULT.MAV_QC_REGISTER_RESULT_FAILD_OTHER){
            connection.disconnect();
            throw new BeeBootstrapException("This Module coud not be registerd");
        }
        
        try{
            pixhawk.waitForConnection();
        }
        catch(BeeBootstrapException e){
            connection.disconnect();
            throw e;
        }
    }

    @Override
    public void disconnect(){
        myModule.unregisterModule();
        connection.disconnect();
    }

    @Override
    public ControlState getControlState() {
        msg_heartbeat heartbeat = pixhawk.getHeartbeat();
        if(heartbeat == null) return ControlState.DISCONNECTED;
        if(heartbeat.system_status <= MAV_STATE.MAV_STATE_CALIBRATING || heartbeat.system_status == MAV_STATE.MAV_STATE_POWEROFF) return ControlState.DISCONNECTED;
        if(pixhawk.performingLand) return ControlState.LANDING;
        if(pixhawk.performingTakeOff) return ControlState.TAKING_OFF;
        switch(heartbeat.system_status){
            case MAV_STATE.MAV_STATE_STANDBY: return ControlState.READY_FOR_TAKE_OFF;
            case MAV_STATE.MAV_STATE_ACTIVE: return ControlState.FLYING;
            case MAV_STATE.MAV_STATE_CRITICAL: return ControlState.CRITICAL;
            case MAV_STATE.MAV_STATE_EMERGENCY: return ControlState.EMERGENCY;
            default: return ControlState.DISCONNECTED;
        }
    }

    @Override
    public BatteryState getBatteryState() {
        msg_sys_status sysStatus = pixhawk.getSysStatus();
        return new BatteryState(sysStatus.battery_remaining/100.0,sysStatus.battery_remaining < 20 ? true : false);
    }

    @Override
    public Frequency getTransmissionRate() {
        return Frequency.ofHz(1);
    }

    @Override
    public Speed getMaxSpeed() {
        return Speed.mps(pixhawk.getParam("MPC_XY_VEL_MAX"));
    }

    @Override
    public Speed getDefaultSpeed() {
        return Speed.mps(5.0);
    }

    @Override
    public RotationalSpeed getMaxRotationalSpeed() {
        return RotationalSpeed.rps(pixhawk.getParam("MPP_MAN_Y_MAX"));
    }

    @Override
    public RotationalSpeed getDefaultRotationalSpeed() {
        return RotationalSpeed.rpm(6.0);
    }
    
    
}
