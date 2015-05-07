package com.jbee.examples;

import com.jbee.Bee;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeContext;
import com.jbee.BeeControl;
import com.jbee.RotationDirection;
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
                defaultSpeed(Speed.mps(2)).
                onCanceled(c -> System.out.println("command " + c.getCommandNumber() + " canceled")).
                onPositionChanged((c, p) -> {
                    System.out.println("command " + c.getCommandNumber() + ": " + p);
                },
                Distance.ofCentimeters(50));

        beeControl.takeOff();
        
        beeControl.up(Distance.ofMeters(5));
        beeControl.rotate(Angle.ofDegrees(90), RotationDirection.CLOCKWISE, RotationalSpeed.rpm(.3));
        beeControl.forward(Distance.ofMeters(2));
        beeControl.onAction(c -> beeControl.cancel(), Duration.ofSeconds(1)).hover(Duration.ofSeconds(10));
        beeControl.land();

        System.out.println("target position: " + bee.monitor().getLastKnownState().getPosition());
        bee.close();

    }
}
