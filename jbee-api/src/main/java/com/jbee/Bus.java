package com.jbee;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 * @param <T>
 */
public abstract class Bus<T> implements Comparable<Bus> {

    volatile T lastKnownValue;

    Priority priority;

    List<Consumer<T>> subscribers = new CopyOnWriteArrayList<>();

    public Bus(Priority priority) {
        this.priority = priority;
    }

    public void publish(T object) {
        lastKnownValue = object;
        subscribers.forEach(s -> s.accept(object));
    }

    public void subscripe(Consumer<T> subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Consumer<T> subscriber) {
        subscribers.remove(subscriber);
    }

    public Optional<T> getLastKnownValue() {
        return Optional.ofNullable(lastKnownValue);
    }

    public void bootstrap(TargetDevice targetDevice, BusRegistry busRegistry) throws BeeBootstrapException {
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Bus o) {
        return getPriority().compareTo(o.getPriority());
    }

}
