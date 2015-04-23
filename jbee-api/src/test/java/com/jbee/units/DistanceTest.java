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
        assertEquals(100, Distance.ofCentimeters(10).toMilimeters());
        assertEquals(1000, Distance.ofMeters(1).toMilimeters());
    }

    @Test
    public void testToCentimeters() {
        assertEquals(1, Distance.ofMilimeters(10).toCentimeters(), 0.0);
        assertEquals(10, Distance.ofCentimeters(10).toCentimeters(), 0.0);
        assertEquals(100, Distance.ofMeters(1).toCentimeters(), 0.0);
    }

    @Test
    public void testToMeters() {
        assertEquals(0.01, Distance.ofMilimeters(10).toMeters(), 0.0001);
        assertEquals(0.1, Distance.ofCentimeters(10).toMeters(), 0.0001);
        assertEquals(1, Distance.ofMeters(1).toMeters(), 0.0);
    }

    @Test
    public void testAdd() {
        assertEquals(150, Distance.ofMeters(1).add(Distance.ofCentimeters(50)).toCentimeters(), 0.0);
    }
}
