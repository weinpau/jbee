/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.jbee.device.pixhawk.controller;

import com.MAVLink.enums.MAV_MODE_FLAG_DECODE_POSITION;
import com.MAVLink.enums.MAV_QC_ACCESS;
import com.MAVLink.enums.MAV_QC_REGISTER_RESULT;
import com.jbee.BeeBootstrapException;
import com.jbee.device.pixhawk.connection.network.NetworkConnection;
import com.jbee.device.pixhawk.internal.PixhawkController;
import com.jbee.device.pixhawk.mavlink.MavlinkModule;
import java.net.InetAddress;
import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;

/**
 *
 * @author Trader
 */
public class ControllerTestHelper {
    
    protected NetworkConnection connection;
    protected MavlinkModule myModule;
    protected PixhawkController pixhawk;
    protected String name;
    
    static int sysid = 4;

    public ControllerTestHelper(String name){
        this.name = name;
    }
    
    @Before
    public void setup(){
        try {
            //init network
            connection = new NetworkConnection(
                    InetAddress.getByName("192.168.2.26"),
                    InetAddress.getByName("192.168.2.255"),
                    8080, 1000);
            connection.connect();
        } catch (Exception ex) {
            connection.disconnect();
            fail(ex.getMessage());
        }
        
        
        myModule = new MavlinkModule(++sysid, 0, connection);
        pixhawk = new PixhawkController(myModule);  
        
        int result = myModule.registerModule(name,MAV_QC_ACCESS.MAV_QC_ACCESS_ALL);
        if(result == MAV_QC_REGISTER_RESULT.MAV_QC_REGISTER_RESULT_FAILD_EXISTING){
            fail("This Module is already registerd");
        }
        if(result == MAV_QC_REGISTER_RESULT.MAV_QC_REGISTER_RESULT_FAILD_OTHER){
            fail("This Module coud not be registerd");
        }
        
        try {
            pixhawk.waitForConnection();
        } catch (BeeBootstrapException ex) {
            fail(ex.getMessage());
        }
        
        if((pixhawk.getHeartbeat().base_mode & MAV_MODE_FLAG_DECODE_POSITION.MAV_MODE_FLAG_DECODE_POSITION_HIL) == 0){
            fail("Test this only in HIL mode!");
        }
    }
    
    @After
    public void teardown(){
        if(myModule != null)
            myModule.unregisterModule();
        connection.disconnect();
    }
}
