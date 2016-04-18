package com.tadev.musicplayer.provider;

import android.provider.BaseColumns;

/**
 * Created by Iris Louis on 17/04/2016.
 */
public class DatabaseConstant {
    public static class DBConsFavorite {
        public static final String MUSIC_ID = "id_music";
        public static final String MUSIC_URL_TITLE = "url_title";
        public static final String MUSIC_NAME = "name";
        public static final String MUSIC_IMAGE = "image";
        public static final String MUSIC_ARTIST = "artist";
        public static final String TB_FAVORITE_MUSIC = "SONGSFAVORITE";
        public static final String CREATE_FAVORITE_TABLE = "create table if not exists "
                + TB_FAVORITE_MUSIC
                + "("
                + BaseColumns._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MUSIC_ID
                + " TEXT NOT NULL, "
                + MUSIC_NAME
                + " TEXT, "
                + MUSIC_URL_TITLE
                + " TEXT NOT NULL, "
                + MUSIC_ARTIST
                + " TEXT, "
                + MUSIC_IMAGE
                + " TEXT);";

        public static final String TB_MUSIC_FAVORITE_UPGRADE = "DROP TABLE IF EXISTS "
                + DBConsFavorite.TB_FAVORITE_MUSIC;
    }
}
