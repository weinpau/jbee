package com.jbee.units;

import java.time.Duration;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class SpeedTest {

    @Test
    public void testMps() {
        Speed instance = Speed.mps(12.5);
        double result = instance.mps();
        assertEquals(12.5, result, 0.0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testMps_invalid() {
        Speed.mps(Double.NaN);
    }

    @Test
    public void testKmph() {
        Speed instance = Speed.kmph(12.5);
        double result = instance.kmph();
        assertEquals(12.5, result, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKmph_invalid() {
        Speed.kmph(Double.NaN);
    }

    @Test
    public void testKn() {
        Speed instance = Speed.kn(12.5);
        double result = instance.kn();
        assertEquals(12.5, result, 0.0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testKn_invalid() {
        Speed.kn(Double.NaN);
    }

    @Test
    public void testMps_To_Kmps() {
        Speed speed = Speed.mps(5);
        assertEquals(18, speed.kmph(), 0.0);

    }

    @Test
    public void testMps_To_Kn() {
        Speed speed = Speed.mps(5);
        assertEquals(9.71922, speed.kn(), 0.00001);

    }

    @Test
    public void testMultiply_double() {
        double factor = 2;
        Speed instance = Speed.mps(2);
        Speed expResult = Speed.mps(4);
        Speed result = instance.multiply(factor);
        assertEquals(expResult, result);

    }

    @Test
    public void testMultiply_double2() {
        double factor = 0;
        Speed instance = Speed.mps(2);
        Speed expResult = Speed.ZERO;
        Speed result = instance.multiply(factor);
        assertEquals(expResult, result);

    }

    @Test
    public void testMultiply_double3() {
        double factor = -1;
        Speed instance = Speed.mps(2);
        Speed expResult = Speed.mps(-2);
        Speed result = instance.multiply(factor);
        assertEquals(expResult, result);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiply_double_invalid() {
        Speed instance = Speed.ZERO;
        instance.multiply(Double.NaN);
    }

    @Test
    public void testMultiply_Speed() {
        Speed speed = Speed.mps(2);
        Speed instance = Speed.mps(-2);
        Speed expResult = Speed.mps(-4);
        Speed result = instance.multiply(speed);
        assertEquals(expResult, result);
    }

    @Test
    public void testMultiply_Speed2() {
        Speed speed = Speed.mps(2);
        Speed instance = Speed.ZERO;
        Speed expResult = Speed.ZERO;
        Speed result = instance.multiply(speed);
        assertEquals(expResult, result);
    }

    @Test
    public void testMultiply_Duration() {
        Duration duration = Duration.ofSeconds(1);
        Speed instance = Speed.mps(10);
        Distance expResult = Distance.ofMeters(10);
        Distance result = instance.multiply(duration);
        assertEquals(expResult, result);
    }

    @Test
    public void testMultiply_Duration2() {
        Duration duration = Duration.ofSeconds(1);
        Speed instance = Speed.ZERO;
        Distance expResult = Distance.ZERO;
        Distance result = instance.multiply(duration);
        assertEquals(expResult, result);
    }

    @Test
    public void testAdd() {
        Speed speed = Speed.mps(2);
        Speed instance = Speed.mps(-2);
        Speed expResult = Speed.ZERO;
        Speed result = instance.add(speed);
        assertEquals(expResult, result);

    }

    @Test
    public void testSub() {
        Speed speed = Speed.mps(2);
        Speed instance = Speed.mps(2);
        Speed expResult = Speed.ZERO;
        Speed result = instance.sub(speed);
        assertEquals(expResult, result);
    }

    @Test
    public void testAbs() {
        Speed speed = Speed.mps(-2);
        Speed expResult = Speed.mps(2);
        Speed result = speed.abs();
        assertEquals(expResult, result);
    }

    @Test
    public void testIsZero() {
        assertTrue(Speed.ZERO.isZero());
        assertTrue(Speed.mps(0).isZero());
        assertFalse(Speed.mps(1).isZero());
    }

    @Test
    public void testIsNegative() {
        assertFalse(Speed.ZERO.isNegative());
        assertFalse(Speed.mps(0).isNegative());
        assertFalse(Speed.mps(1).isNegative());
        assertTrue(Speed.mps(-1).isNegative());
    }

    @Test
    public void testEquals() {
        Speed instance = Speed.mps(1);
        assertTrue(instance.equals(instance));
        assertTrue(instance.equals(Speed.mps(1)));
        assertFalse(instance.equals(Speed.ZERO));
        assertFalse(instance.equals("other"));
    }

}
