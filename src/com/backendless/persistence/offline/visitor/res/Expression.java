package com.backendless.persistence.offline.visitor.res;

public class Expression extends Field {
    private final Field lhs;
    private final ExpressionOperator operator;
    private final Field rhs;

    public Expression(Field lhs, ExpressionOperator operator, Field rhs) {
        this.lhs = lhs;
        this.operator = operator;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        return lhs + " " + operator + " " + rhs;
    }
}
