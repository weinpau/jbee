package com.jbee.positioning;

import com.jbee.units.Distance;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class LatLonTest {

    @Test
    public void testGetLatitude() {
        LatLon instance = new LatLon(12, 50);
        double expResult = 12;
        double result = instance.getLatitude();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testGetLongitude() {
        LatLon instance = new LatLon(12, 50);
        double expResult = 50;
        double result = instance.getLongitude();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testDistance_WGS84() {
        LatLon a = new LatLon(37.316725, -81.916734);
        LatLon b = new LatLon(-22.811413, 45.671930);

        Distance result = a.distance(b, Ellipsoid.WGS84);
        assertEquals(14793106.008, result.toMeters(), 0.001);

    }

    @Test
    public void testEquals() {

        LatLon instance = new LatLon(12, 47);

        assertTrue(instance.equals(new LatLon(12, 47)));
        assertFalse(instance.equals(new LatLon(10, 47)));

    }

    @Test
    public void testGetHemisphere() {

        assertEquals(Hemisphere.SOUTH, new LatLon(-10, -10).getHemisphere());
        assertEquals(Hemisphere.SOUTH, new LatLon(-10, 10).getHemisphere());
        assertEquals(Hemisphere.NORTH, new LatLon(10, -10).getHemisphere());
        assertEquals(Hemisphere.NORTH, new LatLon(10, 10).getHemisphere());

    }

    @Test
    public void testGetUTMZone() {
        assertEquals(33, new LatLon(50, 15).getUTMZone());

    }

    @Test
    public void testToPosition() {
        LatLon origin = new LatLon(50, 16);
        LatLon instance = new LatLon(50, 15);
        assertTrue(instance.toPosition(origin).toLatLon(origin).nearlyEqual(instance, Distance.ofCentimeters(1)));
    }

    @Test
    public void testNearlyEqual() {
        LatLon instance = new LatLon(50, 16);
        assertTrue(instance.nearlyEqual(new LatLon(50.000001, 16.000001), Distance.ofMeters(1)));
        assertFalse(instance.nearlyEqual(new LatLon(50.1, 16.1), Distance.ofMeters(1)));
    }

}
