package com.jbee.units;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class YAWTest {

    @Test
    public void testTurnRound() {
        assertEquals(180, YAW.ZERO.turnRound().toDegrees(), 0.0);
        assertEquals(270, YAW.ofDegrees(90).turnRound().toDegrees(), 0.0);
        assertEquals(90, YAW.ofDegrees(-90).turnRound().toDegrees(), 0.0);
    }

    @Test
    public void testToRadians() {
        assertEquals(Math.PI, YAW.ofDegrees(180).toRadians(), 0.0);
        assertEquals(-Math.PI, YAW.ofDegrees(-180).toRadians(), 0.0);
    }

    @Test
    public void testToDegrees() {
        assertEquals(180, YAW.ofRadians(Math.PI).toDegrees(), 0.0);
        assertEquals(-180, YAW.ofRadians(-Math.PI).toDegrees(), 0.0);
    }

}
