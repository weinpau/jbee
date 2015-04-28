package com.jbee.examples;

import com.jbee.Bee;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeContext;
import com.jbee.BeeControl;
import com.jbee.device.simulation.Simulation;
import com.jbee.units.Distance;
import com.jbee.units.Velocity;
import com.jbee.units.YAW;
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

        beeControl.takeOff();
        beeControl.rotate(YAW.ofDegrees(90));
        beeControl.forward(Distance.ofMeters(2), Velocity.mps(1));        
        beeControl.hover(Duration.ofSeconds(2));
        beeControl.land();
        
       System.out.println("target position: " + bee.monitor().getLastKnownState().getPosition());
        bee.close();

    }
}
