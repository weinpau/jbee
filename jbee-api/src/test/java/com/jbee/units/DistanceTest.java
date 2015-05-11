package com.jbee.units;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class DistanceTest {

    static final double EPSILON = 0;

    @Test
    public void testToMilimeters() {
        assertEquals(10, Distance.ofMeters(0.01).toMillimeters(), EPSILON);
        assertEquals(100, Distance.ofMeters(.1).toMillimeters(), EPSILON);
        assertEquals(1000, Distance.ofMeters(1).toMillimeters(), EPSILON);
        assertEquals(-1000, Distance.ofMeters(-1).toMillimeters(), EPSILON);
        assertEquals(0, Distance.ofMeters(0).toMillimeters(), EPSILON);
    }

    @Test
    public void testToCentimeters() {
        assertEquals(1000, Distance.ofMeters(10).toCentimeters(), EPSILON);
        assertEquals(100, Distance.ofMeters(1).toCentimeters(), EPSILON);
        assertEquals(10, Distance.ofMeters(.1).toCentimeters(), EPSILON);
        assertEquals(-10, Distance.ofMeters(-.1).toCentimeters(), EPSILON);
        assertEquals(0, Distance.ofMeters(0).toCentimeters(), EPSILON);
    }

    @Test
    public void testToMeters() {
        assertEquals(10, Distance.ofMeters(10).toMeters(), EPSILON);
        assertEquals(1, Distance.ofMeters(1).toMeters(), EPSILON);
        assertEquals(.1, Distance.ofMeters(.1).toMeters(), EPSILON);
        assertEquals(-.1, Distance.ofMeters(-.1).toMeters(), EPSILON);
        assertEquals(0, Distance.ofMeters(0).toMeters(), EPSILON);
    }

    @Test
    public void testAdd() {
        assertEquals(1.2, Distance.ofMeters(1).add(Distance.ofMeters(.2)).toMeters(), EPSILON);
    }
}
