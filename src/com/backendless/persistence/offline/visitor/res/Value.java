package com.backendless.persistence.offline.visitor.res;

public class Value<T> extends Field<T> {
    T value;

    public Value(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
