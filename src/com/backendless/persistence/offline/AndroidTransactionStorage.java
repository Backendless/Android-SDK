package com.backendless.persistence.offline;

import android.content.Context;
import android.content.SharedPreferences;

import com.backendless.util.JSONConverter;
import com.backendless.utils.JSONConverterWeborbImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AndroidTransactionStorage implements TransactionStorage {
    private final static String PREF_TAG = "ENDLESS_TRANSACTION";
    private final static String TRANSACTIONS_KEY = "blPendingTransactions";
    private final SharedPreferences prefs;
    private final JSONConverter jsonConverter;

    public AndroidTransactionStorage(Context context) {
        prefs = context.getSharedPreferences(PREF_TAG, Context.MODE_PRIVATE);
        jsonConverter = new JSONConverterWeborbImpl();
    }

    @Override
    public void put(Transaction transaction) {
        List<Transaction> transactions = get();
        transactions.add(transaction);

        String json = jsonConverter.writeObject(transactions);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TRANSACTIONS_KEY, json);
        editor.apply();
    }

    @Override
    public List<Transaction> get() {
        String json = prefs.getString(TRANSACTIONS_KEY, "[]");

        Object[] objects = jsonConverter.readObject(json, Object[].class);
        List<Transaction> transactions = new ArrayList<>();
        for (Object object : objects) {
            Map map = (Map) object;
            Transaction transaction = new Transaction(
                (String) map.get("methodName"),
                (Object[]) map.get("args"),
                (String) map.get("id"),
                (String) map.get("className"));
            transactions.add(transaction);
        }
        return transactions;
    }

    @Override
    public void remove(Transaction transactionToRemove) {
        List<Transaction> transactions = get();
        int removeIndex = -1;

        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(transactionToRemove.getId())) {
                removeIndex = i;
                break;
            }
        }
        if (removeIndex != -1) {
            transactions.remove(removeIndex);
            String json = jsonConverter.writeObject(transactions);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(TRANSACTIONS_KEY, json);
            editor.apply();
        }
    }
}
