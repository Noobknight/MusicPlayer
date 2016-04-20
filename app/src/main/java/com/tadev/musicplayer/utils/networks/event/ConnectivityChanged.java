package com.tadev.musicplayer.utils.networks.event;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.tadev.musicplayer.utils.networks.ConnectivityStatus;
import com.tadev.musicplayer.utils.networks.MobileNetworkType;
import com.tadev.musicplayer.utils.networks.logger.Logger;

/**
 * Created by Iris Louis on 20/04/2016.
 */
public final class ConnectivityChanged {
    private static final String MESSAGE_FORMAT = "ConnectivityChanged: %s";
    private final ConnectivityStatus connectivityStatus;
    private final Context context;

    public ConnectivityChanged(ConnectivityStatus connectivityStatus, Logger logger,
                               Context context) {
        this.connectivityStatus = connectivityStatus;
        this.context = context;
        String message = String.format(MESSAGE_FORMAT, connectivityStatus.toString());
        logger.log(message);
    }

    public ConnectivityStatus getConnectivityStatus() {
        return connectivityStatus;
    }

    public MobileNetworkType getMobileNetworkType() {

        if (connectivityStatus != ConnectivityStatus.MOBILE_CONNECTED) {
            return MobileNetworkType.UNKNOWN;
        }

        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case (TelephonyManager.NETWORK_TYPE_LTE):
                return MobileNetworkType.LTE;
            case (TelephonyManager.NETWORK_TYPE_HSPAP):
                return MobileNetworkType.HSPAP;
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                return MobileNetworkType.EDGE;
            case (TelephonyManager.NETWORK_TYPE_GPRS):
                return MobileNetworkType.GPRS;
            default:
                return MobileNetworkType.UNKNOWN;
        }
    }
}
