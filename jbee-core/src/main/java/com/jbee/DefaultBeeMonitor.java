package com.jbee;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
class DefaultBeeMonitor implements BeeMonitor {

    volatile int currentCommandNumber;
    volatile long startTime = System.currentTimeMillis();
    volatile BeeState lastKnownState = BeeState.START_STATE;
    List<Consumer<BeeState>> beeStateChangeListeners = new ArrayList<>();

    @Override
    public long getCurrentTimestamp() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public void onStateChange(Consumer<BeeState> beeStateListener) {
        beeStateChangeListeners.add(beeStateListener);
    }

    public void removeStateChangeListener(Consumer<BeeState> beeStateListener) {
        beeStateChangeListeners.remove(beeStateListener);
    }

    public synchronized void changeState(BeeState state) {
        if (!lastKnownState.equals(state)) {
            lastKnownState = state;            
            beeStateChangeListeners.forEach(l -> l.accept(state));
        }
    }

    @Override
    public BeeState getLastKnownState() {
        return lastKnownState;
    }

    public int newCommandNumber() {
        return ++currentCommandNumber;
    }

    public int getCurrentCommandNumber() {
        return currentCommandNumber;
    }

}
