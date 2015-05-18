package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.BeeState;
import com.jbee.ControlState;
import com.jbee.PrincipalAxes;
import com.jbee.RotationDirection;
import com.jbee.Velocity;
import com.jbee.commands.Commands;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class FlyControllerTest {

    @Test
    public void testCalculateDeltaYAW() {

        assertTrue(Angle.ofDegrees(40).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(20)),
                Commands.rotateTo(Angle.ofDegrees(340), RotationDirection.CLOCKWISE).build())));

        assertTrue(Angle.ofDegrees(90).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(180)),
                Commands.rotateTo(Angle.ofDegrees(90), RotationDirection.CLOCKWISE).build())));

        assertTrue(Angle.ofDegrees(-320).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(20)),
                Commands.rotateTo(Angle.ofDegrees(340), RotationDirection.COUNTERCLOCKWISE).build())));

        assertTrue(Angle.ofDegrees(-270).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(180)),
                Commands.rotateTo(Angle.ofDegrees(90), RotationDirection.COUNTERCLOCKWISE).build())));
    }

    @Test
    public void testCalculateDeltaYAW_Zero() {

        assertTrue(Angle.ofDegrees(0).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(0)),
                Commands.rotate(Angle.ofDegrees(0), RotationDirection.CLOCKWISE).build())));

        assertTrue(Angle.ofDegrees(0).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(0)),
                Commands.rotate(Angle.ofDegrees(0), RotationDirection.COUNTERCLOCKWISE).build())));

        assertTrue(Angle.ofDegrees(0).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(20)),
                Commands.rotateTo(Angle.ofDegrees(20), RotationDirection.CLOCKWISE).build())));

        assertTrue(Angle.ofDegrees(0).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(20)),
                Commands.rotateTo(Angle.ofDegrees(20), RotationDirection.COUNTERCLOCKWISE).build())));

    }

    @Test
    public void testCalculateDeltaYAW_Realtive() {

        assertTrue(Angle.ofDegrees(40).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(20)),
                Commands.rotate(Angle.ofDegrees(40), RotationDirection.CLOCKWISE).build())));

        assertTrue(Angle.ofDegrees(180).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(340)),
                Commands.rotate(Angle.ofDegrees(180), RotationDirection.CLOCKWISE).build())));

        assertTrue(Angle.ofDegrees(-40).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(20)),
                Commands.rotate(Angle.ofDegrees(40), RotationDirection.COUNTERCLOCKWISE).build())));

        assertTrue(Angle.ofDegrees(-180).equal(FlyController.calculateDeltaYAW(
                YAWState(Angle.ofDegrees(340)),
                Commands.rotate(Angle.ofDegrees(180), RotationDirection.COUNTERCLOCKWISE).build())));
    }

    BeeState YAWState(Angle yaw) {
        return new BeeState(System.currentTimeMillis(),
                Position.ORIGIN,
                Velocity.ZERO,
                new PrincipalAxes(yaw, Angle.ZERO, Angle.ZERO),
                null, ControlState.FLYING);
    }
}
