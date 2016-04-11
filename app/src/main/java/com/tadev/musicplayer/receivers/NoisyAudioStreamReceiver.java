package com.tadev.musicplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tadev.musicplayer.services.PlayService;
import com.tadev.musicplayer.utils.design.actions.Actions;

/**
 * Created by Iris Louis on 08/04/2016.
 */
public class NoisyAudioStreamReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, PlayService.class);
        serviceIntent.setAction(Actions.ACTION_TOGGLE);
        context.startService(serviceIntent);
    }
}
