package com.jbee.device.ardrone2.internal.commands;

/**
 *
 * @author weinpau
 */
public class AT_LED extends AT_Command {

    private final int animation;
    private final float frequency;
    private final int duration;

    public AT_LED(int animation, float frequency, int duration) {
        this.animation = animation;
        this.frequency = frequency;
        this.duration = duration;
    }

    @Override
    public String getId() {
        return "LED";
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{animation, frequency, duration};
    }

}
