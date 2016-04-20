package com.tadev.musicplayer.utils.networks.reveivers;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.tadev.musicplayer.utils.networks.BusWrapper;
import com.tadev.musicplayer.utils.networks.event.WifiSignalStrengthChanged;
import com.tadev.musicplayer.utils.networks.logger.Logger;


/**
 * Created by Iris Louis on 20/04/2016.
 */
public final class WifiSignalStrengthChangeReceiver extends BaseBroadcastReceiver {
    private Context context;

    public WifiSignalStrengthChangeReceiver(BusWrapper busWrapper, Logger logger, Context context) {
        super(busWrapper, logger, context);
        this.context = context;
    }

    @Override public void onReceive(Context context, Intent intent) {
        // We need to start WiFi scan after receiving an Intent
        // in order to get update with fresh data as soon as possible
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        onPostReceive();
    }

    public void onPostReceive() {
        postFromAnyThread(new WifiSignalStrengthChanged(logger, context));
    }
}