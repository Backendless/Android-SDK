package com.backendless.persistence.offline;

import java.util.Map;

public interface SyncCompletionCallback
{
    void syncCompleted( Map<String, SyncStatusReport> syncStatusMap );
}
