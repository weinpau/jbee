package com.jbee;

import com.jbee.units.Speed;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class AxisVelocityTest {

    @Test
    public void testGetX() {
        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.getX();
        assertEquals(Speed.mps(1), result);
    }

    @Test
    public void testGetY() {
        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.getY();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testGetZ() {
        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.getZ();
        assertEquals(Speed.mps(3), result);
    }

    @Test
    public void testWithX() {
        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.withX(Speed.mps(2)).getX();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testWithY() {
        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(5), Speed.mps(3));
        Speed result = instance.withY(Speed.mps(2)).getY();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testWithZ() {
        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.withZ(Speed.mps(2)).getZ();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testAdd() {
        AxisVelocity velocity = new AxisVelocity(Speed.mps(-1), Speed.mps(-2), Speed.mps(-3));
        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        AxisVelocity result = instance.add(velocity);
        assertEquals(AxisVelocity.ZERO, result);
    }

    @Test
    public void testSub() {
        AxisVelocity velocity = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        AxisVelocity result = instance.sub(velocity);
        assertEquals(AxisVelocity.ZERO, result);
    }

    @Test
    public void testAbs() {
        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(-2), Speed.mps(3));
        AxisVelocity expResult = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        assertEquals(expResult, instance.abs());
    }

    @Test
    public void testMultiply() {

        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        AxisVelocity expResult = new AxisVelocity(Speed.mps(2), Speed.mps(4), Speed.mps(6));
        AxisVelocity result = instance.multiply(2);
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiply_invalid() {
        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        instance.multiply(Double.NaN);
    }

    @Test
    public void testTotalSpeed() {
        AxisVelocity instance = new AxisVelocity(Speed.mps(0), Speed.mps(3), Speed.mps(4));
        Speed result = instance.totalSpeed();
        assertEquals(Speed.mps(5), result);

    }

    @Test
    public void testIsZero() {
        assertTrue(AxisVelocity.ZERO.isZero());
        assertFalse(AxisVelocity.ZERO.withX(Speed.mps(1)).isZero());
    }

    @Test
    public void testEquals() {
        AxisVelocity velocity = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        AxisVelocity instance = new AxisVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        AxisVelocity result = instance.add(velocity);
        assertTrue(velocity.equals(instance));
    }

}
