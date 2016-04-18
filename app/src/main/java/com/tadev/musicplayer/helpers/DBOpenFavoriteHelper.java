package com.tadev.musicplayer.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tadev.musicplayer.provider.DatabaseConstant;


/**
 * Created by Iris Louis on 03/10/2015.
 */
public class DBOpenFavoriteHelper extends SQLiteOpenHelper{
    private static final String NAMEFILE_DB_FAVORITE = "FAVORITESONGS.db";
    private static final int DB_VERSION = 1;

    public DBOpenFavoriteHelper(Context context) {
        super(context, NAMEFILE_DB_FAVORITE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstant.DBConsFavorite.CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseConstant.DBConsFavorite.TB_MUSIC_FAVORITE_UPGRADE);
        onCreate(db);
    }
}
