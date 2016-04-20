package com.tadev.musicplayer.utils.networks.logger;

import android.util.Log;

/**
 * Created by Iris Louis on 20/04/2016.
 */
public final class NetworkEventsLogger implements Logger {
    private final static String TAG = "NetworkEvents";

    @Override public void log(String message) {
        Log.d(TAG, message);
    }
}