package com.tadev.musicplayer.helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.models.music.CurrentSongPlay;
import com.tadev.musicplayer.utils.design.support.StringUtils;
import com.tadev.musicplayer.utils.design.support.Utils;

/**
 * Created by Iris Louis on 08/04/2016.
 */
public class NotificationHelper {
    public static NotificationCompat.Builder from(Context context,
                                                  MediaSessionCompat mediaSession,
                                                  CurrentSongPlay currentSongPlay, Bitmap btm,
                                                  long timeWhen) {
        String title = currentSongPlay.song.getMusicTitle();
        String artist = currentSongPlay.song.getMusicArtist();
        Intent nowPlayingIntent = Utils.getNowPlayingIntent(context);
        PendingIntent clickIntent = PendingIntent.getActivity(context, 0, nowPlayingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(artist)
                .setLargeIcon(btm)
                .setWhen(timeWhen)
                .setContentIntent(clickIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.addAction(R.drawable.ic_pause_white_36dp, StringUtils.getStringRes(R.string.action_pause),
                getActionIntent(context, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
//        int stateButton = isPlaying ? R.drawable.ic_play_arrow_white_36dp : R.drawable.ic_pause_white_36dp;
//        builder.addAction(new NotificationCompat.Action(stateButton, "pause",
//                getActionIntent(context, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)));
        builder.setStyle(new NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0)
                .setShowCancelButton(true)
                .setCancelButtonIntent(NotificationHelper.getActionIntent(context, KeyEvent.KEYCODE_MEDIA_STOP)));
        return builder;
    }


    public static PendingIntent getActionIntent(Context context, int mediaKeyEvent) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.setPackage(context.getPackageName());
        intent.putExtra(Intent.EXTRA_KEY_EVENT,
                new KeyEvent(KeyEvent.ACTION_DOWN, mediaKeyEvent));
        return PendingIntent.getBroadcast(context, mediaKeyEvent, intent, 0);
    }
}
