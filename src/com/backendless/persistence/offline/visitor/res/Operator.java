package com.backendless.persistence.offline.visitor.res;

public enum Operator {

    AND("and"),
    OR("or"),
    EQUAL("="),
    NOT_EQUAL("!="),
    GREATER_THAN_OR_EQUAL(">="),
    GREATER_THAN(">"),
    LESS_THAN_OR_EQUAL("<="),
    LESS_THAN("<");

    private final String sql;

    private Operator(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}

