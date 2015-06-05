/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.jbee.device.pixhawk.controller;

import com.MAVLink.enums.MAV_MODE_FLAG_DECODE_POSITION;
import com.jbee.commands.CommandResult;
import com.jbee.commands.HoverCommand;
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
public class HoverControllerTest extends ControllerTestHelper{
    
    public HoverControllerTest() {
        super(HoverControllerTest.class.getSimpleName());
    }
        
    /**
     * Test of execute method, of class HoverController.
     */
    @Test
    public void testExecute() {

        HoverController controller = new HoverController(pixhawk);
        
        assertEquals(CommandResult.COMPLETED, controller.execute(null));
        
    }
    
}
