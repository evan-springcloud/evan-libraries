package org.evan.libraries.redis.serializer;

/**
 * @author Evan.Shen
 * @since 2019-06-19
 */
public class FastJsonWraper<T> {
    private T value;

    public FastJsonWraper() {
    }

    public FastJsonWraper(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
