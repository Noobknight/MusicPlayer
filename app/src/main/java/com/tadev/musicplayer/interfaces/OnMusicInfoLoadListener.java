package com.tadev.musicplayer.interfaces;

import com.tadev.musicplayer.models.Music;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public interface OnMusicInfoLoadListener {
    void onTaskLoadCompleted(Music music);

    void onTaskLoadFailed(Exception e);

    void onPreparing();
}
