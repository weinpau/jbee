package com.jbee.examples;

import com.jbee.Bee;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeContext;
import com.jbee.BeeControl;
import com.jbee.RotationDirection;
import com.jbee.commands.Commands;
import com.jbee.device.simulation.Simulation;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.time.Duration;

/**
 *
 * @author weinpau
 */
public class SimpleFlight {

    public static void main(String[] args) throws BeeBootstrapException {

        Bee bee = BeeContext.of(new Simulation()).bootstrap();

        BeeControl beeControl = bee.control().
                speed(Speed.mps(2)).
                onPositionChanged((c, p) -> {
                    System.out.println("command " + c.getCommandNumber() + ": " + p);
                },
                Distance.ofCentimeters(10));

        beeControl.takeOff();

        beeControl.onPositionChanged((c, p) -> System.out.println(bee.monitor().getLastKnownState().getPrincipalAxes()),
                Distance.ofMeters(1)).execute(Commands.
                        forward(Distance.ofMeters(10)).
                        andRotate(Angle.ofDegrees(90), RotationDirection.CLOCKWISE).
                        with(RotationalSpeed.rpm(1)).
                        build());

        beeControl.rotate(Angle.ofDegrees(90), RotationDirection.COUNTERCLOCKWISE);
        beeControl.forward(Distance.ofMeters(2));
        beeControl.hover(Duration.ofSeconds(5));
        beeControl.land();

        System.out.println("target position: " + bee.monitor().getLastKnownState().getPosition());
        bee.close();

    }
}
