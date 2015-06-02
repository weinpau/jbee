/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.jbee.device.pixhawk.controller;

import com.MAVLink.enums.MAV_MODE;
import com.MAVLink.enums.MAV_MODE_FLAG;
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
public class TakeOffControllerTest extends ControllerTestHelper{
    
    public TakeOffControllerTest() {
        super(TakeOffControllerTest.class.getSimpleName());
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
     * Test of execute method, of class TakeOffController.
     */
    @Test
    public void testExecute() {
        
        if((pixhawk.getHeartbeat().base_mode & MAV_MODE_FLAG_DECODE_POSITION.MAV_MODE_FLAG_DECODE_POSITION_HIL) == 0){
            fail("Test this only in HIL mode!");
        }
        
        TakeOffController controller = new TakeOffController(pixhawk);
        assertEquals(CommandResult.COMPLETED, controller.execute(null));
        
        assertTrue("Pixhawk is in Air",pixhawk.getGpsStatus().alt > 1000);
    }

    
}
