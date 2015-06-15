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
import com.jbee.Direction;
import com.jbee.RotationDirection;
import com.jbee.commands.Commands;
import com.jbee.commands.FlyCommand;
import com.jbee.commands.FlyCommandBuilder;
import com.jbee.device.pixhawk.Pixhawk;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
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
        
        
        BeeControl control = bee.control();

        control = control.rotationalSpeed(RotationalSpeed.rpm(1));
        
        //control = control.onPositionChanged((s,p) -> System.out.println(p), Distance.ofMeters(1));
        
        
        //control.takeOff();
        
        //control.execute(Commands.forward(Distance.ofMeters(5)).andRotate(Angle.ofDegrees(360), RotationDirection.CW).build());
        control.execute(Commands.rotate(Angle.ofDegrees(360), RotationDirection.CCW).andRight(Distance.ofMeters(50)).with(Speed.mps(10)).build());
        //control.execute(Commands.rotate(Angle.ofDegrees(360), RotationDirection.CW).build());
        for (int i = 0; i < 4; i++) {
            //control.execute(Commands.forward(Distance.ofMeters(10)).andRotate(Angle.ofDegrees(90), RotationDirection.CW).build());
        }
        //control.land();
        
        
        context.close();

    }
}
