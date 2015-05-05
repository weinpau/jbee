package com.jbee;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 * @param <T>
 */
public abstract class Bus<T> implements Comparable<Bus> {

    Priority priority;

    List<Consumer<T>> subscribers = new CopyOnWriteArrayList<>();

    public Bus(Priority priority) {
        this.priority = priority;
    }

    public void publish(T object) {
        subscribers.forEach(s -> s.accept(object));
    }

    public void subscripe(Consumer<T> subscriber) {
        subscribers.add(subscriber);
    }

    public void bootstrap() throws BeeBootstrapException {
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Bus o) {
        return getPriority().compareTo(o.getPriority());
    }

}
