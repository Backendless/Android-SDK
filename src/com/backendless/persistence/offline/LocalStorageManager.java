package com.backendless.persistence.offline;

import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.ThreadPoolService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weborb.exceptions.AdaptingException;
import weborb.reader.AnonymousObject;

import static com.backendless.persistence.BackendlessSerializer.serializeToMap;
import static com.backendless.persistence.offline.DataRetrievalPolicy.ONLINEONLY;
import static com.backendless.persistence.offline.DatabaseHelper.parseClause;
import static com.backendless.persistence.offline.LocalStoragePolicy.STOREALL;

public class LocalStorageManager {
    private final DatabaseManager databaseManager;
    private String tableName;

    public LocalStorageManager(String tableName)
    {
        this.tableName = tableName;
        databaseManager = SQLiteDatabaseManager.getInstance();
    }

    public void initDatabase(@Nullable final String whereClause, final AsyncCallback<Integer> responder) {
        if (!databaseManager.isTableEmpty(tableName)) {
            if (responder != null) {
                responder.handleFault(new BackendlessFault("The database cannot be initialized because it is not empty."));
            }
            return;
        }

        ThreadPoolService.getPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                int pageSize = 100;
                int listSize;
                int totalCount = 0;
                final IDataStore<Map> dataStore = Backendless.Data.of(tableName);
                final DataQueryBuilder queryBuilder = DataQueryBuilder
                    .create()
                    .setPageSize(pageSize)
                    .setOffset(0)
                    .setWhereClause(whereClause)
                    .setRetrievalPolicy(ONLINEONLY)
                    .setStoragePolicy(STOREALL);

                try {
                    do {
                        List<Map> result = dataStore.find(queryBuilder);
                        listSize = result.size();
                        totalCount += listSize;
                        queryBuilder.prepareNextPage();
                    } while (listSize == pageSize);
                    if (responder != null)
                        responder.handleResponse(totalCount);
                } catch (Throwable e) {
                    if (responder != null)
                        responder.handleFault(new BackendlessFault(e));
                }
            }
        });
    }

    public void clearLocalDatabase() {
        databaseManager.dropTable(tableName);
    }

    public <E> List<E> find(Class<E> clazz, DataQueryBuilder queryBuilder) {
        BackendlessDataQuery dataQuery = queryBuilder.build();
        List<Map> entities = databaseManager.select(
            tableName,
            null,
            parseClause(dataQuery.getWhereClause()),
            null,
            TextUtils.join(",", dataQuery.getGroupBy()),
            dataQuery.getHavingClause(),
            TextUtils.join(",", dataQuery.getQueryOptions().getSortBy()),
            String.valueOf(dataQuery.getPageSize()));
        if (clazz == Map.class) {
            return (List<E>) entities;
        } else {
            List<E> serializedEntities = new ArrayList<>();
            for (Map map : entities) {
                try {
                    serializedEntities.add((E) new AnonymousObject(new HashMap<>(map)).adapt(clazz));
                } catch (AdaptingException ignored) { }
            }
            return serializedEntities;
        }
    }

    public <E> void find(final Class<E> clazz, final DataQueryBuilder queryBuilder, final AsyncCallback<List<E>> callback) {
        ThreadPoolService.getPoolExecutor().execute( new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    List<E> result =  find(clazz, queryBuilder);
                    if (callback != null)
                        callback.handleResponse(result);
                }
                catch( BackendlessException e )
                {
                    if (callback != null)
                        callback.handleFault( new BackendlessFault( e ) );
                }
            }
        } );
    }

    public <E> int save(E entity, OfflineAwareCallback<E> responder) {
        Map serializedEntity;
        if (entity instanceof Map) {
            serializedEntity = (Map) entity;
        } else {
            serializedEntity = serializeToMap( entity );
        }
        try {
            Map result;
            if (serializedEntity.containsKey("objectId") && serializedEntity.get("objectId") != null) {
                result = update(serializedEntity);
            } else {
                result = create(serializedEntity);
            }

            if (responder != null) {
                if (entity instanceof Map) {
                    responder.handleLocalResponse((E) result);
                } else {
                    responder.handleLocalResponse(entity);
                }
            }

            return (int) result.get("blLocalId");
        } catch (Throwable e) {
            if (responder != null)
                responder.handleLocalFault(new BackendlessFault(e));
        }
        return -1;
    }

    public Map create(Map entity) {
        return databaseManager.insert(tableName, entity);
    }

    public Map update(Map entity) {
        return update(entity, "objectId = '" + entity.get("objectId") + "'");
    }

    public Map update(Map entity, String whereClause) {
        return databaseManager.update(tableName, entity, whereClause, null);
    }

    public void remove(Object callbackObject, String objectId, OfflineAwareCallback responder) {
        try {
            String whereClause = "objectId = '" + objectId + "'";
            databaseManager.delete(tableName, whereClause, null);
            if (responder != null)
                responder.handleLocalResponse(callbackObject);
        } catch (Throwable e) {
            if (responder != null)
                responder.handleLocalFault(new BackendlessFault(e));
        }
    }

    public <E> void store(List<E> entities, DataQueryBuilder queryBuilder) {
        LocalStoragePolicy storagePolicy = queryBuilder.getStoragePolicy();
        if (storagePolicy == null)
            storagePolicy = Backendless.Data.LocalStoragePolicy;
        switch (storagePolicy) {
            case STOREUPDATED: {
                for (E entity : entities) {
                    try {
                        if (entity instanceof Map)
                            update((Map) entity);
                        else
                            update(serializeToMap(entity));
                    } catch (SQLiteException ignored) {}
                }
                break;
            }
            case STOREALL: {
                for (E entity : entities) {
                    try {
                        if (entity instanceof Map)
                            update((Map) entity);
                        else
                            update(serializeToMap(entity));
                    } catch (SQLiteException e) {
                        if (entity instanceof Map)
                            create((Map) entity);
                        else
                            create(serializeToMap(entity));
                    }
                }
                break;
            }
            case DONOTSTOREANY:
            default: break;

        }
    }

    public boolean shouldRetrieveOnline(DataQueryBuilder dataQuery) {
        DataRetrievalPolicy retrievalPolicy = dataQuery.getRetrievalPolicy();
        if (retrievalPolicy == null)
            retrievalPolicy = Backendless.Data.RetrievalPolicy;
        switch (retrievalPolicy) {
            case ONLINEONLY: return true;
            case OFFLINEONLY: return false;
            case DYNAMIC: return ConnectivityManager.getInstance().isConnected();
            default: return true;
        }
    }

    public <E> AsyncCallback<List<E>> getStoreCallback(final DataQueryBuilder dataQuery,
                                                       final AsyncCallback<List<E>> callback ) {
        return new AsyncCallback<List<E>>() {
            @Override
            public void handleResponse(List<E> response) {
                store(response, dataQuery);
                if (callback != null)
                    callback.handleResponse(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (callback != null)
                    callback.handleFault(fault);
            }
        } ;
    }
}
