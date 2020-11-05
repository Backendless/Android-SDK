package com.backendless.persistence.offline.visitor.res;

public class SortField extends Field {
    private SortOrder order;
    private Field field;

    public SortField(Field field, SortOrder order) {
        this.field = field;
        this.order = order;
    }

    public void setOrder(SortOrder order) {
        this.order = order;
    }

    public enum SortOrder {
        ASC, DESC
    }
}
