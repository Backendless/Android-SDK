package com.backendless.persistence.offline.visitor.res;

public class NullCondition implements Condition {
    private Field field;
    private boolean isNull;

    public NullCondition(Field field, boolean isNull) {
        this.field = field;
        this.isNull = isNull;
    }

    @Override
    public String toString() {
        return field + (isNull ? " IS NULL " : " IS NOT NULL ");
    }
}
