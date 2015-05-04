package com.jbee.device.ardrone2.navdata;

import com.jbee.device.ardrone2.navdata.options.Option;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author weinpau
 */
public class NavData {

    State state;
    int sequenceNumber;
    int visionFlag;

    final Map<Class<? extends Option>, Option> options = new HashMap<>();

    NavData() {
    }

    public <T extends Option> T getOption(Class<T> optionType) {
        return (T) options.get(optionType);
    }

    public State getState() {
        return state;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getVisionFlag() {
        return visionFlag;
    }

}
