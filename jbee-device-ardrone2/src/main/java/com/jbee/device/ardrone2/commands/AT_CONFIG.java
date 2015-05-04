package com.jbee.device.ardrone2.commands;

/**
 *
 * @author weinpau
 */
public class AT_CONFIG extends AT_Command {

    private final String name, value;

    public AT_CONFIG(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getId() {
        return "CONFIG";
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{name, value};
    }

}
