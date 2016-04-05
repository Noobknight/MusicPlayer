package com.tadev.musicplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by Iris Louis on 04/04/2016.
 */
public class UpdateSeekbarReceiver extends BroadcastReceiver {
    private final String TAG = "UpdateSeekbarReceiver";
    public static final String KEY_UPDATE_PROGRESS = "update_progress";
    public static final String KEY_CURRENT_POSITION = "current_position";
    private View view;

    public UpdateSeekbarReceiver() {
    }

    public UpdateSeekbarReceiver(View view) {
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
//            int progress = intent.getIntExtra("currentPosition", 0);
//            if (view instanceof SeekBar) {
//                ((SeekBar) view).setProgress(progress);
//            } else if (view instanceof ProgressBar) {
//                ((ProgressBar) view).setProgress(progress);
//            }
        }
    }
}
