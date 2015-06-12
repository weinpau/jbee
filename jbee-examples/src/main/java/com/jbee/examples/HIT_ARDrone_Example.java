package com.jbee.examples;

import com.jbee.Bee;
import com.jbee.BeeBootstrapException;
import com.jbee.BeeContext;
import com.jbee.BeeControl;
import com.jbee.commands.Command;
import com.jbee.device.ardrone2.ARDrone2;
import com.jbee.device.ardrone2.FlightAnimation;
import com.jbee.device.ardrone2.commands.ARDroneCommands;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import java.time.Duration;
import java.util.function.BiConsumer;


public class HIT_ARDrone_Example {
        public static void main(String[] args) throws BeeBootstrapException {
        ARDrone2 ardrone = new ARDrone2();
        BeeContext context = BeeContext.of(ardrone);
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
        
        //2 Sekunden schweben 
        control.hover(Duration.ofSeconds(2));
        
        //3 sekunden Tanzen
        control.execute(ARDroneCommands.flyPose(FlightAnimation.PHI_DANCE, Duration.ofSeconds(3)));
        
        //2Sekunden schweben
        control.hover(Duration.ofSeconds(2));
        
        //landen
        control.land();
        
        context.close();
    }
}
