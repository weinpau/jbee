/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.jbee.device.pixhawk.controller;

import com.jbee.commands.CancelCommand;
import com.jbee.commands.CommandResult;
import com.jbee.commands.Commands;
import com.jbee.units.Distance;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CancelControllerTest extends ControllerTestHelper{
    
    public CancelControllerTest() {
        super("CancelControllerTest");
    }
    
    static long start;
    static long end;
    
    @Test
    public void testExecute() {

        
        FlyController fly = new FlyController(pixhawk);

        if(pixhawk.getLocalPosition().z > -5);
            fly.execute(Commands.up(Distance.ofMeters(5)).build());
        
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {}
            CancelController cancel = new CancelController(pixhawk);
            start = System.currentTimeMillis();
            cancel.execute((CancelCommand) Commands.cancel());
        });
        
        t.start();
        final CommandResult result = fly.execute(Commands.forward(Distance.ofMeters(200)).build());
        end = System.currentTimeMillis();
        
        assertEquals(CommandResult.CANCELLED, result);
        assertEquals("Cancle time laps", 0, end - start, 100);
        
    }

}
