package com.jbee.commands;

import com.jbee.RotationDirection;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
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

}
