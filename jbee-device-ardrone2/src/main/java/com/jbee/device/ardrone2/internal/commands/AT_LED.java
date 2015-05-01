package com.jbee.device.ardrone2.internal.commands;

/**
 *
 * @author weinpau
 */
public class AT_LED extends AT_Command {

    private int animation;
    private float frequency;
    private int duration;

    @Override
    public String getId() {
        return "LED";
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{animation, frequency, duration};

    }

}
