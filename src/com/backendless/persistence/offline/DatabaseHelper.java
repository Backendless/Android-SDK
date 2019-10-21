package com.backendless.persistence.offline;

import android.content.ContentValues;
import android.database.Cursor;

import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.offline.visitor.gen.BackendlessQueryLexer;
import com.backendless.persistence.offline.visitor.gen.BackendlessQueryParser;
import com.backendless.persistence.offline.visitor.AndroidBackendlessQueryVisitor;
import com.backendless.persistence.offline.visitor.res.QueryPart;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {

    public static ContentValues mapToContentValues(Map<String, Object> map) {
        ContentValues contentValues = new ContentValues();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null)
                continue;

            if (value instanceof String)
                contentValues.put(key, (String) value);
            else if (value instanceof Byte)
                contentValues.put(key, (Byte) value);
            else if (value instanceof Short)
                contentValues.put(key, (Short) value);
            else if (value instanceof Integer)
                contentValues.put(key, (Integer) value);
            else if (value instanceof Long)
                contentValues.put(key, (Long) value);
            else if (value instanceof Float)
                contentValues.put(key, (Float) value);
            else if (value instanceof Double)
                contentValues.put(key, (Double) value);
            else if (value instanceof Boolean)
                contentValues.put(key, (Boolean) value);
            else if (value instanceof Date)
                contentValues.put(key, ((Date) value).getTime());
            else throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_ARGUMENT_EXCEPTION);
        }

        return contentValues;
    }

    public static List<Map> cursorToMap(Cursor cursor) {
        List<Map> result = new ArrayList<>();

        while (cursor.moveToNext()) {
            Map<String, Object> object = new HashMap<>();
            for (String columnName : cursor.getColumnNames()) {
                int columnIndex = cursor.getColumnIndex(columnName);

                int type = cursor.getType(columnIndex);
                switch (type) {
                    case Cursor.FIELD_TYPE_STRING:
                        object.put(columnName, cursor.getString(columnIndex));
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        object.put(columnName, cursor.getInt(columnIndex));
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        object.put(columnName, cursor.getFloat(columnIndex));
                        break;
                }
            }
            result.add(object);
        }

        cursor.close();

        return result;
    }

    public static String getColumnDeclaration(String columnName, Object columnValue) {
        StringBuilder declaration = new StringBuilder();

        declaration
            .append(", ")
            .append(columnName)
            .append(' ');

        if (columnValue instanceof String)
            declaration.append("TEXT");
        else if (columnValue instanceof Byte ||
                columnValue instanceof Short ||
                columnValue instanceof Integer ||
                columnValue instanceof Long ||
                columnValue instanceof Boolean)
            declaration.append("INTEGER");
        else if (columnValue instanceof Float ||
                columnValue instanceof Double)
            declaration.append("REAL");
        else if (columnValue == null)
            return "";
        else throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_ARGUMENT_EXCEPTION);


        return declaration.toString();
    }

    public static String parseClause(String whereClause) {
        BackendlessQueryLexer lexer = new BackendlessQueryLexer(CharStreams.fromString(whereClause));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        BackendlessQueryParser parser = new BackendlessQueryParser(tokenStream);

        BackendlessQueryParser.ConditionContext ctx = parser.condition();

        AndroidBackendlessQueryVisitor visitor = new AndroidBackendlessQueryVisitor();
        QueryPart visit = visitor.visit(ctx);

        return visit == null ? "" : visit.toString();
    }
}
