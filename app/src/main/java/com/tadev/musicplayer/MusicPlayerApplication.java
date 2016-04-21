package com.tadev.musicplayer;

import android.app.Application;
import android.util.Log;

import com.tadev.musicplayer.helpers.LocaleHelper;
import com.tadev.musicplayer.interfaces.OnLanguageChangeListener;
import com.tadev.musicplayer.metadata.MusicContainer;
import com.tadev.musicplayer.provider.DBFavoriteManager;
import com.tadev.musicplayer.utils.support.StringUtils;
import com.tadev.musicplayer.utils.support.ToastUtils;
import com.tadev.musicplayer.utils.support.Utils;

import java.util.Locale;

/**
 * Created by Iris Louis on 30/03/2016.
 */
public class MusicPlayerApplication extends Application implements OnLanguageChangeListener {
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
        Log.i(TAG, "onCreate ");
        sInstance = this;
        sInstance.initMusicContainer();
        dbFavoriteManager = DBFavoriteManager.getInstance(sInstance);
        StringUtils.init(this);
        Utils.init(this);
        String language = Locale.getDefault().getLanguage();
        LocaleHelper.onCreate(this, language);
        ToastUtils.init(this);
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

    @Override
    public void onLanguageChanged() {
    }
}
