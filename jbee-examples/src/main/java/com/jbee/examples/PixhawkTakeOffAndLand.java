package com.jbee.examples;

import com.jbee.Bee;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeContext;
import com.jbee.BeeControl;
import com.jbee.device.ardrone2.ARDrone2;
import com.jbee.device.ardrone2.LEDAnimation;
import com.jbee.device.ardrone2.commands.ARDroneCommands;
import com.jbee.device.ardrone2.internal.navdata.options.Demo;
import com.jbee.device.pixhawk.Pixhawk;
import com.jbee.units.Frequency;

import java.time.Duration;

/**
 *
 * @author weinpau
 */
public class PixhawkTakeOffAndLand {

    public static void main(String[] args) throws BeeBootstrapException {
        Pixhawk pixhawk = new Pixhawk();

        BeeContext context = BeeContext.of(pixhawk);

        Bee bee = context.bootstrap();
        bee.monitor().onStateChange(state -> System.out.println(state.getPosition()));
        BeeControl control = bee.control();

        System.out.println("takeOff: " + control.takeOff());

        control.hover(Duration.ofSeconds(5));

        System.out.println("land: " + control.land());

        context.close();

    }

}
