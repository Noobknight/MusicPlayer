package com.tadev.musicplayer.interfaces;

import com.tadev.musicplayer.models.CurrentSongPlay;

/**
 * Created by Iris Louis on 05/04/2016.
 */
public interface IServicePlayer extends OnMusicPlayListener {

    void duration(int duration);

    void position(int currentPosition);

    void currentID(int currentId);

    @Override
    void onChange(int musicId);

    @Override
    void onPublish(int progress);

    void currentSongPlay(CurrentSongPlay currentSongPlay);
}
