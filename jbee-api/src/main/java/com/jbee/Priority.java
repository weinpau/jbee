package com.jbee;

/**
 *
 * @author weinpau
 */
public final class Priority implements Comparable<Priority> {

    private final int value;

    public static final Priority HIGH = new Priority(0);
    public static final Priority MEDIUM = new Priority(4);
    public static final Priority LOW = new Priority(9);

    Priority(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Priority o) {

        return Integer.compare(value, o.value);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.value;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Priority other = (Priority) obj;
        return this.value == other.value;
    }

}
