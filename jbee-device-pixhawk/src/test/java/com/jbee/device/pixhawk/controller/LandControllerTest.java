/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.jbee.device.pixhawk.controller;

import com.jbee.commands.CommandResult;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Trader
 */
public class LandControllerTest extends ControllerTestHelper{
    
    public LandControllerTest() {
        super(LandControllerTest.class.getSimpleName());
    }
    
    @Ignore
    @Test
    public void testExecute() {

        LandController controller = new LandController(pixhawk);
        assertEquals(CommandResult.COMPLETED, controller.execute(null));
        
        assertTrue("Pixhawk is Landed",pixhawk.getGpsStatus().alt < 1000);
    }
    
}
