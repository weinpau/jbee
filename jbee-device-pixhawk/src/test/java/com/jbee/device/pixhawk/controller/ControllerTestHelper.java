/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.jbee.device.pixhawk.controller;

import com.MAVLink.enums.MAV_QC_ACCESS;
import com.MAVLink.enums.MAV_QC_REGISTER_RESULT;
import com.jbee.BeeBootstrapException;
import com.jbee.device.pixhawk.connection.network.NetworkConnection;
import com.jbee.device.pixhawk.internal.CommandDispatcher;
import com.jbee.device.pixhawk.internal.Pixhawk;
import com.jbee.device.pixhawk.mavlink.MavlinkModule;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;

/**
 *
 * @author Trader
 */
public class ControllerTestHelper {
    
    protected NetworkConnection connection;
    protected MavlinkModule myModule;
    protected Pixhawk pixhawk;
    protected String name;
    
    static int sysid = 4;

    public ControllerTestHelper(String name){
        this.name = name;
    }
    
    protected void setup(){
        try {
            //init network
            connection = new NetworkConnection(
                    InetAddress.getByName("192.168.2.26"),
                    InetAddress.getByName("192.168.2.255"),
                    8080, 1000);
            connection.connect();
        } catch (Exception ex) {
            connection.disconnect();
            Assert.fail(ex.getMessage());
        }
        
        
        myModule = new MavlinkModule(++sysid, 0, connection);
        pixhawk = new Pixhawk(myModule);  
        
        int result = myModule.registerModule(name,MAV_QC_ACCESS.MAV_QC_ACCESS_ALL);
        if(result == MAV_QC_REGISTER_RESULT.MAV_QC_REGISTER_RESULT_FAILD_EXISTING){
            Assert.fail("This Module is already registerd");
        }
        if(result == MAV_QC_REGISTER_RESULT.MAV_QC_REGISTER_RESULT_FAILD_OTHER){
            Assert.fail("This Module coud not be registerd");
        }
        
        try {
            pixhawk.waitForConnection();
        } catch (BeeBootstrapException ex) {
            Assert.fail(ex.getMessage());
        }
    }
    
    protected void teardown(){
        if(myModule != null)
            myModule.unregisterModule();
        connection.disconnect();
    }
}
