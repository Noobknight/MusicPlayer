package com.tadev.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tadev.musicplayer.MainActivity;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.callbacks.OnRegisterCallback;
import com.tadev.musicplayer.constant.Extras;
import com.tadev.musicplayer.constant.MusicTypeEnum;
import com.tadev.musicplayer.models.music.CurrentSongPlay;
import com.tadev.musicplayer.receivers.UpdateSeekbarReceiver;
import com.tadev.musicplayer.services.MusicPlayService;
import com.tadev.musicplayer.supports.design.PlayPauseButton;
import com.tadev.musicplayer.supports.design.SquareImageView;
import com.tadev.musicplayer.utils.actions.Actions;
import com.tadev.musicplayer.utils.support.CoverLoader;
import com.tadev.musicplayer.utils.support.Utils;

import java.io.File;

/**
 * Created by Iris Louis on 06/04/2016.
 */
public class PlayBarBottomFragment extends BaseFragment {
    private final String TAG = "PlayBarBottomFragment";
    private SquareImageView coverImage;
    private ProgressBar prgUpdate;
    private TextView txtTitle, txtArtist;
    private PlayPauseButton mPlayPause;
    private View playPauseWrapper;
    private MusicPlayService mService;
    private CurrentSongPlay currentPlay;
    private LocalBroadcastManager localBroadcastManager;
    private OnRegisterCallback mOnRegisterCallback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate ");
        mService = mActivityMain.getService();
        currentPlay = application.getMusicContainer().getmCurrentSongPlay();
    }

    @Override
    protected int setLayoutById() {
        return R.layout.include_bottom_play_card;
    }


    private final View.OnClickListener mPlayPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            } else {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getActivity(), MusicPlayService.class);
                    intent.setAction(Actions.ACTION_PLAY_BAR);
                    if (currentPlay.song.getType() == MusicTypeEnum.LOCAL) {
                        intent.putExtra(Extras.KEY_MODE_OFFLINE, true);
                    }
                    mOnRegisterCallback.onServicePreparing(intent);
                }
            }, 100);

        }
    };

    private UpdateSeekbarReceiver updateSeekbar = new UpdateSeekbarReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                switch (action) {
                    case MusicPlayService.BUFFER_UPDATE:
                        if (mService != null
                                && mService.duration() > -1) {
                            int progress = intent.getIntExtra(UpdateSeekbarReceiver.KEY_UPDATE_PROGRESS, 0);
                            prgUpdate.setProgress(progress);
                        }
                        break;
                    case MainActivity.UPDATE_MUSIC_PLAYBAR:
                        CurrentSongPlay currentSongPlay = intent.getExtras()
                                .getParcelable(MainActivity.EXTRA_CURRENT_PLAY);
                        if (currentSongPlay != null) {
                            prgUpdate.setProgress(0);
                            String title = currentSongPlay.song.getMusicTitle();
                            String artist = currentSongPlay.song.getMusicArtist();
                            txtTitle.setText(title);
                            txtArtist.setText(artist);
                            String pathImage = currentSongPlay.song.getMusicImg();
                            if (!TextUtils.isEmpty(pathImage)) {
                                if (pathImage.startsWith(MusicPlayService.PATH_URI)) {
                                    Bitmap btmArtwork = CoverLoader.getInstance().loadThumbnail(pathImage);
                                    coverImage.setImageBitmap(btmArtwork);
                                } else {
                                    Glide.with(context).load(pathImage).into(coverImage);
                                }
                            } else {
                                coverImage.setImageResource(R.drawable.ic_default_cover);
                            }
                        }
                        break;
                    case Actions.ACTION_PLAY:
                        updateStatePlayPause();
                        break;
                    case Actions.ACTION_PAUSE:
                        updateStatePlayPause();
                        break;
                    case Actions.ACTION_PLAYPAUSE_NOTIFICATION:
                        updateStatePlayPause();
                        break;
                }
            }
        }
    };

    @Override
    protected void initView(View view) {
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        coverImage = (SquareImageView) view.findViewById(R.id.album_art_nowplayingcard);
        prgUpdate = (ProgressBar) view.findViewById(R.id.song_progress_normal);
        txtTitle = (TextView) view.findViewById(R.id.include_bottom_play_title);
        txtArtist = (TextView) view.findViewById(R.id.include_bottom_play_artist);
        mPlayPause = (PlayPauseButton) view.findViewById(R.id.include_bottom_play_play_pause);
        playPauseWrapper = view.findViewById(R.id.play_pause_wrapper);
        mPlayPause.setColor(Utils.getColorRes(R.color.material_pink_500));
        prgUpdate.getProgressDrawable().setColorFilter(Utils.getColorRes(R.color
                .material_pink_500), android.graphics.PorterDuff.Mode.SRC_IN);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) prgUpdate.getLayoutParams();
        prgUpdate.measure(0, 0);
        layoutParams.setMargins(0, -(prgUpdate.getMeasuredHeight() / 4), 0, 0);
        prgUpdate.setLayoutParams(layoutParams);
        updateStatePlayPause();
    }

    private void updateStatePlayPause() {
        if (mService.isPlaying()) {
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            }
        } else {
            if (mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
        }
    }


    @Override
    protected void initViewData() {
        if (currentPlay.song != null) {
            String title = currentPlay.song.getMusicTitle();
            String artist = currentPlay.song.getMusicArtist();
            txtArtist.setText(artist);
            txtTitle.setText(title);
            String pathImage = currentPlay.song.getMusicImg();
            if (!TextUtils.isEmpty(pathImage)) {
                if (pathImage.startsWith(MusicPlayService.PATH_URI)) {
                    Glide.with(context).load(new File(pathImage)).into(coverImage);
                } else {
                    Glide.with(context).load(pathImage).into(coverImage);
                }
            }
            mPlayPause.setPlayed(true);
            mPlayPause.startAnimation();
        }
    }

    @Override
    protected void setViewEvents() {
        playPauseWrapper.setOnClickListener(mPlayPauseListener);
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicPlayService.BUFFER_UPDATE);
        filter.addAction(MainActivity.UPDATE_MUSIC_PLAYBAR);
        filter.addAction(Actions.ACTION_PLAY);
        filter.addAction(Actions.ACTION_PAUSE);
        filter.addAction(Actions.ACTION_PLAYPAUSE_NOTIFICATION);
        localBroadcastManager.registerReceiver(updateSeekbar,
                filter);
        super.onResume();
    }

    @Override
    public void onPause() {
//        localBroadcastManager.unregisterReceiver(updateSeekbar);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(updateSeekbar);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        if (mService != null) {
            updateStatePlayPause();
//            if (mService.getCurrentId() != Integer.parseInt(currentPlay.musicId)) {
//                doUpdateUI();
//            }
        }
        super.onStart();
    }

    //You can update from onStart()
    private void doUpdateUI() {
        if (mService != null) {
            CurrentSongPlay newSong = mService.getCurrentSongPlay();
            String title = newSong.song.getMusicTitle();
            String artist = newSong.song.getMusicArtist();
            txtArtist.setText(artist);
            txtTitle.setText(title);
            String pathImage = newSong.song.getMusicImg();
            if (!TextUtils.isEmpty(pathImage)) {
                if (pathImage.startsWith(MusicPlayService.PATH_URI)) {
                    Glide.with(context).load(new File(pathImage)).into(coverImage);
                } else {
                    Glide.with(context).load(pathImage).into(coverImage);
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        AppCompatActivity activity;
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
            try {
                mOnRegisterCallback = (OnRegisterCallback) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must be implement OnRegisterCallBack");
            }
        }
        super.onAttach(context);
    }
}
