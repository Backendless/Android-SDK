package com.backendless.persistence.offline.visitor.res;

public class CombinedCondition implements Condition {
    private final Condition leftCondition;
    private final Operator operator;
    private final Condition rightCondition;

    public CombinedCondition(Condition leftCondition, Operator operator, Condition rightCondition) {
        this.leftCondition = leftCondition;
        this.operator = operator;
        this.rightCondition = rightCondition;
    }

    @Override
    public String toString() {
        return "(" + leftCondition + ") " + operator + " (" + rightCondition + ")";
    }
}
