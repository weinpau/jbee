package com.jbee.positioning;

import com.jbee.units.Distance;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class CoordinateConverterTest {

    @Test
    public void testUtm2Geo() {
        Position position = new Position(499881, 5666380);
        int zone = 33;
        Hemisphere hemisphere = Hemisphere.NORTH;
        LatLon expResult = new LatLon(51.14887, 14.998299);
        LatLon result = CoordinateConverter.utm2geo(position, Ellipsoid.WGS84, zone, hemisphere);
        assertTrue(expResult.nearlyEqual(result, Distance.ofCentimeters(10)));

    }

    @Test
    public void testGeo2Utm() {
        LatLon coordinate = new LatLon(51.14887, 14.998299);
        Position expResult = new Position(499881, 5666380);
        Position result = CoordinateConverter.geo2utm(coordinate, Ellipsoid.WGS84);
        assertTrue(expResult.nearlyEqual(result, Distance.ofCentimeters(10)));

    }

}
