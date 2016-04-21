package com.tadev.musicplayer.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseActivity;
import com.tadev.musicplayer.fragments.VideoContainerFragment;
import com.tadev.musicplayer.models.video.VideoInfo;
import com.tadev.musicplayer.services.loaders.VideoInfoLoader;
import com.tadev.musicplayer.supports.design.CustomVideoView;
import com.tadev.musicplayer.supports.design.statusbar.StatusBarCompat;
import com.tadev.musicplayer.utils.support.StringUtils;
import com.tadev.musicplayer.utils.support.Utils;

/**
 * Created by Iris Louis on 16/04/2016.
 */
public class VideoPlayingActivity extends BaseActivity implements VideoInfoLoader.VideoInfoLoadListener {
    private final String TAG = "VideoPlayingActivity";
    public static final String KEY_ID = "_id";
    public static final String KEY_URL_TITLE = "url_title";
    private final String KEY_POSITION = "position_video";
    private CustomVideoView mVideoView;
    private MediaController mMediaController;
    private VideoInfo mVideoInfo;
    private Toolbar toolbar;
    private ProgressBar mProgressBar;
    private LinearLayout viewLoading;
    private LinearLayout videoView;
    private FrameLayout flRoot;
    private FrameLayout.LayoutParams defaultVideoViewParams;
    private int defaultScreenOrientationMode;
    private TextView txtTitle, txtArtist, txtComposer, txtYear;
    private ImageView imgThumbnail;
    private TelephonyManager mgr;
    private CardView cardVideoDetail;


    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Utils.getColorRes(R.color.colorPrimary));
        String[] data = getDataSent();
        mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        viewLoading = (LinearLayout) findViewById(R.id.activity_video_playing_loading);
        videoView = (LinearLayout) findViewById(R.id.lrl_videoView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mVideoView = (CustomVideoView) findViewById(R.id.activity_video_playing_videoView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        flRoot = (FrameLayout) findViewById(R.id.activity_video_playing_flRoot);
        txtTitle = (TextView) findViewById(R.id.activity_video_playing_txtTitle);
        txtArtist = (TextView) findViewById(R.id.activity_video_playing_txtArtist);
        txtComposer = (TextView) findViewById(R.id.activity_video_playing_txtComposer);
        txtYear = (TextView) findViewById(R.id.activity_video_playing_txtYear);
        imgThumbnail = (ImageView) findViewById(R.id.activity_video_playing_imgThumbnail);
        cardVideoDetail = (CardView) findViewById(R.id.activity_video_playing_detail);
        mMediaController = new MediaController(this);
        mMediaController.setAnchorView(mVideoView);
        mVideoView.setEnabled(false);
        initToolbar();

        new VideoInfoLoader(data[0], data[1], this).execute();
    }

    @Override
    protected int setLayoutById() {
        return R.layout.activity_video_playing;
    }

    @Override
    protected void addEvents() {
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        txtArtist.setSelected(true);
        txtComposer.setSelected(true);
    }

    private void initToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(StringUtils.getStringRes(R.string.category_vietnam)
                    .replace("Nhạc", "Video"));
            toolbar.setBackgroundColor(Utils.getColorRes(R.color.colorPrimary));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doNavigationToBack();
                }
            });
        }
    }

    private void initDataViews() {
        if (mVideoInfo != null) {
            txtTitle.setText(mVideoInfo.getMusicTitle());
            txtArtist.append(mVideoInfo.getMusicArtist());
            txtComposer.append(mVideoInfo.getMusicComposer());
            txtYear.append(mVideoInfo.getMusicYear());
            if (!TextUtils.isEmpty(mVideoInfo.getVideoThumbnail())) {
                Glide.with(this).load(mVideoInfo.getVideoThumbnail()).into(imgThumbnail);
            }
        }

    }

    private String[] getDataSent() {
        Intent intent = getIntent();
        if (intent != null) {
            return new String[]{intent.getStringExtra(KEY_ID), intent.getStringExtra(KEY_URL_TITLE)};
        }
        return null;
    }


    @Override
    public void onPrepring() {
        // TODO: Handle something here !!! onPrepring
    }

    @Override
    public void onLoadCompleted(VideoInfo resultCallBack) {
        if (resultCallBack != null) {
            mVideoInfo = resultCallBack;
            initDataViews();
            mVideoView.setMediaController(mMediaController);
            mVideoView.setVideoPath(resultCallBack.getFileUrl());
            mProgressBar.setIndeterminate(true);
            mVideoView.requestFocus();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mProgressBar.setVisibility(View.GONE);
//                    mVideoView.setEnabled(true);
                    mVideoView.start();
                }
            });

        }
        viewLoading.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadFailed(Exception e) {

    }

    @Override
    public void onBackPressed() {
        doNavigationToBack();

    }

    private void doNavigationToBack() {
        Animation slideAnim = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        slideAnim.setFillAfter(true);
        slideAnim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation paramAnimation) {
            }

            public void onAnimationRepeat(Animation paramAnimation) {
            }

            public void onAnimationEnd(Animation paramAnimation) {
                new VideoContainerFragment.BackEvent().sendEmptyMessage(0);
                finish();
                overridePendingTransition(0, 0);
            }
        });
        flRoot.startAnimation(slideAnim);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            makeVideoFullScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            exitVideoFullScreen();
        }
    }


    // play video in fullscreen mode
    private void makeVideoFullScreen() {
        defaultScreenOrientationMode = getResources().getConfiguration().orientation;
        defaultVideoViewParams = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
        toolbar.setVisibility(View.GONE);
        cardVideoDetail.setVisibility(View.GONE);
        mVideoView.postDelayed(new Runnable() {

            @Override
            public void run() {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL);
                mVideoView.setLayoutParams(params);
                mVideoView.layout(10, 10, 10, 10);
            }
        }, 200);
    }


    // close fullscreen mode
    private void exitVideoFullScreen() {
        setRequestedOrientation(defaultScreenOrientationMode);
        toolbar.setVisibility(View.VISIBLE);
        cardVideoDetail.setVisibility(View.VISIBLE);
        mVideoView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mVideoView.setLayoutParams(defaultVideoViewParams);
                mVideoView.layout(10, 10, 10, 10);
            }
        }, 200);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_POSITION, mVideoView.getCurrentPosition());
        mVideoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            int position = savedInstanceState.getInt(KEY_POSITION);
            mVideoView.seekTo(position);
        }
    }


    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                //Incoming call: Pause music
                if (mVideoView.isPlaying() && mVideoView != null) {
                    mVideoView.pause();
                }
            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                //Not in call: Play music
                if (!mVideoView.isPlaying() && mVideoView != null) {
                    mVideoView.resume();
                }
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                //A call is dialing, active or on hold
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };



}
