package com.jbee;

import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
public interface BeeMonitor {

    long getCurrentTimestamp();

    void onStateChange(Consumer<BeeState> beeStateListener);

    BeeState getLastKnownState();

}
