package com.jbee.device.ardrone2.internal.commands;

/**
 *
 * @author weinpau
 */
public abstract class AT_Command {

    public abstract String getId();

    public abstract Object[] getParameters();

    public String encode(int sequenceNumber) {
        return "AT*" + getId() + "=" + sequenceNumber + encodedParameters() + "\r";
    }

    private String encodedParameters() {
        StringBuilder sb = new StringBuilder();
        for (Object p : getParameters()) {
            sb.append(",").append(encodeParameter(p));
        }

        return sb.toString();
    }

    private String encodeParameter(Object parameter) {
        if (parameter instanceof Integer) {
            return parameter.toString();
        }
        if (parameter instanceof Float) {
            return Integer.toString(Float.floatToIntBits((Float) parameter));
        }
        if (parameter instanceof String) {
            return "\"" + parameter + "\"";
        }

        throw new IllegalArgumentException("Unsupported parameter type: " + parameter.getClass().getName() + ".");
    }

}
