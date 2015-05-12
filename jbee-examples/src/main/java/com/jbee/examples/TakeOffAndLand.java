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
            
            BeeContext context = BeeContext.of(new ARDrone2());
                        
            Bee bee = context.bootstrap();
            
            BeeControl control = bee.control();
            
            control.takeOff();
            control.hover(Duration.ofSeconds(5));
            control.land();
            
            context.close();
            
            
        }
    
}
