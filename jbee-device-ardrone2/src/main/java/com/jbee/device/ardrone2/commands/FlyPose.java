package com.jbee.device.ardrone2.commands;

import com.jbee.commands.AbstractCommand;
import com.jbee.device.ardrone2.FlightAnimation;
import java.time.Duration;

/**
 *
 * @author weinpau
 */
public class FlyPose extends AbstractCommand {

    FlightAnimation animation;
    Duration duration;

    public FlyPose(FlightAnimation animation, Duration duration) {
        this.animation = animation;
        this.duration = duration;
    }

    public FlightAnimation getAnimation() {
        return animation;
    }

    public Duration getDuration() {
        return duration;
    }
}
