package com.tadev.musicplayer.utils.networks.event;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.tadev.musicplayer.utils.networks.logger.Logger;

import java.util.List;

/**
 * Created by Iris Louis on 20/04/2016.
 */
public final class WifiSignalStrengthChanged {
    private static final String MESSAGE = "WifiSignalStrengthChanged";
    private Context context;

    public WifiSignalStrengthChanged(Logger logger, Context context) {
        this.context = context;
        logger.log(MESSAGE);
    }

    public List<ScanResult> getWifiScanResults() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getScanResults();
    }
}
