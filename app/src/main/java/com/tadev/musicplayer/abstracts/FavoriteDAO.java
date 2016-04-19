package com.tadev.musicplayer.abstracts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tadev.musicplayer.helpers.DBOpenFavoriteHelper;
import com.tadev.musicplayer.models.SongFavorite;

import java.util.ArrayList;

/**
 * Created by Iris Louis on 03/10/2015.
 */
public abstract class FavoriteDAO {
    protected DBOpenFavoriteHelper dlFavoriteHelper;
    protected SQLiteDatabase mDatabaseFavorite;

    public FavoriteDAO(Context context) {
        dlFavoriteHelper = new DBOpenFavoriteHelper(context);
        mDatabaseFavorite = dlFavoriteHelper.getWritableDatabase();
    }

    public abstract void insertFavorite(SongFavorite songFavorite);

    public abstract boolean deleteFavorite(long rowId);

    public abstract boolean deleteRowFarovite(String id);

    public abstract Cursor fetchFavorites();

    public abstract Cursor fetchFavorite(long rowId);

    public abstract boolean updateFavorite(long rowId, SongFavorite songFavorite);

    public abstract ArrayList<SongFavorite> getSongFavorites();

    public abstract boolean checkEvent(SongFavorite songFavorite);

    public abstract SongFavorite getFavorite(long rowId);

    public abstract SongFavorite cursorToFavorite(Cursor cursor);

    public abstract int getCount(SongFavorite item);

    public abstract void deleteAllRows();

    public abstract SongFavorite getSongFavorite(String songId);

    public abstract boolean isExistFavorite(String songId);

    public abstract int getCountDB();

    public abstract void close();

    public abstract SQLiteDatabase getWritetableDB();


}
