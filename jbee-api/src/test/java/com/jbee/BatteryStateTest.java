package com.jbee;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class BatteryStateTest {

    @Test
    public void testGetLevel() {
        BatteryState instance = new BatteryState(.5, false);
        double result = instance.getLevel();
        assertEquals(.5, result, 0.0);
    }

    @Test
    public void testIsTooLow() {
        BatteryState instance = new BatteryState(.5, true);
        assertTrue(instance.isTooLow());
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void test_invalid() {
        new BatteryState(1.1, true);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void test_invalid2() {
        new BatteryState(-.1, true);
    }

}
