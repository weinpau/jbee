package com.jbee.device.ardrone2.internal.commands;

import java.io.UnsupportedEncodingException;

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

    public byte[] toBytes(int sequenceNumber) {
        try {
            return encode(sequenceNumber).getBytes("ASCII");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    String encodedParameters() {
        StringBuilder sb = new StringBuilder();
        for (Object p : getParameters()) {
            sb.append(",").append(encodeParameter(p));
        }

        return sb.toString();
    }

    String encodeParameter(Object parameter) {
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
