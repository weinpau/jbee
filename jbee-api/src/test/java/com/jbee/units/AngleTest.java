package com.jbee.units;

import java.time.Duration;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class AngleTest {

    @Test
    public void testTurnRound() {
        assertEquals(180, Angle.ZERO.turnRound().toDegrees(), 0.0);
        assertEquals(270, Angle.ofDegrees(90).turnRound().toDegrees(), 0.0);
        assertEquals(90, Angle.ofDegrees(-90).turnRound().toDegrees(), 0.0);
    }

    @Test
    public void testToRadians() {
        assertEquals(Math.PI, Angle.ofDegrees(180).toRadians(), 0.0);
        assertEquals(-Math.PI, Angle.ofDegrees(-180).toRadians(), 0.0);
    }

    @Test
    public void testToDegrees() {
        assertEquals(180, Angle.ofRadians(Math.PI).toDegrees(), 0.0);
        assertEquals(-180, Angle.ofRadians(-Math.PI).toDegrees(), 0.0);
    }

    @Test
    public void testNormalize() {
        assertTrue(Angle.ofDegrees(0).equal(Angle.ofDegrees(0).normalize()));
        assertTrue(Angle.ofDegrees(90).equal(Angle.ofDegrees(90).normalize()));
        assertTrue(Angle.ofDegrees(-90).equal(Angle.ofDegrees(-90).normalize()));
        assertTrue(Angle.ofDegrees(180).equal(Angle.ofDegrees(180).normalize()));
        assertTrue(Angle.ofDegrees(340).equal(Angle.ofDegrees(340).normalize()));
        assertTrue(Angle.ofDegrees(0).equal(Angle.ofDegrees(360).normalize()));
        assertTrue(Angle.ofDegrees(40).equal(Angle.ofDegrees(400).normalize()));
        assertTrue(Angle.ofDegrees(-40).equal(Angle.ofDegrees(-400).normalize()));
    }

    @Test
    public void testAdd() {
        assertTrue(Angle.ofDegrees(5).add(Angle.ofDegrees(-20)).equal(Angle.ofDegrees(-15)));
        assertTrue(Angle.ZERO.add(Angle.ZERO).equal(Angle.ZERO));
    }

    @Test
    public void testSub() {
        assertTrue(Angle.ofDegrees(5).sub(Angle.ofDegrees(-20)).equal(Angle.ofDegrees(25)));
        assertTrue(Angle.ZERO.sub(Angle.ZERO).equal(Angle.ZERO));
    }

    @Test
    public void testMultiply() {
        assertTrue(Angle.ofDegrees(5).multiply(-1).equal(Angle.ofDegrees(-5)));
       assertTrue(Angle.ofDegrees(5).multiply(0).equal(Angle.ZERO));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiply_invalid() {
        Angle.ofDegrees(5).multiply(Double.NaN);
      }
    
    @Test
    public void testAbs() {
    assertTrue(Angle.ofDegrees(-5).abs().equal(Angle.ofDegrees(5)));
    assertTrue(Angle.ofDegrees(5).abs().equal(Angle.ofDegrees(5)));     
    assertTrue(Angle.ZERO.abs().equal(Angle.ZERO));     
      }

    @Test
    public void testDivide() {
        AngularSpeed speed = AngularSpeed.ofDegreesPerSecond(5);
        Angle instance = Angle.ofDegrees(20);
        Duration expResult = Duration.ofSeconds(4);
        Duration result = instance.divide(speed);
        assertEquals(expResult, result);       
    }

    @Test
    public void testIsZero() {
        assertFalse(Angle.ofDegrees(-10).isZero());
        assertTrue(Angle.ZERO.isZero());
        assertTrue(Angle.ofDegrees(0).isZero());
        assertFalse(Angle.ofDegrees(10).isZero());
    }

    @Test
    public void testIsNegative() {
        assertTrue(Angle.ofDegrees(-10).isNegative());
        assertFalse(Angle.ZERO.isNegative());
        assertFalse(Angle.ofDegrees(10).isNegative());
    }

    @Test
    public void testEqual() {
        assertTrue(Angle.ofDegrees(10).equal(Angle.ofDegrees(10.000000001)));
        assertFalse(Angle.ofDegrees(10).equal(Angle.ofDegrees(10.1)));
    }

    @Test
    public void testOfRadians() {
        Angle angle = Angle.ofRadians(1.0);
        assertEquals(1.0, angle.toRadians(), 0.0001);
    }

    @Test
    public void testOfDegrees() {
        Angle angle = Angle.ofDegrees(5);
        assertEquals(5.0, angle.toDegrees(), 0.0001);
    }

}
