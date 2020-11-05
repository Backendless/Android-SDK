package com.backendless.persistence.offline;

public class SyncError {
    public Object object;
    public String error;

    public SyncError(Object object, String error) {
        this.object = object;
        this.error = error;
    }
}
