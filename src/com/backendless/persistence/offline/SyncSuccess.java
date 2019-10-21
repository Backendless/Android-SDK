package com.backendless.persistence.offline;

import java.util.ArrayList;
import java.util.List;

public class SyncSuccess {
    public List<Object> created;
    public List<Object> updated;
    public List<Object> deleted;

    public SyncSuccess() {
        this.created = new ArrayList<>();
        this.updated = new ArrayList<>();
        this.deleted = new ArrayList<>();
    }
}
