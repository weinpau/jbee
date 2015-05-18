package com.jbee.concurrent;

import java.util.concurrent.Callable;

/**
 *
 * @author weinpau
 * @param <T>
 */
public abstract class CallbackWrapper<T> implements Callable<T> {

    final Object resultMutex = new Object();
    boolean completed = false;
    T result;

    protected void submit(T result) {
        synchronized (resultMutex) {
            this.result = result;
            completed = true;
            resultMutex.notifyAll();
        }
    }

    protected abstract void handle();

    @Override
    public T call() throws InterruptedException {
        handle();
        synchronized (resultMutex) {

            while (!completed) {
                resultMutex.wait();
            }
            completed = false;
        }
        return result;
    }
}
