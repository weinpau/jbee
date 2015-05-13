package com.jbee.positioning;

import com.jbee.units.Distance;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class PositionTest {

    @Test
    public void testGetX() {
        Position instance = new Position(12, 32);
        double expResult = 12;
        double result = instance.getX();
        assertEquals(expResult, result, 0.0);

    }

    @Test
    public void testGetY() {
        Position instance = new Position(12, 32);
        double expResult = 32;
        double result = instance.getY();
        assertEquals(expResult, result, 0.0);

    }

    @Test
    public void testGetZ() {
        Position instance = new Position(12, 32, 9);
        double expResult = 9;
        double result = instance.getZ();
        assertEquals(expResult, result, 0.0);

    }

    @Test
    public void testWithX() {

        double x = 20;
        Position instance = new Position(12, 32, 9);
        Position expResult = new Position(x, 32, 9);
        Position result = instance.withX(x);
        assertEquals(expResult, result);

    }

    @Test
    public void testWithY() {
        double y = 20;
        Position instance = new Position(12, 32, 9);
        Position expResult = new Position(12, y, 9);
        Position result = instance.withY(y);
        assertEquals(expResult, result);
    }

    @Test
    public void testWithZ() {
        double z = 20;
        Position instance = new Position(12, 32, 9);
        Position expResult = new Position(12, 32, z);
        Position result = instance.withZ(z);
        assertEquals(expResult, result);
    }

    @Test
    public void testAddX() {
        double x = 8;
        Position instance = new Position(12, 32, 9);
        Position expResult = new Position(20, 32, 9);
        Position result = instance.addX(x);
        assertEquals(expResult, result);

    }

    @Test
    public void testAddY() {
        double y = 8;
        Position instance = new Position(12, 32, 9);
        Position expResult = new Position(12, 40, 9);
        Position result = instance.addY(y);
        assertEquals(expResult, result);
    }

    @Test
    public void testAddZ() {
        double z = 1;
        Position instance = new Position(12, 32, 9);
        Position expResult = new Position(12, 32, 10);
        Position result = instance.addZ(z);
        assertEquals(expResult, result);
    }

    @Test
    public void testAbs() {
        Position instance = new Position(12, -32, -9);
        Position expResult = new Position(12, 32, 9);
        Position result = instance.abs();
        assertEquals(expResult, result);

    }

    @Test
    public void testAdd() {
        Position position = new Position(-12, -32, -9);
        Position instance = new Position(12, 32, 9);
        Position expResult = Position.ORIGIN;
        Position result = instance.add(position);
        assertEquals(expResult, result);

    }

    @Test
    public void testSub() {
        Position position = new Position(12, 32, 9);
        Position instance = new Position(12, 32, 9);
        Position expResult = Position.ORIGIN;
        Position result = instance.sub(position);
        assertEquals(expResult, result);
    }

    @Test
    public void testMultiply() {
        Position instance = new Position(12, 32, 9);
        Position expResult = Position.ORIGIN;
        Position result = instance.multiply(0);
        assertEquals(expResult, result);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiply_Invalid() {
        Position instance = new Position(12, 32, 9);
        instance.multiply(Double.NaN);
    }

    @Test
    public void testNormalize() {
        Position instance = new Position(12, 32, 9);
        Position result = instance.normalize();
        assertEquals(1, result.distance(Position.ORIGIN).toMeters(), 0.00001);
    }

    @Test
    public void testNormalize_Zero() {
        Position result = Position.ORIGIN.normalize();
        assertEquals(0, result.distance(Position.ORIGIN).toMeters(), 0.00001);

    }

    @Test
    public void testNearlyEqual() {

        Position position = new Position(12, 32, 9);

        Position instance = new Position(12.1, 32.4, 8.9);

        assertTrue(instance.nearlyEqual(position, Distance.ofMeters(1)));
        assertFalse(instance.nearlyEqual(position, Distance.ofCentimeters(1)));

    }

    @Test
    public void testIsOrigin() {
        assertTrue(Position.ORIGIN.isOrigin());
        assertFalse(new Position(1, 0).isOrigin());
    }

    @Test
    public void testDistance() {

        assertEquals(0, Position.ORIGIN.distance(Position.ORIGIN).toMeters(), 0.00001);
        assertEquals(1, Position.ORIGIN.distance(new Position(1, 0)).toMeters(), 0.00001);
        assertEquals(Math.sqrt(2), Position.ORIGIN.distance(new Position(-1, -1)).toMeters(), 0.00001);

    }

    @Test
    public void testEquals() {

        Position instance = new Position(1, 2);

        assertTrue(instance.equals(new Position(1, 2)));
        assertTrue(instance.equals(new Position(1, 2, 0)));
        assertFalse(instance.equals("other"));
        assertFalse(instance.equals(new Position(1, 2, 3)));

    }

}
