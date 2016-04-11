package com.tadev.musicplayer.playback;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tadev.musicplayer.interfaces.PlayBack;
import com.tadev.musicplayer.models.CurrentSongPlay;

/**
 * Created by Iris Louis on 08/04/2016.
 */
public class PlayBackManager extends Service implements
        PlayBack, AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnPreparedListener {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private Context context;
    private boolean isPrepared;

    public PlayBackManager(Context context) {
        this.context = context;
    }

    @Override
    public void initPlayBack() {
        createMediaPlayer();
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    }

    @Override
    public void start() {
        if (mMediaPlayer != null && isPrepared) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void play(String url) {

    }

    @Override
    public void pause() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public int getCurrentStreamPosition() {
        return 0;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public void seekTo(int where) {

    }

    @Override
    public CurrentSongPlay getCurrentSongPlay() {
        return null;
    }

    @Override
    public int getCurrentSongId() {
        return 0;
    }

    @Override
    public void createMediaPlayer() {
        if (mMediaPlayer != null) {
            return;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
    }

    @Override
    public void playOrPause() {

    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
