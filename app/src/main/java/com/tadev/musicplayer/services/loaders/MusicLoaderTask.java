package com.tadev.musicplayer.services.loaders;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tadev.musicplayer.MusicPlayerApplication;
import com.tadev.musicplayer.constant.Constants;
import com.tadev.musicplayer.models.BaseModel;
import com.tadev.musicplayer.models.music.MusicKorea;
import com.tadev.musicplayer.models.music.MusicUSUK;
import com.tadev.musicplayer.models.music.MusicVietNam;
import com.tadev.musicplayer.utils.design.support.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iris Louis on 31/03/2016.
 */
public class MusicLoaderTask extends AsyncTask<String, Void, List<BaseModel>> {
    private final String TAG = "MusicLoaderTask";
    //Store Data
    private MusicPlayerApplication application;
    private String tag;

    //Method Callback
    public interface OnTaskLoading {
        void onTaskLoadCompleted(List<BaseModel> musics);

        void onTaskLoadFailed(Exception e);

        void onPreparing();
    }

    private OnTaskLoading mTaskLoading;

    public MusicLoaderTask(OnTaskLoading mTaskLoading, String tag) {
        this.mTaskLoading = mTaskLoading;
        application = MusicPlayerApplication.getInstance();
        this.tag = tag;
    }


    @Override
    protected void onPreExecute() {
        mTaskLoading.onPreparing();
    }


    @Override
    protected List<BaseModel> doInBackground(String... params) {
        final String KEY_MUSIC_HOT = "hot";
        final String KEY_OBJECT = "music";
        try {
            JSONObject response = JsonUtils.getJsonResponse(params[0]);
            JSONObject root = response.getJSONObject(KEY_MUSIC_HOT);
            JSONArray objects = root.getJSONArray(KEY_OBJECT);
            return toGsonBaseModel(objects.toString(), tag);
        } catch (IOException | JSONException e) {
            mTaskLoading.onTaskLoadFailed(e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<BaseModel> baseModels) {
        if (baseModels != null) {
            application.getMusicContainer().setBaseModelsMusic(baseModels);
            mTaskLoading.onTaskLoadCompleted(baseModels);
        }
    }

    private List<BaseModel> toGsonBaseModel(String jsonResponse, String tag) {
        Type listType = null;
        List<BaseModel> baseModels = application.getMusicContainer().getBaseModelsMusic();
        if (baseModels == null) {
            baseModels = new ArrayList<>();
        }
        switch (tag) {
            case Constants.VIETNAM_TAG:
                listType = new TypeToken<ArrayList<MusicVietNam>>() {
                }.getType();
                List<MusicVietNam> listMusicVN = new Gson().fromJson(jsonResponse, listType);
                baseModels.addAll(listMusicVN);
                break;
            case Constants.KOREA_TAG:
                listType = new TypeToken<ArrayList<MusicKorea>>() {
                }.getType();
                List<MusicKorea> listMusicKorea = new Gson().fromJson(jsonResponse, listType);
                baseModels.addAll(listMusicKorea);
                break;
            case Constants.US_TAG:
                listType = new TypeToken<ArrayList<MusicUSUK>>() {
                }.getType();
                List<MusicUSUK> listMusicUs = new Gson().fromJson(jsonResponse, listType);
                baseModels.addAll(listMusicUs);
                break;
        }
        return baseModels;
    }

}
