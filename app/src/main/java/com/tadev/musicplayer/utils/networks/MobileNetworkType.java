package com.tadev.musicplayer.utils.networks;

/**
 * Created by Iris Louis on 20/04/2016.
 */
public enum MobileNetworkType {
    UNKNOWN("unknown"),
    LTE("LTE"),
    HSPAP("HSPAP"),
    EDGE("EDGE"),
    GPRS("GPRS");

    private final String type;

    MobileNetworkType(String status) {
        this.type = status;
    }

    @Override public String toString() {
        return type;
    }
}
