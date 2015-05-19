package com.jbee.device.ardrone2.commands;

import com.jbee.commands.AbstractCommand;
import com.jbee.device.ardrone2.LEDAnimation;
import com.jbee.units.Frequency;
import java.time.Duration;

/**
 *
 * @author weinpau
 */
public class PlayLEDAnimation extends AbstractCommand {

    LEDAnimation animation;
    Frequency frequency;
    Duration duration;

    public PlayLEDAnimation(LEDAnimation animation, Frequency frequency, Duration duration) {
        this.animation = animation;
        this.frequency = frequency;
        this.duration = duration;
    }

    public LEDAnimation getAnimation() {
        return animation;
    }

       
    public Frequency getFrequency() {
        return frequency;
    }

    public Duration getDuration() {
        return duration;
    }

}
