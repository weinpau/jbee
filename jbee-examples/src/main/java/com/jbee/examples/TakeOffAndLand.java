package com.jbee.examples;

import com.jbee.Bee;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeContext;
import com.jbee.BeeControl;
import com.jbee.device.ardrone2.ARDrone2;

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

        //arDrone.onNavDataReceived(data -> System.out.println("received: " + data.getOption(Demo.class)));
        bee.monitor().onStateChange(System.out::println);

        System.out.println(bee.monitor().getLastKnownState().getBatteryState().getLevel());

        BeeControl control = bee.control();

        System.out.println("takeOff: " + control.takeOff());
        control.hover(Duration.ofSeconds(5));
        System.out.println("land: " + control.land());

        context.close();

    }

}
