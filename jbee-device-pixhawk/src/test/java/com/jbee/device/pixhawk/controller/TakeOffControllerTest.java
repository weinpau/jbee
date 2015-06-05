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
public class TakeOffControllerTest extends ControllerTestHelper{
    
    public TakeOffControllerTest() {
        super(TakeOffControllerTest.class.getSimpleName());
    }
    
    /**
     * Test of execute method, of class TakeOffController.
     */
    @Ignore
    @Test
    public void testExecute() {
        
        TakeOffController controller = new TakeOffController(pixhawk);
        assertEquals(CommandResult.COMPLETED, controller.execute(null));
        
        assertTrue("Pixhawk is in Air",pixhawk.getGpsStatus().alt > 1000);
    }

    
}
