package com.backendless.persistence.offline;

import java.util.List;
import java.util.Map;

public interface DatabaseManager {

    List<Map> select(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);

    Map insert(String table, Map values);

    Map update(String table, Map values, String whereClause, String[] whereArgs);

    Map delete(String table, String whereClause, String[] whereArgs);

    boolean isTableEmpty(String table);

    void dropTable(String tableName);

    void resetDatabase();
}
