package com.tadev.musicplayer.services.loaders;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.tadev.musicplayer.common.Api;
import com.tadev.musicplayer.models.video.VideoInfo;
import com.tadev.musicplayer.utils.support.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Iris Louis on 15/04/2016.
 */
public class VideoInfoLoader extends AsyncTask<Void, Void, VideoInfo> {
    private final String TAG = "VideoInfoLoader";
    private String idVideo, urlTitle;
    private VideoInfoLoadListener mOnVideoInfoLoadListener;

    public interface VideoInfoLoadListener {
        void onPrepring();

        void onLoadCompleted(VideoInfo videoInfo);

        void onLoadFailed(Exception e);
    }


    public VideoInfoLoader(String idSong, String urlTitle,
                           VideoInfoLoadListener mOnVideoInfoLoadListener) {
        this.idVideo = idSong;
        this.urlTitle = urlTitle;
        this.mOnVideoInfoLoadListener = mOnVideoInfoLoadListener;
    }


    @Override
    protected VideoInfo doInBackground(Void... params) {
        final String KEY_OBJECT = "music_info";
        try {
            JSONObject response = JsonUtils.getJsonResponse(Api.getURLFindInfo(idVideo, urlTitle));
            JSONObject objects = response.getJSONObject(KEY_OBJECT);
            return toMusicGson(objects.toString());
        } catch (IOException | JSONException e) {
            mOnVideoInfoLoadListener.onLoadFailed(e);
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mOnVideoInfoLoadListener.onPrepring();
    }

    @Override
    protected void onPostExecute(VideoInfo videoInfo) {
        mOnVideoInfoLoadListener.onLoadCompleted(videoInfo);
    }

    private VideoInfo toMusicGson(String jsonResponse) {
        return new Gson().fromJson(jsonResponse, VideoInfo.class);
    }
}
