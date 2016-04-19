package com.tadev.musicplayer.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.tadev.musicplayer.abstracts.FavoriteDAO;
import com.tadev.musicplayer.models.SongFavorite;

import java.util.ArrayList;

/**
 * Created by Iris Louis on 03/10/2015.
 */
public class FavoriteDatabase extends FavoriteDAO {
    public FavoriteDatabase(Context context) {
        super(context);
    }

    @Override
    public void insertFavorite(SongFavorite songFavorite) {
        mDatabaseFavorite
                .execSQL(
                        "INSERT INTO " + DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC
                                + "(" + DatabaseConstant.DBConsFavorite.MUSIC_ID + ", "
                                + DatabaseConstant.DBConsFavorite.MUSIC_NAME + ", "
                                + DatabaseConstant.DBConsFavorite.MUSIC_URL_TITLE + ", "
                                + DatabaseConstant.DBConsFavorite.MUSIC_ARTIST + ", "
                                + DatabaseConstant.DBConsFavorite.MUSIC_IMAGE
                                + ") VALUES (?,?,?,?,?)",
                        new Object[]{songFavorite.getId(), songFavorite.getName()
                                , songFavorite.getUrlTitle(),
                                songFavorite.getArtist()
                                , songFavorite.getImage()});
    }

    @Override
    public boolean deleteFavorite(long rowId) {
        return mDatabaseFavorite.delete(DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC,
                BaseColumns._ID + "=" + rowId, null) > 0;
    }

    @Override
    public boolean deleteRowFarovite(String id) {
        return mDatabaseFavorite.delete(DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC,
                DatabaseConstant.DBConsFavorite.MUSIC_ID + "=" + id, null) > 0;
    }

    @Override
    public Cursor fetchFavorites() {
        return mDatabaseFavorite.query(DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC,
                new String[]{BaseColumns._ID, DatabaseConstant.DBConsFavorite.MUSIC_ID,
                        DatabaseConstant.DBConsFavorite.MUSIC_NAME,
                        DatabaseConstant.DBConsFavorite.MUSIC_URL_TITLE
                        , DatabaseConstant.DBConsFavorite.MUSIC_ARTIST
                        , DatabaseConstant.DBConsFavorite.MUSIC_IMAGE}, null, null, null,
                null, null);
    }

