package com.tadev.musicplayer.services.loaders;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.tadev.musicplayer.common.Api;
import com.tadev.musicplayer.interfaces.OnMusicInfoLoadListener;
import com.tadev.musicplayer.models.music.Music;
import com.tadev.musicplayer.utils.design.support.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownServiceException;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class MusicInfoLoaderTask extends AsyncTask<Void, Void, Music> {
    private final String TAG = "MusicInfoLoaderTask";
    private String idSong, urlTitle;
    private OnMusicInfoLoadListener mOnMusicInfoLoadListener;

    public MusicInfoLoaderTask(String idSong, String urlTitle,
                               OnMusicInfoLoadListener mOnMusicInfoLoadListener) {
        this.idSong = idSong;
        this.urlTitle = urlTitle;
        this.mOnMusicInfoLoadListener = mOnMusicInfoLoadListener;
    }


    @Override
    protected Music doInBackground(Void... params) {
        final String KEY_OBJECT = "music_info";
        try {
            JSONObject response = JsonUtils.getJsonResponse(Api.getURLFindInfo(idSong, urlTitle));
            if (response.toString().startsWith(Api.ERROR_MSG)) {
                mOnMusicInfoLoadListener.onTaskLoadFailed(new UnknownServiceException("File Not Found"));
                return null;
            }
            JSONObject objects = response.getJSONObject(KEY_OBJECT);

            return toMusicGson(objects.toString());
        } catch (IOException | JSONException e) {
            mOnMusicInfoLoadListener.onTaskLoadFailed(e);
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mOnMusicInfoLoadListener.onPreparing();
    }

    @Override
    protected void onPostExecute(Music music) {
        mOnMusicInfoLoadListener.onTaskLoadCompleted(music);
    }

    private Music toMusicGson(String jsonResponse) {
        return new Gson().fromJson(jsonResponse, Music.class);
    }
}
