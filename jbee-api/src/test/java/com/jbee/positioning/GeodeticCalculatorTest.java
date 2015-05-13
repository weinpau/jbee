package com.jbee.positioning;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author weinpau
 */
public class GeodeticCalculatorTest {

    @Test
    public void testCalculateGeodeticCurve() {

        Ellipsoid reference = Ellipsoid.WGS84;

        LatLon lincolnMemorial = new LatLon(38.88922, -77.04978);
        LatLon eiffelTower = new LatLon(48.85889, 2.29583);

        GeodeticCurve geoCurve = GeodeticCalculator.calculateGeodeticCurve(reference, lincolnMemorial, eiffelTower);

        assertEquals(6179016.136, geoCurve.getEllipsoidalDistance(), 0.001);
        assertEquals(51.76792142, geoCurve.getAzimuth(), 0.0000001);
        assertEquals(291.75529334, geoCurve.getReverseAzimuth(), 0.0000001);
    }

    @Test
    public void testAntiPodal1() {

        Ellipsoid reference = Ellipsoid.WGS84;

        LatLon p1 = new LatLon(10, 80.6);
        LatLon p2 = new LatLon(-10, -100);

        GeodeticCurve geoCurve = GeodeticCalculator.calculateGeodeticCurve(reference, p1, p2);

        assertEquals(19970718.422432076, geoCurve.getEllipsoidalDistance(), 0.001);
        assertEquals(90.0004877491174, geoCurve.getAzimuth(), 0.0000001);
        assertEquals(270.0004877491174, geoCurve.getReverseAzimuth(), 0.0000001);
    }

    @Test
    public void testAntiPodal2() {
        Ellipsoid reference = Ellipsoid.WGS84;

        LatLon p1 = new LatLon(11, 80);
        LatLon p2 = new LatLon(-10, -100);

        GeodeticCurve geoCurve = GeodeticCalculator.calculateGeodeticCurve(reference, p1, p2);

        assertEquals(19893320.272061437, geoCurve.getEllipsoidalDistance(), 0.001);
        assertEquals(360.0, geoCurve.getAzimuth(), 0.0000001);
        assertEquals(0.0, geoCurve.getReverseAzimuth(), 0.0000001);
    }

}
