package com.backendless.persistence.offline;

import android.database.sqlite.SQLiteException;

import com.backendless.ContextHandler;
import com.backendless.Invoker;
import com.backendless.ThreadPoolService;
import com.backendless.exceptions.BackendlessException;
import com.backendless.utils.ResponderHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weborb.client.IChainedResponder;
import weborb.exceptions.AdaptingException;
import weborb.reader.AnonymousObject;

import static com.backendless.Persistence.PERSISTENCE_MANAGER_SERVER_ALIAS;
import static com.backendless.persistence.BackendlessSerializer.serializeToMap;
import static com.backendless.persistence.offline.SyncManager.SyncMode.AUTO;
import static com.backendless.persistence.offline.SyncManager.SyncMode.SEMI_AUTO;

public class SyncManager {
    private final static SyncManager instance = new SyncManager();
    private final CallbackManager callbackManager;
    private final TransactionStorage transactionStorage;
    private boolean isSyncing = false;
    private Map<String, Boolean> tableSyncMap;
    private boolean isAutoSyncEnabled = false;

    private SyncManager() {
        TransactionStorageFactory.instance().init(ContextHandler.getAppContext());
        transactionStorage = TransactionStorageFactory.instance().getStorage();
        callbackManager = CallbackManager.getInstance();
        tableSyncMap = new HashMap<>();
    }

    public static SyncManager getInstance() {
        return instance;
    }


    public void startAutoSynchronization() {
        if (ConnectivityManager.getInstance().isConnected())
            ThreadPoolService.getPoolExecutor().execute(new SyncRunnable());
    }

    public void startSemiAutoSynchronization(SyncCompletionCallback callback) {
        if (ConnectivityManager.getInstance().isConnected())
            ThreadPoolService.getPoolExecutor().execute(new SyncRunnable(callback));
    }

    public void startSemiAutoSynchronization(String tableName, SyncCompletionCallback callback) {
        if (ConnectivityManager.getInstance().isConnected())
            ThreadPoolService.getPoolExecutor().execute(new SyncRunnable(tableName, callback));
    }

    public boolean isAutoSyncEnabled() {
        return isAutoSyncEnabled;
    }

    public boolean isAutoSyncEnabled(String tableName) {
        return tableSyncMap.containsKey(tableName) && tableSyncMap.get(tableName);
    }
    public void enableAutoSync() {
        isAutoSyncEnabled = true;
    }

    public void enableAutoSync(String tableName) {
        tableSyncMap.put(tableName, true);
    }

    public void disableAutoSync() {
        isAutoSyncEnabled = false;
    }

    public void disableAutoSync(String tableName) {
        tableSyncMap.put(tableName, false);
    }



    class SyncRunnable implements Runnable {
        private final SyncMode syncMode;
        private String tableName;
        private SyncCompletionCallback callback;
        private Map<String, SyncStatusReport> syncStatusMap = new HashMap<>();

        SyncRunnable() {
            this(AUTO, null, null);
        }

        SyncRunnable(SyncCompletionCallback callback) {
            this(SEMI_AUTO, null, callback);
        }

        SyncRunnable(String tableName, SyncCompletionCallback callback) {
            this(SEMI_AUTO, tableName, callback);
        }

        SyncRunnable(SyncMode syncMode, String tableName, SyncCompletionCallback callback) {
            this.syncMode = syncMode;
            this.tableName = tableName;
            this.callback = callback;
        }

        @Override
        public void run() {
            if (isSyncing) return;
            isSyncing = true;
            syncTransactions();
            isSyncing = false;
        }

