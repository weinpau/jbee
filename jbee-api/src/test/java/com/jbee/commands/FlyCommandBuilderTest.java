package com.jbee.commands;

import com.jbee.RotationDirection;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author weinpau
 */
public class FlyCommandBuilderTest {

    @Test
    public void testRotate() {
        FlyCommandBuilder instance = new FlyCommandBuilder();

        FlyCommand flyCommand = instance.
                rotate(Angle.ofRadians(1), RotationDirection.CCW).build();

        assertTrue(flyCommand.isRealtiveRotation());
        assertEquals(Angle.ofRadians(1), flyCommand.getAngle());
        assertEquals(RotationDirection.CCW, flyCommand.getRotationDirection());
        assertEquals(FlyCommand.Mode.ROTATE, flyCommand.getMode());
    }

    @Test
    public void testRotate_WithSpeed() {
        FlyCommandBuilder instance = new FlyCommandBuilder();

        FlyCommand flyCommand = instance.
                rotate(Angle.ofRadians(1), RotationDirection.CCW).
                with(RotationalSpeed.rps(1)).
                build();

        assertTrue(flyCommand.isRealtiveRotation());
        assertEquals(Angle.ofRadians(1), flyCommand.getAngle());
        assertEquals(RotationDirection.CCW, flyCommand.getRotationDirection());
        assertEquals(RotationalSpeed.rps(1), flyCommand.getRotationalSpeed());
        assertEquals(FlyCommand.Mode.ROTATE, flyCommand.getMode());
    }

    @Test
    public void testRotateTo() {

        FlyCommandBuilder instance = new FlyCommandBuilder();

        FlyCommand flyCommand = instance.
                rotateTo(Angle.ofRadians(1), RotationDirection.CCW).
                build();

        assertFalse(flyCommand.isRealtiveRotation());
        assertEquals(Angle.ofRadians(1), flyCommand.getAngle());
        assertEquals(RotationDirection.CCW, flyCommand.getRotationDirection());
        assertEquals(FlyCommand.Mode.ROTATE, flyCommand.getMode());

    }

    @Test
    public void testRotateTo_WithSpeed() {

        FlyCommandBuilder instance = new FlyCommandBuilder();

        FlyCommand flyCommand = instance.
                rotateTo(Angle.ofRadians(1), RotationDirection.CCW).
                with(RotationalSpeed.rps(1)).
                build();

        assertFalse(flyCommand.isRealtiveRotation());
        assertEquals(Angle.ofRadians(1), flyCommand.getAngle());
        assertEquals(RotationDirection.CCW, flyCommand.getRotationDirection());
        assertEquals(RotationalSpeed.rps(1), flyCommand.getRotationalSpeed());
        assertEquals(FlyCommand.Mode.ROTATE, flyCommand.getMode());
    }

    @Test
    public void testFlyTo() {

        FlyCommandBuilder instance = new FlyCommandBuilder();
        Position position = new Position(1, 2, 3);

        FlyCommand flyCommand = instance.flyTo(position).build();

        assertFalse(flyCommand.isRealtivePosition());
        assertEquals(position, flyCommand.getPosition());
        assertNull(flyCommand.getSpeed());
        assertEquals(Angle.ZERO, flyCommand.getAngle());
        assertEquals(FlyCommand.Mode.FLY, flyCommand.getMode());

    }

    @Test
    public void testFlyTo_WithSpeed() {

        FlyCommandBuilder instance = new FlyCommandBuilder();
        Position position = new Position(1, 2, 3);
        Speed speed = Speed.mps(1);

        FlyCommand flyCommand = instance.flyTo(position).with(speed).build();

        assertFalse(flyCommand.isRealtivePosition());
        assertEquals(position, flyCommand.getPosition());
        assertEquals(speed, flyCommand.getSpeed());
        assertEquals(Angle.ZERO, flyCommand.getAngle());
        assertEquals(FlyCommand.Mode.FLY, flyCommand.getMode());
    }

    @Test
    public void testFlyTo_WithRotation() {

        FlyCommandBuilder instance = new FlyCommandBuilder();
        Position position = new Position(1, 2, 3);

        FlyCommand flyCommand = instance.
                flyTo(position).
                andRotate(Angle.ofRadians(1), RotationDirection.CCW).
                build();

        assertFalse(flyCommand.isRealtivePosition());
        assertEquals(position, flyCommand.getPosition());
        assertNull(flyCommand.getSpeed());
        assertEquals(Angle.ofRadians(1), flyCommand.getAngle());
        assertEquals(RotationDirection.CCW, flyCommand.getRotationDirection());
        assertEquals(FlyCommand.Mode.FLY_AND_ROTATE, flyCommand.getMode());

    }

    @Test
    public void testFlyRelative() {

        FlyCommandBuilder instance = new FlyCommandBuilder();

        FlyCommand flyCommand = instance.
                fly(Distance.ofMeters(5), Distance.ofMeters(10), Distance.ZERO).
                build();

        assertTrue(flyCommand.isRealtivePosition());
        assertEquals(new Position(5, 10, 0), flyCommand.getPosition());
        assertNull(flyCommand.getSpeed());
        assertEquals(Angle.ZERO, flyCommand.getAngle());
        assertEquals(FlyCommand.Mode.FLY, flyCommand.getMode());

    }

    @Test
    public void testRotateAndFly() {

        FlyCommandBuilder instance = new FlyCommandBuilder();

        FlyCommand flyCommand = instance.
                rotate(Angle.ofDegrees(40), RotationDirection.CW).
                andFly(Distance.ofMeters(10), Distance.ofMeters(-5), Distance.ofMeters(5)).
                build();

        assertTrue(flyCommand.isRealtivePosition());
        assertEquals(new Position(10, -5, 5), flyCommand.getPosition());
        assertEquals(FlyCommand.Mode.ROTATE_AND_FLY, flyCommand.getMode());

    }

}
