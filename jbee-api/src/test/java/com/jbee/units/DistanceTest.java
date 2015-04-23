package com.jbee.units;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class DistanceTest {

    @Test
    public void testToMilimeters() {
        assertEquals(10, Distance.ofMilimeters(10).toMilimeters());
        assertEquals(104, Distance.ofCentimeters(10.4).toMilimeters());
        assertEquals(1387, Distance.ofMeters(1.387).toMilimeters());
    }

    @Test
    public void testToCentimeters() {
        assertEquals(1, Distance.ofMilimeters(10).toCentimeters(), 0.0);
        assertEquals(10, Distance.ofCentimeters(10).toCentimeters(), 0.0);
        assertEquals(100, Distance.ofMeters(1).toCentimeters(), 0.0);
    }

    @Test
    public void testToMeters() {
        assertEquals(0.01, Distance.ofMilimeters(10).toMeters(), 0.0);
        assertEquals(0.1, Distance.ofCentimeters(10).toMeters(), 0.0);
        assertEquals(1, Distance.ofMeters(1).toMeters(), 0.0);
    }

}
