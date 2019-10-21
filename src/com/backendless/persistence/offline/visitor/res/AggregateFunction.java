package com.backendless.persistence.offline.visitor.res;

public class AggregateFunction extends Field {
    private final AggregateOperator operator;
    private final Field field;

    public AggregateFunction(AggregateOperator operator, Field field) {
        this.operator = operator;
        this.field = field;
    }

    @Override
    public String toString() {
        return operator + "(" + field + ")";
    }
}
