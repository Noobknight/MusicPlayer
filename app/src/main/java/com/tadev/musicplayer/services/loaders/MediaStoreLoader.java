package com.tadev.musicplayer.services.loaders;

import android.content.Context;
import android.os.AsyncTask;

import com.tadev.musicplayer.models.MusicOffline;
import com.tadev.musicplayer.utils.support.MusicUtils;

import java.util.ArrayList;

/**
 * Created by Iris Louis on 17/04/2016.
 */
public class MediaStoreLoader extends AsyncTask<Void, Void, ArrayList<MusicOffline>> {
    private Context context;
    private StoreLoaderListener mLoaderListener;

    public MediaStoreLoader(Context context, StoreLoaderListener mLoaderListener) {
        this.context = context;
        this.mLoaderListener = mLoaderListener;
    }

    public interface StoreLoaderListener {
        void onLoadCompleted(ArrayList<MusicOffline> results);

        void onLoadFailed();

        void onPreparing();
    }

    @Override
    protected void onPreExecute() {
        mLoaderListener.onPreparing();
    }

    @Override
    protected ArrayList<MusicOffline> doInBackground(Void... params) {
        return MusicUtils.scanMusic(context);
    }

    @Override
    protected void onPostExecute(ArrayList<MusicOffline> musicOfflines) {
        if (musicOfflines != null) {
            mLoaderListener.onLoadCompleted(musicOfflines);
        } else {
            mLoaderListener.onLoadFailed();
        }
    }


}
