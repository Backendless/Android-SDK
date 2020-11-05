package com.backendless.persistence.offline.visitor.res;

public class NotCondition implements Condition {
    private Condition condition;

    public NotCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "NOT " + condition;
    }
}
