package com.jbee.units;

import java.time.Duration;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class DistanceTest {

    static final double EPSILON = 0;

    @Test
    public void testToMillimeters() {
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

    @Test
    public void testAbs() {
        assertEquals(1.2, Distance.ofMeters(1.2).abs().toMeters(), EPSILON);
        assertEquals(1.2, Distance.ofMeters(-1.2).abs().toMeters(), EPSILON);
        assertEquals(0, Distance.ZERO.abs().toMeters(), EPSILON);
    }

    @Test
    public void testSub() {
        assertEquals(0, Distance.ofMeters(5).sub(Distance.ofMeters(5)).toMeters(), EPSILON);
        assertEquals(5, Distance.ofMeters(-1).sub(Distance.ofMeters(-6)).toMeters(), EPSILON);
    }

    @Test
    public void testMultiply() {
        assertEquals(8, Distance.ofMeters(4).multiply(2).toMeters(), EPSILON);
        assertEquals(-8, Distance.ofMeters(-4).multiply(2).toMeters(), EPSILON);
        assertEquals(-8, Distance.ofMeters(8).multiply(-1).toMeters(), EPSILON);
        assertEquals(0, Distance.ofMeters(4).multiply(0).toMeters(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiply_invalid() {
        Distance.ofMeters(4).multiply(Double.NaN);
    }

    @Test
    public void testDivide() {
        Distance instance = Distance.ofMeters(10);
        Speed speed = Speed.mps(5);
        assertEquals(Duration.ofSeconds(2), instance.divide(speed));
    }

    @Test
    public void testLessThan() {
        assertTrue(Distance.ZERO.lessThan(Distance.ofMeters(1)));
        assertFalse(Distance.ofMeters(2).lessThan(Distance.ofMeters(1)));
    }

    @Test
    public void testGreaterThan() {
        assertFalse(Distance.ZERO.greaterThan(Distance.ofMeters(1)));
        assertTrue(Distance.ofMeters(2).greaterThan(Distance.ofMeters(1)));
    }

    @Test
    public void testIsZero() {
        assertTrue(Distance.ZERO.isZero());
        assertFalse(Distance.ofMeters(-1).isZero());
        assertFalse(Distance.ofMeters(1).isZero());

    }

    @Test
    public void testIsNegative() {
        assertFalse(Distance.ZERO.isNegative());
        assertTrue(Distance.ofMeters(-1).isNegative());
        assertFalse(Distance.ofMeters(1).isNegative());
    }

    @Test
    public void testEqual() {
        assertTrue(Distance.ofMeters(-1).equal(Distance.ofMeters(-1+1e-50)));
        assertFalse(Distance.ofMeters(-1).equal(Distance.ofMeters(-1.1)));
    }

    @Test
    public void testOfMeters() {
        assertEquals(8, Distance.ofMeters(8).toMeters(), EPSILON);
    }

    @Test
    public void testOfCentimeters() {
        assertEquals(800, Distance.ofMeters(8).toCentimeters(), EPSILON);
    }

    @Test
    public void testOfMillimeters() {
        assertEquals(8000, Distance.ofMeters(8).toMillimeters(), EPSILON);
    }

}
