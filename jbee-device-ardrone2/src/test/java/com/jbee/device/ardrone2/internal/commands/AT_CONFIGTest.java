package com.jbee.device.ardrone2.internal.commands;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class AT_CONFIGTest {

    @Test
    public void testParseConfigValue() {

        assertEquals("12", AT_CONFIG.parseConfigValue(12));
        assertEquals("test", AT_CONFIG.parseConfigValue("test"));
        assertEquals("TRUE", AT_CONFIG.parseConfigValue(true));
        assertEquals("FALSE", AT_CONFIG.parseConfigValue(false));
        assertEquals("", AT_CONFIG.parseConfigValue(null));

    }

}
