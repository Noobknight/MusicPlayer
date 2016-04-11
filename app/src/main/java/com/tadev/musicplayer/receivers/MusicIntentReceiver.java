package com.tadev.musicplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.tadev.musicplayer.services.PlayService;
import com.tadev.musicplayer.utils.design.actions.Actions;

/**
 * Created by Iris Louis on 08/04/2016.
 */
public class MusicIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(
                android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {

            Intent intent1 = new Intent(Actions.ACTION_PAUSE);
            intent1.setClass(context,
                    PlayService.class);
            // send an intent to our MusicService to telling it to pause the
            // audio
            context.startService(intent1);

        } else if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {

            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(
                    Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;

            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    Intent intentToggle = new Intent(
                            Actions.ACTION_TOGGLE);
                    intentToggle.setClass(context,
                            PlayService.class);
                    context.startService(intentToggle);
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    Intent intentPlay = new Intent(Actions.ACTION_PLAY);
                    intentPlay.setClass(context,
                            PlayService.class);
                    context.startService(intentPlay);
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    Intent intentPause = new Intent(Actions.ACTION_PAUSE);
                    intentPause.setClass(context,
                            PlayService.class);
                    context.startService(intentPause);

                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Intent intentNext = new Intent(Actions.ACTION_NEXT);
                    intentNext.setClass(context,
                            PlayService.class);
                    context.startService(intentNext);
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Intent intentPrev = new Intent(Actions.ACTION_PREVIOUS);
                    intentPrev.setClass(context,
                            PlayService.class);
                    context.startService(intentPrev);
                    break;
                default:
                    break;
            }
        }
    }
}
