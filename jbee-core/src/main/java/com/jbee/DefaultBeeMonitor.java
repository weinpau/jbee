package com.jbee;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
class DefaultBeeMonitor implements BeeMonitor {

    long startTime = System.currentTimeMillis();
    BeeState lastKnownState = BeeState.START_STATE;
    List<Consumer<BeeState>> beeStateChangeListeners = new ArrayList<>();

    ExecutorService monitorExecutor = Executors.newCachedThreadPool();

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

    synchronized void changeState(BeeState state) {
        if (lastKnownState.equals(lastKnownState)) {
            lastKnownState = state;
            beeStateChangeListeners.forEach(l -> monitorExecutor.submit(() -> l.accept(state)));
        }
    }

    @Override
    public synchronized BeeState getLastKnownState() {
        return lastKnownState;
    }

}
