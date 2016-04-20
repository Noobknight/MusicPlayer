package com.tadev.musicplayer.services.loaders;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tadev.musicplayer.constant.Extras;
import com.tadev.musicplayer.models.video.Video;
import com.tadev.musicplayer.utils.support.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Iris Louis on 13/04/2016.
 */
public class VideoLoaderTask extends AsyncTask<String, Void, List<Video>> {
    private final String TAG = "VideoLoaderTask";


    public interface VideoLoader {
        void onLoadCompleted(List<Video> videos);
    }

    private VideoLoader mVideoLoader;
    private int typeGet;

    public VideoLoaderTask(VideoLoader mVideoLoader, int typeGet) {
        this.mVideoLoader = mVideoLoader;
        this.typeGet = typeGet;
    }

    @Override
    protected void onPreExecute() {
        onPreparing();
    }

    @Override
    protected List<Video> doInBackground(String... params) {
        String keyRoot = null;
        final String keyObject = "music";
        Log.i(TAG, "doInBackground " + typeGet);
        if (typeGet == Extras.TYPE_GET_NORMAL) {
            keyRoot = "hot";
        } else if (typeGet == Extras.TYPE_GET_MORE) {
            keyRoot = "new";
        }
        Log.i(TAG, "doInBackground " + keyRoot);
        try {
            JSONObject response = JsonUtils.getJsonResponse(params[0]);
            JSONObject root = response.getJSONObject(keyRoot);
            JSONArray objects = root.getJSONArray(keyObject);
            return toGsonVideos(objects.toString());
        } catch (IOException | JSONException e) {
            onLoadFailed(e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Video> videos) {
        mVideoLoader.onLoadCompleted(videos);
    }

    private List<Video> toGsonVideos(String jsonResponse) {
        Type listType = new TypeToken<List<Video>>() {
        }.getType();
        return new Gson().fromJson(jsonResponse, listType);
    }

    public void onLoadFailed(Exception e) {
    }

    public void onPreparing() {
    }
}
