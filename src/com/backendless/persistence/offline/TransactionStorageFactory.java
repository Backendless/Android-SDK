package com.backendless.persistence.offline;


import android.content.Context;

import com.backendless.Backendless;
import com.backendless.exceptions.ExceptionMessage;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TransactionStorageFactory
{
    private static AndroidTransactionStorage androidTransactionStorage;
    private static TransactionStorageFactory instance = new TransactionStorageFactory();

    public static TransactionStorageFactory instance()
    {
        return instance;
    }

    private TransactionStorageFactory()
    {
    }

    public void init( Context context )
    {
        if (androidTransactionStorage == null)
            androidTransactionStorage = new AndroidTransactionStorage( context );
    }

    public TransactionStorage getStorage()
    {
        if( Backendless.isAndroid() && androidTransactionStorage == null )
            throw new IllegalArgumentException( ExceptionMessage.INIT_BEFORE_USE );

        if( Backendless.isAndroid() )
            return androidTransactionStorage;

        // CodeRunner
        if( Backendless.isCodeRunner() )
            throw new NotImplementedException();

        // Java
        throw new NotImplementedException();
    }
}
