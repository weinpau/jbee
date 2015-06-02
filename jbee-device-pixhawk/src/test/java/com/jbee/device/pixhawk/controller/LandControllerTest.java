/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.jbee.device.pixhawk.controller;

import com.MAVLink.enums.MAV_MODE_FLAG_DECODE_POSITION;
import com.jbee.commands.CommandResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Trader
 */
public class LandControllerTest extends ControllerTestHelper{
    
    public LandControllerTest() {
        super(LandControllerTest.class.getSimpleName());
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        setup();
    }
    
    @After
    public void tearDown() {
        teardown();
    }

    /**
     * Test of execute method, of class LandController.
     */
    @Test
    public void testExecute() {
        if((pixhawk.getHeartbeat().base_mode & MAV_MODE_FLAG_DECODE_POSITION.MAV_MODE_FLAG_DECODE_POSITION_HIL) == 0){
            fail("Test this only in HIL mode!");
        }
        
        LandController controller = new LandController(pixhawk);
        assertEquals(CommandResult.COMPLETED, controller.execute(null));
        
        assertTrue("Pixhawk is Landed",pixhawk.getGpsStatus().alt < 1000);
    }
    
}
