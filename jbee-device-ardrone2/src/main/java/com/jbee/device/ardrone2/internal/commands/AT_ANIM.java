package com.jbee.device.ardrone2.internal.commands;

/**
 *
 * @author weinpau
 */
public class AT_ANIM extends AT_Command {

    private final int animation;
    private final int duration;

    public AT_ANIM(int animation, int duration) {
        this.animation = animation;
        this.duration = duration;
    }

    @Override
    public String getId() {
        return "ANIM";
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{animation, duration};
    }

}
