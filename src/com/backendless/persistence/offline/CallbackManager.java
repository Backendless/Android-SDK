package com.backendless.persistence.offline;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.HashMap;
import java.util.Map;

public class CallbackManager {
    private final static CallbackManager instance = new CallbackManager();

    private final Map<String, AsyncCallback> globalSaveCallbacks = new HashMap<>();
    private final Map<String, AsyncCallback> globalRemoveCallbacks = new HashMap<>();
    private final Map<String, OfflineAwareCallback> offlineAwareCallbacks = new HashMap<>();

    private CallbackManager()
    {

    }

    public static CallbackManager getInstance() {
        return instance;
    }


    public void putGlobalSaveCallback(String tableName, AsyncCallback callback) {
        globalSaveCallbacks.put(tableName, callback);
    }

    public void putGlobalRemoveCallback(String tableName, AsyncCallback callback) {
        globalRemoveCallbacks.put(tableName, callback);
    }

    public void putOfflineAwareCallback(String transactionId, OfflineAwareCallback callback) {
        offlineAwareCallbacks.put(transactionId, callback);
    }

    public void handleRemoteResponse(Object entity, Transaction transaction) {
        String transactionId = transaction.getId();
        OfflineAwareCallback offlineAwareCallback = offlineAwareCallbacks.get(transactionId);
        if (offlineAwareCallback != null) {
            offlineAwareCallback.handleRemoteResponse(entity);
        } else {
            String tableName = (String) transaction.getArgs()[0];

            if (transaction.getMethodName().equals("save")) {
                AsyncCallback callback = globalSaveCallbacks.get(tableName);
                if (callback != null)
                    callback.handleResponse(entity);
            } else if (transaction.getMethodName().equals("remove")) {
                AsyncCallback callback = globalRemoveCallbacks.get(tableName);
                if (callback != null)
                    callback.handleResponse(entity);
            }
        }
    }

    public void handleRemoteFault(Throwable e, Transaction transaction) {
        String transactionId = transaction.getId();
        BackendlessFault fault = new BackendlessFault(e);
        OfflineAwareCallback offlineAwareCallback = offlineAwareCallbacks.get(transactionId);
        if (offlineAwareCallback != null) {
            offlineAwareCallback.handleRemoteFault(fault);
        } else {
            String tableName = (String) transaction.getArgs()[0];

            if (transaction.getMethodName().equals("save")) {
                AsyncCallback callback = globalSaveCallbacks.get(tableName);
                if (callback != null)
                    callback.handleFault(fault);
            } else if (transaction.getMethodName().equals("remove")) {
                AsyncCallback callback = globalRemoveCallbacks.get(tableName);
                if (callback != null)
                    callback.handleFault(fault);
            }
        }
    }

    public OfflineAwareCallback getOfflineAwareCallback(Transaction transaction) {
        String id = transaction.getId();
        return offlineAwareCallbacks.get(id);
    }

}
