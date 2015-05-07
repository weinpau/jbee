package com.jbee.flightpath;

import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class FlightPathTest {

    static final double EPSILON = 0.001;

    @Test
    public void testGetTargetNode() {
        FlightPathNode node = new FlightPathNode(new Position(2, 4, 1), Angle.ofDegrees(10));
        FlightPath instance = FlightPath.of(node);
        assertEquals(node, instance.getTargetNode());

    }

    @Test
    public void testGetTargetNode_chain() {
        FlightPathNode node_1 = new FlightPathNode(new Position(2, 4, 1), Angle.ofDegrees(10));
        FlightPathNode node_2 = new FlightPathNode(new Position(-6, 10, 0), Angle.ofDegrees(-30));
        FlightPathNode expResult = new FlightPathNode(new Position(-4, 14, 1), Angle.ofDegrees(-20));

        FlightPath instance = FlightPath.of(node_1).chain(FlightPath.of(node_2));
        assertEquals(expResult.getDeltaYaw().toRadians(), instance.getTargetNode().getDeltaYaw().toRadians(), EPSILON);
        assertTrue(expResult.getDeltaPosition().nearlyEqual(instance.getTargetNode().getDeltaPosition(), Distance.ofMilimeters(1)));
    }

    @Test
    public void testGetTargetNode_combine() {
        FlightPathNode node_1 = new FlightPathNode(new Position(2, 4, 1), Angle.ofDegrees(10));
        FlightPathNode node_2 = new FlightPathNode(new Position(-6, 10, 0), Angle.ofDegrees(-30));
        FlightPathNode expResult = new FlightPathNode(new Position(-2, 7, .5), Angle.ofDegrees(-10));

        FlightPath instance = FlightPath.of(node_1).combine(FlightPath.of(node_2));
        assertEquals(expResult.getDeltaYaw().toRadians(), instance.getTargetNode().getDeltaYaw().toRadians(), EPSILON);
        assertTrue(expResult.getDeltaPosition().nearlyEqual(instance.getTargetNode().getDeltaPosition(), Distance.ofMilimeters(1)));
    }

    @Test
    public void testGetTargetNode_combine_complex() {
        FlightPathNode node_1 = new FlightPathNode(new Position(2, 4, 1), Angle.ofDegrees(10));
        FlightPathNode node_2 = new FlightPathNode(new Position(-6, 10, 0), Angle.ofDegrees(-30));
        FlightPathNode node_3 = new FlightPathNode(new Position(-1, -5, 10), Angle.ofDegrees(180));
        FlightPathNode expResult = new FlightPathNode(new Position(-2.5, 4.5, 5.5), Angle.ofDegrees(80));

        FlightPath instance = FlightPath.of(node_1).combine(FlightPath.of(node_2).chain(FlightPath.of(node_3)));
        assertEquals(expResult.getDeltaYaw().toRadians(), instance.getTargetNode().getDeltaYaw().toRadians(), EPSILON);
        assertTrue(expResult.getDeltaPosition().nearlyEqual(instance.getTargetNode().getDeltaPosition(), Distance.ofMilimeters(1)));
    }

    @Test
    public void testCombine() {
        Position p_1 = new Position(2, 4, 1);
        FlightPathNode node_1 = new FlightPathNode(p_1, Angle.ofDegrees(0));
        Position p_2 = new Position(4, 8, 2);
        FlightPathNode node_2 = new FlightPathNode(p_2, Angle.ofDegrees(0));
        FlightPathNode node_3 = new FlightPathNode(Position.ORIGIN, Angle.ofDegrees(180));

        FlightPath instance = FlightPath.of(node_1).chain(FlightPath.of(node_2)).combine(FlightPath.of(node_3));

        assertEquals(2, instance.chain.size());

        assertTrue(p_1.nearlyEqual(instance.chain.get(0).getDeltaPosition(), Distance.ofMilimeters(1)));
        assertTrue(p_2.nearlyEqual(instance.chain.get(1).getDeltaPosition(), Distance.ofMilimeters(1)));

        assertEquals(Angle.ofDegrees(60).toRadians(), instance.chain.get(0).getDeltaYaw().toRadians(), EPSILON);
        assertEquals(Angle.ofDegrees(120).toRadians(), instance.chain.get(1).getDeltaYaw().toRadians(), EPSILON);

    }

    @Test
    public void testGetLength() {
        Position p = new Position(2, 4, 1);
        FlightPathNode node = new FlightPathNode(p, Angle.ofDegrees(10));
        FlightPath instance = FlightPath.of(node);
        assertEquals(p.distance(Position.ORIGIN), instance.getLength());
    }

    @Test
    public void testGetLength_chain() {
        Position p_1 = new Position(2, 4, 1);
        FlightPathNode node_1 = new FlightPathNode(p_1, Angle.ofDegrees(10));
        Position p_2 = new Position(-6, 10, 0);
        FlightPathNode node_2 = new FlightPathNode(p_2, Angle.ofDegrees(-30));

        FlightPath instance = FlightPath.of(node_1).chain(FlightPath.of(node_2));
        assertEquals(p_1.distance(Position.ORIGIN).add(p_2.distance(Position.ORIGIN)), instance.getLength());
    }

}
