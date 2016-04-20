package com.tadev.musicplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.tadev.musicplayer.services.MusicPlayService;
import com.tadev.musicplayer.utils.actions.Actions;

/**
 * Created by Iris Louis on 08/04/2016.
 */
public class RemoteControlReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null || event.getAction() != KeyEvent.ACTION_UP) {
            return;
        }
        Intent serviceIntent = null;
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                serviceIntent = new Intent(context, MusicPlayService.class);
                serviceIntent.setAction(Actions.ACTION_PLAY);
                break;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                serviceIntent = new Intent(context, MusicPlayService.class);
                serviceIntent.setAction(Actions.ACTION_PAUSE);
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                serviceIntent = new Intent(context, MusicPlayService.class);
                serviceIntent.setAction(Actions.ACTION_PLAY_PAUSE);
                break;
            case KeyEvent.KEYCODE_HEADSETHOOK:
                serviceIntent = new Intent(context, MusicPlayService.class);
                serviceIntent.setAction(Actions.ACTION_TOGGLE);
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                serviceIntent = new Intent(context, MusicPlayService.class);
                serviceIntent.setAction(Actions.ACTION_NEXT);
                break;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                serviceIntent = new Intent(context, MusicPlayService.class);
                serviceIntent.setAction(Actions.ACTION_PREVIOUS);
                break;
        }
        context.startService(serviceIntent);
    }

}
