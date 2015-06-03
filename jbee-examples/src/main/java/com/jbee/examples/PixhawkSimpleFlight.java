/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jbee.examples;

import com.jbee.Bee;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeContext;
import com.jbee.BeeControl;
import com.jbee.RotationDirection;
import com.jbee.commands.Commands;
import com.jbee.commands.FlyCommand;
import com.jbee.commands.FlyCommandBuilder;
import com.jbee.device.pixhawk.Pixhawk;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.RotationalSpeed;
import java.time.Duration;

/**
 *
 * @author Trader
 */
public class PixhawkSimpleFlight {
    public static void main(String[] args) throws BeeBootstrapException {
        Pixhawk pixhawk = new Pixhawk();

        BeeContext context = BeeContext.of(pixhawk);

        Bee bee = context.bootstrap();
        //bee.monitor().onStateChange(state -> System.out.print("\r" + state.getPosition()));
        BeeControl control = bee.control();


        //Commands.forward(Distance.ZERO).andRotate(Angle.ZERO, RotationDirection.CLOCKWISE).build()
        control = control.rotationalSpeed(RotationalSpeed.rpm(6));
        
        control.takeOff();
        //control.hover(Duration.ofSeconds(1));
        /*
       for (int i = 0; i < 4; i++) {
            control.forward(Distance.ofMeters(5));
            control.rotate(Angle.ofDegrees(90), RotationDirection.CLOCKWISE);
        }*/
        for (int i = 0; i < 4; i++) {
            control.execute(Commands.forward(Distance.ofMeters(10)).andRotate(Angle.ofDegrees(90), RotationDirection.CLOCKWISE).build());
        }
        System.out.println("land: " + control.land());
        
        context.close();

    }
}
