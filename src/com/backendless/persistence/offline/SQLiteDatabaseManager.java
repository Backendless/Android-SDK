package com.backendless.persistence.offline;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.backendless.ContextHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.backendless.persistence.offline.DatabaseHelper.cursorToMap;
import static com.backendless.persistence.offline.DatabaseHelper.getColumnDeclaration;
import static com.backendless.persistence.offline.DatabaseHelper.mapToContentValues;

public class SQLiteDatabaseManager implements DatabaseManager {
    private final static SQLiteDatabaseManager instance = new SQLiteDatabaseManager();
    private final String databaseName = "BackendlessLocalStorage.db";
    private final int databaseVersion = 1;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;

    public static SQLiteDatabaseManager getInstance()
    {
        return instance;
    }

    private SQLiteDatabaseManager() {
        initHelper();
    }

    @Override
    public List<Map> select(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        return cursorToMap(cursor);
    }

    @Override
    public Map insert(String table, Map values) {
        createTableIfNotExists(table, values);

        ContentValues contentValues = mapToContentValues(values);
        contentValues.put("blLocalTimestamp", System.currentTimeMillis() / 1000);
        contentValues.put("___class", table);

        long rowId = db.insert(table, null, contentValues);
        if (rowId == -1) throw new SQLiteException("Could not insert the row into the table " + table);
        List<Map> result = select(table, null, "rowid = " + rowId,
            null, null, null, null, "1");
        if (result.size() ==  0) throw new SQLiteException("Could not find the '" + table + "' entry with row id: " + rowId);
        return result.get(0);
    }

    @Override
    public Map update(String table, Map values, String whereClause, String[] whereArgs) {
        if (!tableExists(table)) throw new SQLiteException("Local table with name '" + table + "' doesn't exist");

        ContentValues contentValues = mapToContentValues(values);
        int updated = db.update(table, contentValues, whereClause, whereArgs);
        if (updated == 0) throw new SQLiteException("No rows were affected by the update operation");

        String objectId = (String) values.get("objectId");
        if (objectId != null) {
            List<Map> result = select(table, null, "objectId = '" + objectId + "'",
                null, null, null, null, null);
            if (result.size() ==  0) throw new SQLiteException("Could not find the '" + table + "' entry with objectId: " + objectId);
            return result.get(0);
        } else {
            throw new SQLiteException("Could not find the '" + table + "' entry without objectId");
        }
    }

    @Override
    public Map delete(String table, String whereClause, String[] whereArgs) {
        if (!tableExists(table)) throw new SQLiteException("Local table with name '" + table + "' doesn't exist");

        List<Map> result = select(table, null, whereClause,
            whereArgs, null, null, null, null);
        if (result.size() ==  0) throw new SQLiteException("Could not find the '" + table + "' entry where " + whereClause);
        Map entry = result.get(0);

        int deleted = db.delete(table, whereClause, whereArgs);
        if (deleted == 0) throw new SQLiteException("No rows were affected by the delete operation");
        return entry;
    }

    @Override
    public boolean isTableEmpty(String table) {
        if (!tableExists(table)) return true;
        String query = "SELECT count(*) FROM " + table;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        return count == 0;
    }

    @Override
    public void dropTable(String tableName) {
        String dropQuery = "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(dropQuery);
    }

    @Override
    public void resetDatabase() {
        helper.close();
        db.close();
        SQLiteDatabase.deleteDatabase(new File(db.getPath()));
        initHelper();
    }

    private void initHelper() {
        helper = new SQLiteOpenHelper(
            ContextHandler.getAppContext(), databaseName, null, databaseVersion) {

            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };

        db = helper.getWritableDatabase();
    }

    private boolean tableExists(String table) {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + table + "'";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    private void createTableIfNotExists(String table, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table + " (");

        for (Iterator<Map.Entry<String, String>> it = sdkTypes.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = it.next();
            sql.append(entry.getKey());
            sql.append(' ');
            sql.append(entry.getValue());

            if (it.hasNext()) {
                sql.append(", ");
            }
        }

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (!sdkTypes.containsKey(entry.getKey())) {
                sql.append(getColumnDeclaration(entry.getKey(), entry.getValue()));
            }
        }
        sql.append(')');

        db.execSQL(sql.toString());
    }

    private static Map<String, String> sdkTypes = new HashMap<String, String>() {{
        put("blLocalId", "INTEGER PRIMARY KEY AUTOINCREMENT");
        put("blPendingOperation", "INTEGER");
        put("blLocalTimestamp", "INTEGER");
        put("created", "INTEGER");
        put("updated", "INTEGER");
        put("___class", "TEXT");
        put("objectId", "TEXT");
        put("ownerId", "TEXT");
    }};



}
