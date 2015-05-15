package com.jbee.device.ardrone2.internal.commands;

/**
 *
 * @author weinpau
 */
public class AT_CONFIG extends AT_Command {

    private final String name, value;

    public AT_CONFIG(String name, Object value) {
        this.name = name;
        this.value = parseConfigValue(value);
    }

    final String parseConfigValue(Object value) {
        if (value instanceof Boolean) {
            return ((boolean) value) ? "TRUE" : "FALSE";
        }
        return value.toString();
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
