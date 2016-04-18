package com.tadev.musicplayer.interfaces;

import com.tadev.musicplayer.models.SongFavorite;

import java.util.ArrayList;

/**
 * Created by Iris Louis on 17/04/2016.
 */
public interface RetrieveListener {
    void onRetriveCompleted(ArrayList<SongFavorite> results);

    void onRetriveFailed();
}
