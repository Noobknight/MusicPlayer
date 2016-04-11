package com.tadev.musicplayer.interfaces;

import com.tadev.musicplayer.models.CurrentSongPlay;

/**
 * Created by Iris Louis on 08/04/2016.
 */
public interface PlayBack {


    void initPlayBack();

    void start();

    void stop();


    void play(String url);

    void pause();

    boolean isPlaying();

    boolean isConnected();

    int getCurrentStreamPosition();

    int getDuration();

    void seekTo(int where);

    CurrentSongPlay getCurrentSongPlay();

    int getCurrentSongId();

    void createMediaPlayer();

    void playOrPause();
}
