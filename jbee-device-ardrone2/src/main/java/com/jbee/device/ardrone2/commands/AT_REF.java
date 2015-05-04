package com.jbee.device.ardrone2.commands;

/**
 *
 * @author weinpau
 */
public class AT_REF extends AT_Command {

    public static final AT_Command LAND = new AT_REF(0);
    public static final AT_Command TAKE_OFF = new AT_REF(1 << 9);
    public static final AT_Command EMERGENCY = new AT_REF(1 << 8);
    private final int value;

    public AT_REF(int value) {
        this.value = (value |= (1 << 18) | (1 << 20) | (1 << 22) | (1 << 24) | (1 << 28));
    }

    @Override
    public String getId() {
        return "REF";
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{value};
    }

}
