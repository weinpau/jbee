package com.jbee.units;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class VelocityTest {

    @Test
    public void testMps() {
        Velocity instance = Velocity.mps(12.5);
        double result = instance.mps();
        assertEquals(12.5, result, 0.0);

    }

    @Test
    public void testKmph() {
        Velocity instance = Velocity.kmph(12.5);
        double result = instance.kmph();
        assertEquals(12.5, result, 0.0);
    }

    @Test
    public void testKn() {
        Velocity instance = Velocity.kn(12.5);
        double result = instance.kn();
        assertEquals(12.5, result, 0.0);

    }

    @Test
    public void testMps_To_Kmps() {        
        Velocity velocity = Velocity.mps(5);
        assertEquals(18, velocity.kmph(), 0.0);

    }

    @Test
    public void testMps_To_Kn() {
        Velocity velocity = Velocity.mps(5);
        assertEquals(9.71922, velocity.kn(), 0.00001);

    }

}
