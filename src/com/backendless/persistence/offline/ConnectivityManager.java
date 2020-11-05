package com.backendless.persistence.offline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;

import com.backendless.ContextHandler;

public class ConnectivityManager {
    private final static ConnectivityManager instance = new ConnectivityManager();
    private final SyncManager syncManager;
    private boolean isConnected;

    public static ConnectivityManager getInstance() {
        return instance;
    }

    private ConnectivityManager() {
        syncManager = SyncManager.getInstance();
        registerReceiver();
    }

    private void registerReceiver() {
        BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isConnected = isNetworkAvailable(context);
                if (isConnected) syncManager.startAutoSynchronization();
            }
        };
        IntentFilter filter = new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        ContextHandler.getAppContext().registerReceiver(networkStateReceiver, filter);
    }

    private boolean isNetworkAvailable(Context context) {
        android.net.ConnectivityManager cm = (android.net.ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return activeNetwork.getState() == NetworkInfo.State.CONNECTED;
        }
        return false;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
