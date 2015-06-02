package com.jbee.device.ardrone2.commands;

import com.jbee.commands.Command;
import com.jbee.commands.Commands;
import com.jbee.device.ardrone2.FlightAnimation;
import com.jbee.device.ardrone2.LEDAnimation;
import com.jbee.units.Frequency;
import java.time.Duration;

/**
 *
 * @author weinpau
 */
public final class ARDroneCommands extends Commands {

    private ARDroneCommands() {
    }

    public static Command playLED(LEDAnimation animation, Frequency frequency, Duration duration) {
        return new PlayLEDAnimation(animation, frequency, duration);
    }

    public static Command flyPose(FlightAnimation animation, Duration duration) {
        return new FlyPose(animation, duration);
    }

}