        void syncTransactions() {
            List<Transaction> transactions = transactionStorage.get();
            for (Transaction transaction : transactions) {
                String transactionTableName = (String) transaction.getArgs()[0];

                if (syncMode == SEMI_AUTO && tableName != null && !transactionTableName.equals(tableName)) continue;

                if (syncMode == AUTO && !isAutoSyncEnabled && !isAutoSyncEnabled(transactionTableName))
                    continue;

                try {

                    if (transaction.getMethodName().equals("remove")) {
                        Map entity = (Map) transaction.getArgs()[1];
                        Invoker.invokeSync(PERSISTENCE_MANAGER_SERVER_ALIAS,
                            transaction.getMethodName(), transaction.getArgs());

                        if (transaction.getClassName() != null) {
                            Object serializedEntity = new AnonymousObject(new HashMap<>(entity))
                                .adapt(Class.forName(transaction.getClassName()));
                            callbackManager.handleRemoteResponse(serializedEntity, transaction);
                            handleSyncSuccess(transaction, serializedEntity);
                        } else {
                            callbackManager.handleRemoteResponse(entity, transaction);
                            handleSyncSuccess(transaction, entity);
                        }

                        // delete local entity
                        processRemoveTransaction(transaction);

                        // remove completed transaction from the storage
                        transactionStorage.remove(transaction);
                    } else {
                        // remove local fields
                        Integer localId = (Integer) ((Map) transaction.getArgs()[1]).remove("blLocalId");

                        IChainedResponder responder = null;
                        if (transaction.getClassName() != null)
                            responder = ResponderHelper.getPOJOAdaptingResponder( Class.forName(transaction.getClassName()) );

                        Object result = Invoker.invokeSync(PERSISTENCE_MANAGER_SERVER_ALIAS,
                            transaction.getMethodName(), transaction.getArgs(), responder);

                        // notify the registered callback if present
                        callbackManager.handleRemoteResponse(result, transaction);

                        handleSyncSuccess(transaction, result);

                        // update local entity with server response
                        if (transaction.getClassName() != null)
                            updateLocalEntity(serializeToMap(result), transaction, localId);
                        else
                            updateLocalEntity(result, transaction, localId);

                        // remove completed transaction from the storage
                        transactionStorage.remove(transaction);
                    }
                } catch( Throwable e ) {
                    callbackManager.handleRemoteFault(e, transaction);

                    handleSyncFailure(transaction, e);

                    // Delete the transaction if the fault is internal
                    if (isInternalFault(e))
                        transactionStorage.remove(transaction);
                }
            }

            // notify the completion callback if present
            if (syncMode == SEMI_AUTO && callback != null)
                callback.syncCompleted(syncStatusMap);
        }

        private void updateLocalEntity(Object result, Transaction transaction, Integer localId) {
            if (!(result instanceof Map)) return;

            String tableName = (String) transaction.getArgs()[0];
            LocalStorageManager storageManager = new LocalStorageManager(tableName);
            if (localId != null) {
                storageManager.update((Map) result, "blLocalId = " + localId);
            } else {
                storageManager.create((Map) result);
            }
        }

        private void processRemoveTransaction(Transaction transaction) {
            String tableName = (String) transaction.getArgs()[0];
            Map entity = (Map) transaction.getArgs()[1];

            Object callbackObject = null;
            if (transaction.getClassName() == null) {
                callbackObject = entity;
            } else {
                try {
                    callbackObject = new AnonymousObject(new HashMap<>(entity))
                        .adapt(Class.forName(transaction.getClassName()));
                } catch (AdaptingException | ClassNotFoundException ignored) { }
            }
            LocalStorageManager storageManager = new LocalStorageManager(tableName);
            if (entity.containsKey("objectId")) {
                storageManager.remove(callbackObject, (String) entity.get("objectId"),
                    callbackManager.getOfflineAwareCallback(transaction));
            }
        }

        private void handleSyncSuccess(Transaction transaction, Object result) {
            String tableName = (String) transaction.getArgs()[0];
            Map entity = (Map) transaction.getArgs()[1];

            SyncStatusReport syncStatusReport = syncStatusMap.get(tableName);
            if (syncStatusReport == null)
                syncStatusReport = new SyncStatusReport();

            if (transaction.getMethodName().equals("save")) {
                if (entity.containsKey("objectId"))
                    syncStatusReport.successfulCompletion.updated.add(result);
                else
                    syncStatusReport.successfulCompletion.created.add(result);
            } else if (transaction.getMethodName().equals("remove")) {
                syncStatusReport.successfulCompletion.deleted.add(result);
            }

            syncStatusMap.put(tableName, syncStatusReport);
        }

        private void handleSyncFailure(Transaction transaction, Throwable e) {
            String tableName = (String) transaction.getArgs()[0];
            Map entity = (Map) transaction.getArgs()[1];

            SyncError syncError = new SyncError(entity, e.getMessage());

            SyncStatusReport syncStatusReport = syncStatusMap.get(tableName);
            if (syncStatusReport == null)
                syncStatusReport = new SyncStatusReport();

            if (transaction.getMethodName().equals("save")) {
                if (entity.containsKey("objectId"))
                    syncStatusReport.failedCompletion.updateErrors.add(syncError);
                else
                    syncStatusReport.failedCompletion.createErrors.add(syncError);
            } else if (transaction.getMethodName().equals("remove")) {
                syncStatusReport.failedCompletion.deleteErrors.add(syncError);
            }

            syncStatusMap.put(tableName, syncStatusReport);
        }

        private boolean isInternalFault(Throwable e) {
            if (e instanceof SQLiteException) return true;

            if (e instanceof BackendlessException) {
                BackendlessException backendlessException = (BackendlessException) e;
                return !backendlessException.getCode().equals("Internal client exception");
            }
            return false;
        }
    }

    enum SyncMode {
        AUTO,
        SEMI_AUTO,
        MANUAL
    }
}
