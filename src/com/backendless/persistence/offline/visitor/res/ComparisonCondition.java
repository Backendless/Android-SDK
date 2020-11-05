package com.backendless.persistence.offline.visitor.res;

public class ComparisonCondition implements Condition {
    private Field left;
    private Operator operator;
    private Field right;

    public ComparisonCondition(Field left, Operator operator, Field right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return left + " " + operator + " " + right;
    }
}
