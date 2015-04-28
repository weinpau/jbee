package com.jbee.examples;

import com.jbee.Bee;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeContext;
import com.jbee.BeeControl;
import com.jbee.device.simulation.Simulation;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import com.jbee.units.Velocity;
import java.time.Duration;

/**
 *
 * @author weinpau
 */
public class SimpleFlight {

    public static void main(String[] args) throws BeeBootstrapException {

        Bee bee = BeeContext.of(new Simulation()).bootstrap();

        BeeControl beeControl = bee.control().
                onPositionChange((c, p) -> {
                    System.out.println("command " + c.getCommandNumber() + ": " + p);

                },
                Distance.ofCentimeters(50));

        beeControl.takeOff(Distance.ofMeters(2));
        beeControl.up(Distance.ofMeters(10), Velocity.kn(5));
        beeControl.flyTo(new Position(10, 10, 10), Velocity.kn(4));
        beeControl.hover(Duration.ofSeconds(2));
        beeControl.land();

        bee.close();

    }
}