    @Override
    public Cursor fetchFavorite(long rowId) {
        Cursor mCursor = null;
        try {
            mCursor = mDatabaseFavorite.query(true,
                    DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC, new String[]{BaseColumns._ID,
                            DatabaseConstant.DBConsFavorite.MUSIC_ID,
                            DatabaseConstant.DBConsFavorite.MUSIC_NAME,
                            DatabaseConstant.DBConsFavorite.MUSIC_URL_TITLE,
                            DatabaseConstant.DBConsFavorite.MUSIC_ARTIST,
                            DatabaseConstant.DBConsFavorite.MUSIC_IMAGE}, BaseColumns._ID
                            + "=" + rowId, null, null, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
        } finally {
            mCursor.close();
        }
    }


    @Override
    public boolean updateFavorite(long rowId, SongFavorite songFavorite) {
        ContentValues args = new ContentValues();
        args.put(DatabaseConstant.DBConsFavorite.MUSIC_ID, songFavorite.getId());
        args.put(DatabaseConstant.DBConsFavorite.MUSIC_NAME, songFavorite.getName());
        args.put(DatabaseConstant.DBConsFavorite.MUSIC_URL_TITLE, songFavorite.getUrlTitle());
        args.put(DatabaseConstant.DBConsFavorite.MUSIC_ARTIST, songFavorite.getArtist());
        args.put(DatabaseConstant.DBConsFavorite.MUSIC_IMAGE, songFavorite.getImage());
        return mDatabaseFavorite.update(DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC, args,
                BaseColumns._ID + "=" + rowId, null) > 0;
    }

    @Override
    public ArrayList<SongFavorite> getSongFavorites() {
        Cursor cursor = null;
        ArrayList<SongFavorite> favorites = new ArrayList<SongFavorite>();
        try {
            cursor = mDatabaseFavorite.query(DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC,
                    new String[]{BaseColumns._ID, DatabaseConstant.DBConsFavorite.MUSIC_ID,
                            DatabaseConstant.DBConsFavorite.MUSIC_NAME,
                            DatabaseConstant.DBConsFavorite.MUSIC_URL_TITLE,
                            DatabaseConstant.DBConsFavorite.MUSIC_ARTIST, DatabaseConstant.DBConsFavorite.MUSIC_IMAGE
                    }, null, null,
                    null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    SongFavorite item = new SongFavorite();
                    item.setId(cursor.getString(1));
                    item.setName(cursor.getString(2));
                    item.setUrlTitle(cursor.getString(3));
                    item.setArtist(cursor.getString(4));
                    item.setImage(cursor.getString(5));
                    if (item != null) {
                        favorites.add(item);
                    }
                }
            }
            return favorites;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public boolean checkEvent(SongFavorite songFavorite) {
        Cursor cursor = mDatabaseFavorite
                .query(DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC,
                        new String[]{BaseColumns._ID, DatabaseConstant.DBConsFavorite.MUSIC_ID,
                                DatabaseConstant.DBConsFavorite.MUSIC_NAME
                        },
                        DatabaseConstant.DBConsFavorite.MUSIC_ID + " = ? and "
                                + DatabaseConstant.DBConsFavorite.MUSIC_NAME + " = ?",
                        new String[]{songFavorite.getId(), songFavorite.getName()
                        }, null, null, null, null);

        if (cursor.moveToFirst())
            return true; // row exists
        else
            return false;
    }

    @Override
    public SongFavorite getFavorite(long rowId) {
        SongFavorite item = new SongFavorite();
        item = cursorToFavorite(fetchFavorite(rowId));
        return item;
    }


    @Override
    public SongFavorite cursorToFavorite(Cursor cursor) {
        SongFavorite item = new SongFavorite();
        item.setId(cursor.getString(1));
        item.setName(cursor.getString(2));
        item.setUrlTitle(cursor.getString(3));
        item.setArtist(cursor.getString(4));
        item.setImage(cursor.getString(5));
        return item;
    }

    @Override
    public int getCount(SongFavorite item) {
        Cursor c = null;
        try {
            mDatabaseFavorite = dlFavoriteHelper.getReadableDatabase();
            String query = "select count(*) from "
                    + DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC + " where "
                    + DatabaseConstant.DBConsFavorite.MUSIC_ID + " = ?";
            c = mDatabaseFavorite.rawQuery(query, new String[]{item.getId()});
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
            return 0;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    @Override
    public void deleteAllRows() {
        mDatabaseFavorite.execSQL("DELETE FROM "
                + DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC);
    }

    @Override
    public SongFavorite getSongFavorite(String songId) {
        String select = "SELECT * FROM " + DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC + " " +
                "where id_music = \"" + songId + "\"";
        Cursor cursor = mDatabaseFavorite.rawQuery(select, null);
        SongFavorite item = new SongFavorite();
        if (cursor.moveToFirst()) {
            item.setId(cursor.getString(1));
            item.setName(cursor.getString(2));
            item.setUrlTitle(cursor.getString(3));
            item.setArtist(cursor.getString(4));
            item.setImage(cursor.getString(5));
            return item;
        } else {
            return null;
        }
    }

    @Override
    public boolean isExistFavorite(String id_story) {
        String query = "SELECT * FROM " + DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC + " " +
                "where id_music = \"" + id_story + "\"";
        Cursor cursor = mDatabaseFavorite.rawQuery(query, null);
        return cursor.getCount() > 0;
    }



    @Override
    public int getCountDB() {
        String query = "SELECT COUNT(*) FROM " + DatabaseConstant.DBConsFavorite.TB_FAVORITE_MUSIC;
        Cursor cursor = mDatabaseFavorite.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }

    @Override
    public void close() {
        dlFavoriteHelper.close();
    }

    @Override
    public SQLiteDatabase getWritetableDB() {
        return mDatabaseFavorite;
    }


}
