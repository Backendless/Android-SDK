package com.backendless.persistence.offline;

import com.backendless.ContextHandler;
import com.backendless.async.callback.AsyncCallback;

public class TransactionManager {
    private final static TransactionManager instance = new TransactionManager();
    private final CallbackManager callbackManager;
    private final TransactionStorage transactionStorage;
    private final SyncManager syncManager;


    private TransactionManager() {
        TransactionStorageFactory.instance().init(ContextHandler.getAppContext());
        transactionStorage = TransactionStorageFactory.instance().getStorage();
        callbackManager = CallbackManager.getInstance();
        syncManager = SyncManager.getInstance();
    }

    public static TransactionManager getInstance()
    {
        return instance;
    }

    public void onSave(String tableName, AsyncCallback callback) {
        callbackManager.putGlobalSaveCallback(tableName, callback);
    }

    public void onRemove(String tableName, AsyncCallback callback) {
        callbackManager.putGlobalRemoveCallback(tableName, callback);
    }

    public <T> void scheduleOperation(String methodName, Object[] args, String className,
                                      OfflineAwareCallback<T> responder) {
        Transaction transaction = new Transaction(methodName, args, className);

        callbackManager.putOfflineAwareCallback(transaction.getId(), responder);

        transactionStorage.put(transaction);

        syncManager.startAutoSynchronization();
    }

}
