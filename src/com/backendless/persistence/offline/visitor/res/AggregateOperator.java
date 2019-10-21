package com.backendless.persistence.offline.visitor.res;

public enum AggregateOperator {
    AVG("AVG"),
    MAX("MAX"),
    MIN("MIN"),
    SUM("SUM"),
    COUNT("COUNT");

    private final String sql;

    private AggregateOperator(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}
