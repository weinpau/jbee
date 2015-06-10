package com.jbee;

import com.jbee.units.Speed;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class GlobalVelocityTest {

    @Test
    public void testGetX() {
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.getX();
        assertEquals(Speed.mps(1), result);
    }

    @Test
    public void testGetY() {
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.getY();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testGetZ() {
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.getZ();
        assertEquals(Speed.mps(3), result);
    }

    @Test
    public void testWithX() {
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.withX(Speed.mps(2)).getX();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testWithY() {
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(5), Speed.mps(3));
        Speed result = instance.withY(Speed.mps(2)).getY();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testWithZ() {
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        Speed result = instance.withZ(Speed.mps(2)).getZ();
        assertEquals(Speed.mps(2), result);
    }

    @Test
    public void testAdd() {
        GlobalVelocity velocity = new GlobalVelocity(Speed.mps(-1), Speed.mps(-2), Speed.mps(-3));
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        GlobalVelocity result = instance.add(velocity);
        assertEquals(GlobalVelocity.ZERO, result);
    }

    @Test
    public void testSub() {
        GlobalVelocity velocity = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        GlobalVelocity result = instance.sub(velocity);
        assertEquals(GlobalVelocity.ZERO, result);
    }

    @Test
    public void testAbs() {
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(-2), Speed.mps(3));
        GlobalVelocity expResult = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        assertEquals(expResult, instance.abs());
    }

    @Test
    public void testMultiply() {

        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        GlobalVelocity expResult = new GlobalVelocity(Speed.mps(2), Speed.mps(4), Speed.mps(6));
        GlobalVelocity result = instance.multiply(2);
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiply_invalid() {
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        instance.multiply(Double.NaN);
    }

    @Test
    public void testTotalSpeed() {
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(0), Speed.mps(3), Speed.mps(4));
        Speed result = instance.totalSpeed();
        assertEquals(Speed.mps(5), result);

    }

    @Test
    public void testIsZero() {
        assertTrue(GlobalVelocity.ZERO.isZero());
        assertFalse(GlobalVelocity.ZERO.withX(Speed.mps(1)).isZero());
    }

    @Test
    public void testEquals() {
        GlobalVelocity velocity = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        GlobalVelocity instance = new GlobalVelocity(Speed.mps(1), Speed.mps(2), Speed.mps(3));
        GlobalVelocity result = instance.add(velocity);
        assertTrue(velocity.equals(instance));
    }

}
