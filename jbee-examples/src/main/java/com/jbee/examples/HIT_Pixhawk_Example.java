package com.jbee.examples;

import com.jbee.Bee;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeContext;
import com.jbee.BeeControl;
import com.jbee.RotationDirection;
import com.jbee.commands.Command;
import com.jbee.commands.Commands;
import com.jbee.device.pixhawk.Pixhawk;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import java.util.function.BiConsumer;

public class HIT_Pixhawk_Example {
    public static void main(String[] args) throws BeeBootstrapException {
        Pixhawk pixhawk = new Pixhawk();
        BeeContext context = BeeContext.of(pixhawk);
        Bee bee = context.bootstrap();
        BeeControl control = bee.control();

        //Jeden Meter die Position ausgeben
        control = control.onPositionChanged(new BiConsumer<Command, Position>() {

            public void accept(Command s, Position p) {
                System.out.println(p);
            }
        }, Distance.ofMeters(1));
        
        //Drohne starten
        control.takeOff();
        
        //4 mal vorwärts fliegen und dabei um 90 Grad drehen
        for (int i = 0; i < 4; i++) {
            control.execute(Commands.forward(Distance.ofMeters(5)).andRotate(Angle.ofDegrees(90), RotationDirection.CW).build());
        }
        
        //landen
        control.land();
        
        context.close();
    }
}
