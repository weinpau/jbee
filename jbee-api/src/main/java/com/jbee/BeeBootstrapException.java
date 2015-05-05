package com.jbee;

import java.io.IOException;

/**
 *
 * @author weinpau
 */
public class BeeBootstrapException extends IOException {

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
