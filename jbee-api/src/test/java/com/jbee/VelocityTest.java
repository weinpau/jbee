package com.jbee;

import com.jbee.units.Speed;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class VelocityTest {

    @Test
    public void testGetX() {
        Velocity instance = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.getX();
        assertEquals(Speed.mps(1), result);
    }

    @Test
    public void testGetY() {
        Velocity instance = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.getY();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testGetZ() {
        Velocity instance = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.getZ();
        assertEquals(Speed.mps(3), result);
    }

    @Test
    public void testWithX() {
        Velocity instance = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.withX(Speed.mps(2)).getX();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testWithY() {
        Velocity instance = new Velocity(Speed.mps(1), Speed.mps(5), Speed.mps(3));
        Speed result = instance.withY(Speed.mps(2)).getY();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testWithZ() {
        Velocity instance = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.withZ(Speed.mps(2)).getZ();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testAdd() {
        Velocity velocity = new Velocity(Speed.mps(-1), Speed.mps(-2), Speed.mps(-3));
        Velocity instance = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Velocity result = instance.add(velocity);
        assertEquals(Velocity.ZERO, result);
    }

    @Test
    public void testSub() {
        Velocity velocity = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Velocity instance = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Velocity result = instance.sub(velocity);
        assertEquals(Velocity.ZERO, result);
    }

    @Test
    public void testMultiply() {

        Velocity instance = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Velocity expResult = new Velocity(Speed.mps(2), Speed.mps(4), Speed.mps(6));
        Velocity result = instance.multiply(2);
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiply_invalid() {
        Velocity instance = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        instance.multiply(Double.NaN);
    }

    @Test
    public void testTotalSpeed() {
        Velocity instance = new Velocity(Speed.mps(0), Speed.mps(3), Speed.mps(4));
        Speed result = instance.totalSpeed();
        assertEquals(Speed.mps(5), result);

    }

    @Test
    public void testIsZero() {
        assertTrue(Velocity.ZERO.isZero());
        assertFalse(Velocity.ZERO.withX(Speed.mps(1)).isZero());
    }

    @Test
    public void testEquals() {
        Velocity velocity = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Velocity instance = new Velocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Velocity result = instance.add(velocity);
        assertTrue(velocity.equals(instance));
    }

}
