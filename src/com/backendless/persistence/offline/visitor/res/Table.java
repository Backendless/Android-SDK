package com.backendless.persistence.offline.visitor.res;

public class Table implements QueryPart {
    private String tableName;

    public Table(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return tableName;
    }
}
