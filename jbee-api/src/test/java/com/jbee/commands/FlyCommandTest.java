package com.jbee.commands;

import com.jbee.RotationDirection;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.time.Duration;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class FlyCommandTest {

    @Test
    public void testCalculateDeltaYAW() {

        assertTrue(Angle.ofDegrees(40).equal(
                Commands.rotateTo(Angle.ofDegrees(340), RotationDirection.CLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(20))));

        assertTrue(Angle.ofDegrees(90).equal(
                Commands.rotateTo(Angle.ofDegrees(90), RotationDirection.CLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(180))));

        assertTrue(Angle.ofDegrees(-320).equal(
                Commands.rotateTo(Angle.ofDegrees(340), RotationDirection.COUNTERCLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(20))));

        assertTrue(Angle.ofDegrees(-270).equal(
                Commands.rotateTo(Angle.ofDegrees(90), RotationDirection.COUNTERCLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(180))));
    }

    @Test
    public void testCalculateDeltaYAW_Zero() {

        assertTrue(Angle.ofDegrees(0).equal(
                Commands.rotate(Angle.ZERO, RotationDirection.CLOCKWISE).build().calculateDeltaYAW(Angle.ZERO)));

        assertTrue(Angle.ofDegrees(0).equal(
                Commands.rotate(Angle.ZERO, RotationDirection.COUNTERCLOCKWISE).build().
                calculateDeltaYAW(Angle.ZERO)));

        assertTrue(Angle.ofDegrees(0).equal(
                Commands.rotateTo(Angle.ofDegrees(20), RotationDirection.CLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(20))));

        assertTrue(Angle.ofDegrees(0).equal(
                Commands.rotateTo(Angle.ofDegrees(20), RotationDirection.COUNTERCLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(20))));

    }

    @Test
    public void testCalculateDeltaYAW_Realtive() {

        assertTrue(Angle.ofDegrees(40).equal(
                Commands.rotate(Angle.ofDegrees(40), RotationDirection.CLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(20))));

        assertTrue(Angle.ofDegrees(180).equal(
                Commands.rotate(Angle.ofDegrees(180), RotationDirection.CLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(340))));

        assertTrue(Angle.ofDegrees(350).equal(
                Commands.rotate(Angle.ofDegrees(350), RotationDirection.CLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(20))));

        assertTrue(Angle.ofDegrees(-40).equal(
                Commands.rotate(Angle.ofDegrees(40), RotationDirection.COUNTERCLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(20))));

        assertTrue(Angle.ofDegrees(-180).equal(
                Commands.rotate(Angle.ofDegrees(180), RotationDirection.COUNTERCLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(340))));

        assertTrue(Angle.ofDegrees(-350).equal(
                Commands.rotate(Angle.ofDegrees(350), RotationDirection.COUNTERCLOCKWISE).build().
                calculateDeltaYAW(Angle.ofDegrees(20))));

    }

    @Test
    public void testCalculateTargetPosition() {

        Position p = Commands.flyTo(new Position(10, 10)).build().calculateTargetPosition(Position.ORIGIN, Angle.ZERO);
        assertTrue(p.nearlyEqual(new Position(10, 10), Distance.ofMillimeters(1)));
    }

    @Test
    public void testCalculateTargetPosition_Relative() {

        Position p = Commands.forward(Distance.ofMeters(10)).build().calculateTargetPosition(Position.ORIGIN, Angle.ZERO);
        assertTrue(p.nearlyEqual(new Position(0, 10), Distance.ofMillimeters(1)));

        p = Commands.forward(Distance.ofMeters(10)).build().calculateTargetPosition(p, Angle.ofDegrees(90));
        assertTrue(p.nearlyEqual(new Position(-10, 10), Distance.ofMillimeters(1)));
    }

    @Test
    public void testCalculateFlyDuration() {

        FlyCommand command = Commands.
                forward(Distance.ofMeters(10)).
                with(Speed.mps(2)).
                andRotate(Angle.ofDegrees(90), RotationDirection.CLOCKWISE).
                with(RotationalSpeed.rpm(1)).build();

        Duration duration = command.calculateFlyDuration(Position.ORIGIN);

        assertEquals(Duration.ofSeconds(5), duration);
    }

    @Test
    public void testCalculateRotationDuration() {

        FlyCommand command = Commands.
                forward(Distance.ofMeters(10)).
                with(Speed.mps(2)).
                andRotate(Angle.ofDegrees(90), RotationDirection.CLOCKWISE).
                with(RotationalSpeed.rpm(1)).build();

        Duration duration = command.calculateRotationDuration(Angle.ZERO);

        assertEquals(Duration.ofSeconds(15), duration);
    }

    @Test
    public void testCalculateDuration() {

        FlyCommand command = Commands.
                forward(Distance.ofMeters(10)).
                with(Speed.mps(2)).
                andRotate(Angle.ofDegrees(90), RotationDirection.CLOCKWISE).
                with(RotationalSpeed.rpm(1)).build();

        Duration duration = command.calculateDuration(Position.ORIGIN, Angle.ZERO);

        assertEquals(Duration.ofSeconds(15), duration);
    }

    @Test
    public void testCalculateDuration_2() {

        FlyCommand command = Commands.
                forward(Distance.ofMeters(10)).
                with(Speed.mps(2)).
                andRotate(Angle.ofDegrees(90), RotationDirection.CLOCKWISE).
                with(RotationalSpeed.rps(10)).build();

        Duration duration = command.calculateDuration(Position.ORIGIN, Angle.ZERO);

        assertEquals(Duration.ofSeconds(5), duration);
    }

}
