package com.backendless.persistence.offline.visitor.res;

public class Field<T> implements QueryPart {
    public String alias;
    private String name;

    public Field() {
    }

    public Field(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
