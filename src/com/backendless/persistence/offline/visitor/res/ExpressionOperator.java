package com.backendless.persistence.offline.visitor.res;

public enum ExpressionOperator {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%");

    private final String sql;

    private ExpressionOperator(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}
