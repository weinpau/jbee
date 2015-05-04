package com.jbee.device.ardrone2.navdata.options;

import java.nio.ByteBuffer;

/**
 *
 * @author weinpau
 */
public interface Option {

    OptionId getId();
    
    void parse(ByteBuffer buffer);

}
