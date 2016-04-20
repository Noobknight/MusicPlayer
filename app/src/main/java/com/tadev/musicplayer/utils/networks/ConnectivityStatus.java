package com.tadev.musicplayer.utils.networks;

/**
 * Created by Iris Louis on 20/04/2016.
 */
public enum ConnectivityStatus {
    UNKNOWN("unknown"),
    WIFI_CONNECTED("connected to WiFi"),
    WIFI_CONNECTED_HAS_INTERNET("connected to WiFi (Internet available)"),
    WIFI_CONNECTED_HAS_NO_INTERNET("connected to WiFi (Internet not available)"),
    MOBILE_CONNECTED("connected to mobile network"),
    OFFLINE("offline");

    private final String status;

    ConnectivityStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
