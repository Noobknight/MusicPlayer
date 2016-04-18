package com.tadev.musicplayer.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.tadev.musicplayer.R;

/**
 * Created by Iris Louis on 16/04/2016.
 */
public class VideoActivity extends Activity {
    private VideoView mVideoView;
    private String mUrl;
    private static String TAG = VideoActivity.class.getName();
    private RelativeLayout.LayoutParams defaultVideoViewParams;
    private int defaultScreenOrientationMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.test_video);
        mVideoView = (VideoView) findViewById(R.id.video);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i(TAG, "onConfigurationChanged ORIENTATION_LANDSCAPE");
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            makeVideoFullScreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i(TAG, "onConfigurationChanged ORIENTATION_PORTRAIT");
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            exitVideoFullScreen();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    // play video in fullscreen mode
    private void makeVideoFullScreen() {

        defaultScreenOrientationMode = getResources().getConfiguration().orientation;
        defaultVideoViewParams = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mVideoView.postDelayed(new Runnable() {

            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);

                mVideoView.setLayoutParams(params);
                mVideoView.layout(10, 10, 10, 10);
            }
        }, 700);
    }


    // close fullscreen mode
    private void exitVideoFullScreen() {
        setRequestedOrientation(defaultScreenOrientationMode);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        mVideoView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mVideoView.setLayoutParams(defaultVideoViewParams);
                mVideoView.layout(10, 10, 10, 10);
            }
        }, 700);
    }

}