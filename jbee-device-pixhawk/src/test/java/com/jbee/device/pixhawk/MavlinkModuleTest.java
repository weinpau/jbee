package com.jbee.device.pixhawk;

import com.MAVLink.enums.MAV_QC_ACCESS;
import com.MAVLink.enums.MAV_QC_REGISTER_RESULT;
import com.jbee.device.pixhawk.connection.network.NetworkConnection;
import com.jbee.device.pixhawk.mavlink.MavlinkModule;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Erik Jähne
 */
public class MavlinkModuleTest {
    
    MavlinkModule mavModule;
    NetworkConnection connection;
    
    public MavlinkModuleTest() {
        try {
            connection = new NetworkConnection(
                    InetAddress.getByName("192.168.2.26"),
                    InetAddress.getByName("192.168.2.255"),
                    8080, 1000);
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(MavlinkModuleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        mavModule = new MavlinkModule(5, 0,connection );
    }
    
    @BeforeClass
    public static void setUpClass() {

    }
    
    @AfterClass
    public static void tearDownClass() {

    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        
    }

    /**
     * Test of registerModule method, of class MavlinkModule.
     */
    @Test
    public void testRegisterModule() {
        System.out.println("registerModule");
        try {
            connection.connect();
        } catch (IOException ex) {
            Logger.getLogger(MavlinkModuleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        int result = mavModule.registerModule("JBEE Test",MAV_QC_ACCESS.MAV_QC_ACCESS_BASIC);
        assertEquals(MAV_QC_REGISTER_RESULT.MAV_QC_REGISTER_RESULT_REGISTER_OK,result);
        
        System.out.println("unregisterModule");
        result = mavModule.unregisterModule();
        assertEquals(MAV_QC_REGISTER_RESULT.MAV_QC_REGISTER_RESULT_UNREGISTER_OK,result);
        connection.disconnect();
    }

}
