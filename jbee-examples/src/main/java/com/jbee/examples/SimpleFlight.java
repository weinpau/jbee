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
                onCanceled(c -> System.out.println("command " + c.getCommandNumber() + " canceled")).
                onPositionChanged((c, p) -> {
                    System.out.println("command " + c.getCommandNumber() + ": " + p);
                },
                Distance.ofCentimeters(10));

        beeControl.takeOff();

        beeControl.execute(Commands.
                forward(Distance.ofMeters(5)).
                andRotate(Angle.ofDegrees(90), RotationDirection.CLOCKWISE).
                build());
        beeControl.forward(Distance.ofMeters(5));
      
        beeControl.rotate(Angle.ofDegrees(90), RotationDirection.COUNTERCLOCKWISE);
        beeControl.forward(Distance.ofMeters(2));
        beeControl.onAction(c -> beeControl.cancel(), Duration.ofSeconds(1)).hover(Duration.ofSeconds(10));
        beeControl.land();

        System.out.println("target position: " + bee.monitor().getLastKnownState().getPosition());
        bee.close();

    }
}
