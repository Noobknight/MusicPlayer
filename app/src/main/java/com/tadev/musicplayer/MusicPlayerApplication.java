package com.tadev.musicplayer;

import android.app.Application;

import com.tadev.musicplayer.metadata.MusicContainer;

/**
 * Created by Iris Louis on 30/03/2016.
 */
public class MusicPlayerApplication extends Application {
    private final String TAG = "MusicPlayerApplication";
    private MusicContainer musicContainer;
    private static MusicPlayerApplication sInstance;

    public static MusicPlayerApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sInstance.initMusicContainer();
    }

    private void initMusicContainer() {
        musicContainer = MusicContainer.getInstance();
    }

    public MusicContainer getMusicContainer() {
        return musicContainer;
    }
}
