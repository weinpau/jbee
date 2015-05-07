package com.jbee.flightpath;

import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class FlightPathCalculatorTest {

    static final double EPSILON = 0.001;

    @Test
    public void testCalculateNode() {

        List<FlightPathNode> chain = Arrays.asList(
                new FlightPathNode(new Position(10, 0, 0), Angle.ZERO));

        Speed speed = Speed.mps(1);
        RotationalSpeed rotationalSpeed = RotationalSpeed.rpm(1);

        FlightPathCalculator instance = new FlightPathCalculator(chain, speed, rotationalSpeed);

        FlightPathNode pm_0 = instance.calculateNode(0);
        assertTrue(Position.ORIGIN.nearlyEqual(pm_0.getDeltaPosition(), Distance.ofMilimeters(1)));
        assertEquals(Angle.ofRadians(0).toRadians(), pm_0.getDeltaYaw().toRadians(), EPSILON);

        FlightPathNode pm_1 = instance.calculateNode(1000);
        assertTrue(new Position(1, 0, 0).nearlyEqual(pm_1.getDeltaPosition(), Distance.ofMilimeters(1)));
        assertEquals(Angle.ofRadians(0).toRadians(), pm_1.getDeltaYaw().toRadians(), EPSILON);

        FlightPathNode pm_2 = instance.calculateNode(10_000);
        assertTrue(new Position(10, 0, 0).nearlyEqual(pm_2.getDeltaPosition(), Distance.ofMilimeters(1)));
        assertEquals(Angle.ofRadians(0).toRadians(), pm_2.getDeltaYaw().toRadians(), EPSILON);
    }

    @Test
    public void testCalculateNode_2() {

        FlightPath path = FlightPath.of(new Position(6, 0, 0), Angle.ZERO).
                combine(FlightPath.of(Position.ORIGIN, Angle.ofDegrees(90)));

        Speed speed = Speed.mps(1);
        RotationalSpeed rotationalSpeed = RotationalSpeed.rpm(360);

        FlightPathCalculator instance = new FlightPathCalculator(path.chain, speed, rotationalSpeed);

        FlightPathNode pm_0 = instance.calculateNode(0);
        assertTrue(Position.ORIGIN.nearlyEqual(pm_0.getDeltaPosition(), Distance.ofMilimeters(1)));
        assertEquals(Angle.ofRadians(0).toRadians(), pm_0.getDeltaYaw().toRadians(), EPSILON);

        FlightPathNode pm_1 = instance.calculateNode(1000);
        assertTrue(new Position(1, 0, 0).nearlyEqual(pm_1.getDeltaPosition(), Distance.ofMilimeters(1)));
        assertEquals(Angle.ofDegrees(15).toRadians(), pm_1.getDeltaYaw().toRadians(), EPSILON);

        FlightPathNode pm_2 = instance.calculateNode(2000);
        assertTrue(new Position(2, 0, 0).nearlyEqual(pm_2.getDeltaPosition(), Distance.ofMilimeters(1)));
        assertEquals(Angle.ofDegrees(30).toRadians(), pm_2.getDeltaYaw().toRadians(), EPSILON);
        
        FlightPathNode pm_3 = instance.calculateNode(6000);
        assertTrue(new Position(6, 0, 0).nearlyEqual(pm_3.getDeltaPosition(), Distance.ofMilimeters(1)));
        assertEquals(Angle.ofDegrees(90).toRadians(), pm_3.getDeltaYaw().toRadians(), EPSILON);
    }

}
