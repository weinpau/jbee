package com.jbee.device.ardrone2.internal.commands;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class AT_CommandTest {

    @Test
    public void testEncodeParameter() {

        assertEquals("-1085485875", AT_Command.encodeParameter(-.8f));
        assertEquals("2", AT_Command.encodeParameter(2));
        assertEquals("\"test\"", AT_Command.encodeParameter("test"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeParameter_unsupported() {
        AT_Command.encodeParameter(new Object());
    }

}
