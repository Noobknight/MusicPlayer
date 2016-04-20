package com.tadev.musicplayer.utils.support;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.constant.MusicTypeEnum;
import com.tadev.musicplayer.models.MusicOffline;

import java.util.ArrayList;

/**
 * Created by Iris Louis on 17/04/2016.
 */
public class MusicUtils {

    public static ArrayList<MusicOffline> scanMusic(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission((Activity) context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission((Activity) context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            } else {
                ArrayList<MusicOffline> mMusics = new ArrayList<>();
                Cursor cursor = context.getContentResolver().query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                if (cursor == null) {
                    return null;
                }
                while (cursor.moveToNext()) {
                    int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                    if (isMusic == 0) {
                        continue;
                    }
                    long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String unknown = context.getString(R.string.unknown);
                    artist = artist.equals("<unknown>") ? unknown : artist;
                    String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String coverUri = getCoverUri(context, albumId);
                    String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                    MusicOffline music = new MusicOffline();
                    music.setId(id);
                    music.setType(MusicTypeEnum.LOCAL);
                    music.setTitle(title);
                    music.setArtist(artist);
                    music.setAlbum(album);
                    music.setDuration(duration);
                    music.setUri(url);
                    music.setCoverUri(coverUri);
                    music.setFileName(fileName);
                    mMusics.add(music);
                }
                cursor.close();
                return mMusics;
            }
        } else {
            ArrayList<MusicOffline> mMusics = new ArrayList<>();
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if (cursor == null) {
                return null;
            }
            while (cursor.moveToNext()) {
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if (isMusic == 0) {
                    continue;
                }
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String unknown = context.getString(R.string.unknown);
                artist = artist.equals("<unknown>") ? unknown : artist;
                String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String coverUri = getCoverUri(context, albumId);
                String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                MusicOffline music = new MusicOffline();
                music.setId(id);
                music.setType(MusicTypeEnum.LOCAL);
                music.setTitle(title);
                music.setArtist(artist);
                music.setAlbum(album);
                music.setDuration(duration);
                music.setUri(url);
                music.setCoverUri(coverUri);
                music.setFileName(fileName);
                mMusics.add(music);
            }
            cursor.close();
            return mMusics;
        }
        return null;
    }

    private static String getCoverUri(Context context, long albumId) {
        String uri = null;
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/audio/albums/" + albumId),
                new String[]{"album_art"}, null, null, null);
        if (cursor != null) {
            cursor.moveToNext();
            uri = cursor.getString(0);
            cursor.close();
        }
        return uri;
    }

}
