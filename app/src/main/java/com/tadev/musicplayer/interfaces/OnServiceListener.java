package com.tadev.musicplayer.interfaces;

import com.tadev.musicplayer.models.music.CurrentSongPlay;

/**
 * Created by Iris Louis on 08/04/2016.
 */
public interface OnServiceListener {

    void duration(int duration);

    void position(int currentPosition);

    void currentID(int currentId);

    void onChange(int musicId);

    void onPublish(int progress);

    void onChangeSongPlay(CurrentSongPlay currentSongPlay);

}
