package com.jbee.units;

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

}
