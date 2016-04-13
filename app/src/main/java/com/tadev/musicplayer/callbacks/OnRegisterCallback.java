package com.tadev.musicplayer.callbacks;

import android.content.Intent;
import android.content.ServiceConnection;

/**
 * Created by Iris Louis on 05/04/2016.
 */
public interface OnRegisterCallback {
    void onServiceRegister(Intent intent, ServiceConnection mServiceConnection);

    void onServicePreparing(Intent intent);

    void onUnRegisterReceiver(boolean enable);

    void onDownloadRegister(Intent intent);

}
