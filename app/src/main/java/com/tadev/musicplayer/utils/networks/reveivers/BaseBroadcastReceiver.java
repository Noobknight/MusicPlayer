package com.tadev.musicplayer.utils.networks.reveivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.tadev.musicplayer.utils.networks.BusWrapper;
import com.tadev.musicplayer.utils.networks.ConnectivityStatus;
import com.tadev.musicplayer.utils.networks.NetworkState;
import com.tadev.musicplayer.utils.networks.event.ConnectivityChanged;
import com.tadev.musicplayer.utils.networks.logger.Logger;


/**
 * Created by Iris Louis on 20/04/2016.
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final BusWrapper busWrapper;
    private final Context context;
    protected final Logger logger;

    public BaseBroadcastReceiver(BusWrapper busWrapper, Logger logger, Context context) {
        this.busWrapper = busWrapper;
        this.logger = logger;
        this.context = context;
    }

    @Override public abstract void onReceive(Context context, Intent intent);

    protected boolean statusNotChanged(ConnectivityStatus connectivityStatus) {
        return NetworkState.status == connectivityStatus;
    }

    protected void postConnectivityChanged(ConnectivityStatus connectivityStatus) {
        NetworkState.status = connectivityStatus;
        postFromAnyThread(new ConnectivityChanged(connectivityStatus, logger, context));
    }

    protected void postFromAnyThread(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            busWrapper.post(event);
        } else {
            handler.post(new Runnable() {
                @Override public void run() {
                    busWrapper.post(event);
                }
            });
        }
    }
}