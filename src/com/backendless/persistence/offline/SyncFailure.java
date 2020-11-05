package com.backendless.persistence.offline;

import java.util.ArrayList;
import java.util.List;

public class SyncFailure {
    public List<SyncError> createErrors;
    public List<SyncError> updateErrors;
    public List<SyncError> deleteErrors;

    public SyncFailure() {
        this.createErrors = new ArrayList<>();
        this.updateErrors = new ArrayList<>();
        this.deleteErrors = new ArrayList<>();
    }
}
