package com.backendless.persistence.offline;

import java.util.List;

public interface TransactionStorage {

    void put(Transaction transaction);

    List<Transaction> get();

    void remove(Transaction transaction);
}
