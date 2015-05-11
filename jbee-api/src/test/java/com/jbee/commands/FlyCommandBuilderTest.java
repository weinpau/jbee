package com.jbee.commands;

import com.jbee.RotationDirection;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class FlyCommandBuilderTest {

    public FlyCommandBuilderTest() {
    }

    @Test
    public void testRotate() {
        FlyCommandBuilder instance = new FlyCommandBuilder();

        FlyCommand flyCommand = instance.
                rotate(Angle.ofRadians(1), RotationDirection.COUNTERCLOCKWISE).build();

        assertTrue(flyCommand.isRealtiveRotation());
        assertEquals(Angle.ofRadians(1), flyCommand.getAngle());
        assertEquals(RotationDirection.COUNTERCLOCKWISE, flyCommand.getRotationDirection());
    }

    @Test
    public void testRotate_WithSpeed() {
        FlyCommandBuilder instance = new FlyCommandBuilder();

        FlyCommand flyCommand = instance.
                rotate(Angle.ofRadians(1), RotationDirection.COUNTERCLOCKWISE).
                with(RotationalSpeed.rps(1)).
                build();

        assertTrue(flyCommand.isRealtiveRotation());
        assertEquals(Angle.ofRadians(1), flyCommand.getAngle());
        assertEquals(RotationDirection.COUNTERCLOCKWISE, flyCommand.getRotationDirection());
        assertEquals(RotationalSpeed.rps(1), flyCommand.getRotationalSpeed());
    }

    @Test
    public void testRotateTo() {

        FlyCommandBuilder instance = new FlyCommandBuilder();

        FlyCommand flyCommand = instance.
                rotateTo(Angle.ofRadians(1), RotationDirection.COUNTERCLOCKWISE).
                build();

        assertFalse(flyCommand.isRealtiveRotation());
        assertEquals(Angle.ofRadians(1), flyCommand.getAngle());
        assertEquals(RotationDirection.COUNTERCLOCKWISE, flyCommand.getRotationDirection());

    }

    @Test
    public void testRotateTo_WithSpeed() {

        FlyCommandBuilder instance = new FlyCommandBuilder();

        FlyCommand flyCommand = instance.
                rotateTo(Angle.ofRadians(1), RotationDirection.COUNTERCLOCKWISE).
                with(RotationalSpeed.rps(1)).
                build();

        assertFalse(flyCommand.isRealtiveRotation());
        assertEquals(Angle.ofRadians(1), flyCommand.getAngle());
        assertEquals(RotationDirection.COUNTERCLOCKWISE, flyCommand.getRotationDirection());
        assertEquals(RotationalSpeed.rps(1), flyCommand.getRotationalSpeed());
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
    }

    @Test
    public void testFlyTo_WithRotation() {

        FlyCommandBuilder instance = new FlyCommandBuilder();
        Position position = new Position(1, 2, 3);

        FlyCommand flyCommand = instance.
                flyTo(position).
                andRotate(Angle.ofRadians(1), RotationDirection.COUNTERCLOCKWISE).
                build();

        assertFalse(flyCommand.isRealtivePosition());
        assertEquals(position, flyCommand.getPosition());
        assertNull(flyCommand.getSpeed());
        assertEquals(Angle.ofRadians(1), flyCommand.getAngle());
        assertEquals(RotationDirection.COUNTERCLOCKWISE, flyCommand.getRotationDirection());

    }

    @Test
    public void testFly() {

        FlyCommandBuilder instance = new FlyCommandBuilder();

        FlyCommand flyCommand = instance.
                forward(Distance.ofMeters(10)).
                right(Distance.ofMeters(5)).
                build();

        assertTrue(flyCommand.isRealtivePosition());
        assertEquals(new Position(5, 10, 0), flyCommand.getPosition());
        assertNull(flyCommand.getSpeed());
        assertEquals(Angle.ZERO, flyCommand.getAngle());

    }

}
