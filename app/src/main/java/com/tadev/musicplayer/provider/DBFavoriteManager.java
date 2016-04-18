package com.tadev.musicplayer.provider;

import android.content.Context;

import com.tadev.musicplayer.models.SongFavorite;

import java.util.ArrayList;

/**
 * Created by Iris Louis on 03/10/2015.
 */
public class DBFavoriteManager {
    private static DBFavoriteManager manager = null;
    private FavoriteDatabase mDatabase;

    public DBFavoriteManager(Context context) {
        mDatabase = new FavoriteDatabase(context);
    }

    public static DBFavoriteManager getInstance(Context context) {
        if (null == manager) {
            manager = new DBFavoriteManager(context);
        }
        return manager;
    }

    public synchronized void insertFavorite(SongFavorite item) {
        mDatabase.insertFavorite(item);
    }

    public synchronized boolean deleteFavorite(long rowID) {
        return mDatabase.deleteFavorite(rowID);
    }

    public synchronized boolean updateFavorite(long rowId,
                                               SongFavorite item) {
        return mDatabase.updateFavorite(rowId, item);
    }

    public synchronized ArrayList<SongFavorite> getFavorites() {
        return mDatabase.getSongFavorites();
    }

    public synchronized boolean checkInsertFavorite(SongFavorite item) {
        return mDatabase.checkEvent(item);
    }

    public synchronized SongFavorite getFavoriteByID(long rowId) {
        return mDatabase.getFavorite(rowId);
    }

    public synchronized SongFavorite getSongFavorite(String songId) {
        return mDatabase.getSongFavorite(songId);
    }

    public synchronized int getCountDB() {
        return mDatabase.getCountDB();
    }

    public synchronized int getCount(SongFavorite item) {
        return mDatabase.getCount(item);
    }

    public synchronized void deleteAllRows() {
        mDatabase.deleteAllRows();
    }

    public synchronized void close() {
        mDatabase.close();
    }

    public boolean isExistFavorite(SongFavorite item) {
        return mDatabase.isExistFavorite(item.getId());
    }


    public boolean isDeleteSucess(String songId) {
        return mDatabase.deleteRowFarovite(songId);
    }
}
