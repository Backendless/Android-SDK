package com.backendless.persistence.offline;

public class SyncStatusReport {
    public SyncSuccess successfulCompletion;
    public SyncFailure failedCompletion;

    public SyncStatusReport() {
        successfulCompletion = new SyncSuccess();
        failedCompletion = new SyncFailure();
    }
}
