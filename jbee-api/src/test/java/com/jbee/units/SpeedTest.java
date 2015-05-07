package com.jbee.units;

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

    @Test
    public void testKmph() {
        Speed instance = Speed.kmph(12.5);
        double result = instance.kmph();
        assertEquals(12.5, result, 0.0);
    }

    @Test
    public void testKn() {
        Speed instance = Speed.kn(12.5);
        double result = instance.kn();
        assertEquals(12.5, result, 0.0);

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

}