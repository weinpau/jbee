package com.jbee;

/**
 *
 * @author weinpau
 */
public class BeeBootstrapException extends Exception {

    public BeeBootstrapException() {
    }

    public BeeBootstrapException(String message) {
        super(message);
    }

    public BeeBootstrapException(Throwable cause) {
        super(cause);
    }

    public BeeBootstrapException(String message, Throwable cause) {
        super(message, cause);
    }

}
