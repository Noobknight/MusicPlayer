package com.tadev.musicplayer;

import android.app.Application;

import com.tadev.musicplayer.metadata.MusicContainer;
import com.tadev.musicplayer.provider.DBFavoriteManager;
import com.tadev.musicplayer.utils.design.support.StringUtils;
import com.tadev.musicplayer.utils.design.support.Utils;

/**
 * Created by Iris Louis on 30/03/2016.
 */
public class MusicPlayerApplication extends Application {
    private final String TAG = "MusicPlayerApplication";
    private MusicContainer musicContainer;
    private DBFavoriteManager dbFavoriteManager;
    private static MusicPlayerApplication sInstance;

    public static MusicPlayerApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sInstance.initMusicContainer();
        dbFavoriteManager = DBFavoriteManager.getInstance(sInstance);
        StringUtils.init(this);
        Utils.init(this);
    }

    private void initMusicContainer() {
        musicContainer = MusicContainer.getInstance();
    }

    public MusicContainer getMusicContainer() {
        return musicContainer;
    }

    public DBFavoriteManager getDatabaseFavorite() {
        return dbFavoriteManager;
    }

}
