package com.jbee.examples;

import com.jbee.Bee;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeContext;
import com.jbee.BeeControl;
import com.jbee.device.ardrone2.ARDrone2;
import com.jbee.device.ardrone2.LEDAnimation;
import com.jbee.device.ardrone2.commands.ARDroneCommands;
import com.jbee.device.ardrone2.internal.navdata.options.Demo;
import com.jbee.units.Frequency;

import java.time.Duration;

/**
 *
 * @author weinpau
 */
public class TakeOffAndLand {

    public static void main(String[] args) throws BeeBootstrapException {
        ARDrone2 arDrone = new ARDrone2();

        BeeContext context = BeeContext.of(arDrone);

        Bee bee = context.bootstrap();
        
        arDrone.onNavDataReceived(data -> System.out.println("received: " + data.getOption(Demo.class)));
        BeeControl control = bee.control();

        System.out.println("takeOff: " + control.takeOff());
        control.hover(Duration.ofSeconds(2));

        control.execute(ARDroneCommands.playLED(LEDAnimation.BLINK_ORANGE, Frequency.ofHz(10), Duration.ofSeconds(4)));
        
        control.hover(Duration.ofSeconds(5));

        System.out.println("land: " + control.land());

        context.close();

    }

}
