package com.tadev.musicplayer.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.interfaces.OnBackFragmentListener;
import com.tadev.musicplayer.models.video.VideoInfo;
import com.tadev.musicplayer.services.loaders.VideoInfoLoader;
import com.tadev.musicplayer.utils.design.support.StringUtils;
import com.tadev.musicplayer.utils.design.support.Utils;

/**
 * Created by Iris Louis on 15/04/2016.
 */
public class VideoPlayingFragment extends BaseFragment implements
        VideoInfoLoader.VideoInfoLoadListener {
    public static final String TAG = "VideoPlayingFragment";
    private static final String KEY_ID = "_id";
    private static final String KEY_URL_TITLE = "url_title";
    private VideoInfo videoInfo;
    private LinearLayout viewLoading, videoView;
    private OnBackFragmentListener mOnBackFragmentListener;
    private Toolbar toolbar;
    private VideoView mVideoView;
    private MediaController mMediaController;


    public static VideoPlayingFragment newInstance(String idVideo, String urlTitle) {
        VideoPlayingFragment videoPlayingFragment = new VideoPlayingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, idVideo);
        bundle.putString(KEY_URL_TITLE, urlTitle);
        videoPlayingFragment.setArguments(bundle);
        return videoPlayingFragment;
    }


    @Override
    protected int setLayoutById() {
        return R.layout.fragment_playing_video;
    }

    @Override
    protected void initView(View view) {
        String[] data = getDataSent();
        viewLoading = (LinearLayout) view.findViewById(R.id.fragment_playing_video_loading);
        videoView = (LinearLayout) view.findViewById(R.id.lrl_videoView);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mVideoView = (VideoView) view.findViewById(R.id.fragment_playing_video_videoView);
        initToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOnBackFragmentListener.onBack(true);
                Handler handler = new Handler();
                baseMenuActivity.mFragmentManager.popBackStack();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        baseMenuActivity.getToolbar().setVisibility(View.VISIBLE);
                    }
                }, getResources().getInteger(R.integer.fragmentAnimationTime));
            }
        });
        mMediaController = new MediaController(context);
        mMediaController.setAnchorView(mVideoView);
        new VideoInfoLoader(data[0], data[1], this).execute();
    }

    @Override
    protected void getSaveInstanceState(Bundle savedInstanceState) {
        super.getSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void initViewData() {

    }


    @Override
    protected void setViewEvents() {
    }

    private void initToolbar() {
        if (toolbar != null) {
            baseMenuActivity.setSupportActionBar(toolbar);
            ActionBar actionBar = baseMenuActivity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            toolbar.setTitle(StringUtils.getStringRes(R.string.category_vietnam)
                    .replace("Nhạc", "Video"));
            toolbar.setBackgroundColor(Utils.getColorRes(R.color.colorPrimary));
        }
    }


    private String[] getDataSent() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            return new String[]{bundle.getString(KEY_ID), bundle.getString(KEY_URL_TITLE)};
        }
        return null;
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    public void onPrepring() {

    }

    @Override
    public void onLoadCompleted(VideoInfo resultCallBack) {
        if (resultCallBack != null) {
            videoInfo = resultCallBack;
//            Uri uri = Uri.parse(resultCallBack.getFileUrl());
            Log.i(TAG, "onLoadCompleted " + resultCallBack.getFileUrl());
            mVideoView.setMediaController(mMediaController);
            mVideoView.setVideoPath(resultCallBack.getFileUrl());
            mVideoView.requestFocus();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVideoView.start();
                }
            });

        }
        viewLoading.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadFailed(Exception e) {
//        viewLoading.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        AppCompatActivity activity;
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
            try {
                mOnBackFragmentListener = (OnBackFragmentListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " class must be implement" +
                        " OnBackFragmentListener");
            }
        }
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button
                    mOnBackFragmentListener.onBack(true);
                    Handler handler = new Handler();
                    baseMenuActivity.mFragmentManager.popBackStack();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            baseMenuActivity.getToolbar().setVisibility(View.VISIBLE);
                        }
                    }, getResources().getInteger(R.integer.fragmentAnimationTime));
                    return true;

                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
