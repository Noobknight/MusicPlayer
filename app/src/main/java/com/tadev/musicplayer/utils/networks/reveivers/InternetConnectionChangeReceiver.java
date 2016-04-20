package com.tadev.musicplayer.utils.networks.reveivers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tadev.musicplayer.utils.networks.BusWrapper;
import com.tadev.musicplayer.utils.networks.ConnectivityStatus;
import com.tadev.musicplayer.utils.networks.logger.Logger;


/**
 * Created by Iris Louis on 20/04/2016.
 */
public final class InternetConnectionChangeReceiver extends BaseBroadcastReceiver {
    public final static String INTENT =
            "networkevents.intent.action.INTERNET_CONNECTION_STATE_CHANGED";
    public final static String INTENT_EXTRA = "networkevents.intent.extra.CONNECTED_TO_INTERNET";

    public InternetConnectionChangeReceiver(BusWrapper busWrapper, Logger logger, Context context) {
        super(busWrapper, logger, context);
    }

    @Override public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(INTENT)) {
            boolean connectedToInternet = intent.getBooleanExtra(INTENT_EXTRA, false);
            onPostReceive(connectedToInternet, context);
        }
    }

    public void onPostReceive(boolean connectedToInternet, Context context) {
        ConnectivityStatus connectivityStatus =
                (connectedToInternet) ? ConnectivityStatus.WIFI_CONNECTED_HAS_INTERNET
                        : ConnectivityStatus.WIFI_CONNECTED_HAS_NO_INTERNET;

        if (statusNotChanged(connectivityStatus)) {
            return;
        }

        // we are checking if device is connected to WiFi again,
        // because connectivityStatus may change in a short period of time
        // after receiving it

        if (context != null && !isConnectedToWifi(context)) {
            return;
        }

        postConnectivityChanged(connectivityStatus);
    }

    private boolean isConnectedToWifi(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }

        return false;
    }
}