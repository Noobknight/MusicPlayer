package com.tadev.musicplayer.ui.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.callbacks.OnRegisterCallback;
import com.tadev.musicplayer.constant.Constants;
import com.tadev.musicplayer.models.CurrentSongPlay;
import com.tadev.musicplayer.models.Lyric;
import com.tadev.musicplayer.models.Song;
import com.tadev.musicplayer.receivers.UpdateSeekbarReceiver;
import com.tadev.musicplayer.services.MusicPlayService;
import com.tadev.musicplayer.utils.design.PlayPauseDrawable;
import com.tadev.musicplayer.utils.design.TextViewTitle;
import com.tadev.musicplayer.utils.design.actions.Actions;
import com.tadev.musicplayer.utils.design.support.Utils;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class MusicPlayingFragment extends BaseFragment {
    private static final String TAG = "MusicPlayingFragment";
    private static final String KEY_SONG = "song";
    private static final String KEY_LYRIC = "lyric";
    private TextViewTitle txtTitle, txtArtist;
    private SeekBar seekBar;
    private ImageView imgShuffle, imgRepeat, imgNext, imgPrevious;
    private Song mSong;
    private Lyric mLyric;
    private FloatingActionButton fabPlayPause;
    private PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable();
    private MusicPlayService mService;
    private LocalBroadcastManager localBroadcastManager;
    private TextView txtCurrentTime, txtTotalTime;
    private CurrentSongPlay currentPlay;
    private boolean isPlayState;
    //Service Callback To Activity
    private OnRegisterCallback mOnRegisterCallback;


    public static MusicPlayingFragment newInstance(Song song, Lyric lyric) {
        MusicPlayingFragment fragment = new MusicPlayingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SONG, song);
        bundle.putParcelable(KEY_LYRIC, lyric);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate ");
        mService = mActivityMain.getService();
        currentPlay = application.getMusicContainer().getmCurrentSongPlay();
    }

    @Override
    public void onStart() {
        if (mService != null &&
                mService.getCurrentId() != Integer.parseInt(mSong.getMusicId())
                ) {
            //Service has register and musicID not equal
            playPauseDrawable.transformToPlay(true);
            isPlayState = false;
        } else {
            int duration = application.getMusicContainer().getmCurrentSongPlay().duration;
            int position = application.getMusicContainer().getmCurrentSongPlay().position;
            int progress = application.getMusicContainer().getmCurrentSongPlay().progress;
            if (duration != 0 && position != 0 && progress != 0) {
                txtTotalTime.setText(Utils.getTimeString(duration));
                txtCurrentTime.setText(Utils.getTimeString(position));
                seekBar.setProgress(progress);
            }
            //Service has register and musicId equal with current ID
            if (mService.isPlaying()) {
                playPauseDrawable.transformToPause(true);
                isPlayState = true;
            } else {
                playPauseDrawable.transformToPlay(true);
                isPlayState = false;
            }
        }
        txtTitle.setText(mSong.getMusicTitle());
        txtArtist.setText(mSong.getMusicArtist());
        super.onStart();


    }


    @Override
    public void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayService.BUFFER_UPDATE);
        intentFilter.addAction(Actions.ACTION_PLAYPAUSE_NOTIFICATION);
        localBroadcastManager.registerReceiver(updateSeekbar,
                intentFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        localBroadcastManager.unregisterReceiver(updateSeekbar);
        super.onPause();
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


    @Override
    protected int setLayoutById() {
        return R.layout.fragment_music_playing;
    }

    @Override
    protected void initView(View rootView) {
//        bindService();
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        txtTitle = (TextViewTitle) rootView.findViewById(R.id.fragment_music_playing_txtMusicName);
        txtArtist = (TextViewTitle) rootView.findViewById(R.id.fragment_music_playing_txtMusicArtist);
        imgNext = (ImageView) rootView.findViewById(R.id.fragment_music_playing_imgNext);
        imgPrevious = (ImageView) rootView.findViewById(R.id.fragment_music_playing_imgPrevious);
        imgRepeat = (ImageView) rootView.findViewById(R.id.fragment_music_playing_imgRepeat);
        imgShuffle = (ImageView) rootView.findViewById(R.id.fragment_music_playing_imgShuffle);
        seekBar = (SeekBar) rootView.findViewById(R.id.fragment_music_playing_seekbar);
        txtCurrentTime = (TextView) rootView.findViewById(R.id.fragment_music_playing_txtCurrentTime);
        txtTotalTime = (TextView) rootView.findViewById(R.id.fragment_music_playing_txtTotalTime);
        fabPlayPause = (FloatingActionButton) rootView.findViewById(R.id.fragment_music_playing_fabPlayPause);
    }

    @Override
    protected void initViewData() {
        fabPlayPause.setImageDrawable(playPauseDrawable);
//        if (mService != null &&
//                mService.getCurrentId() != Integer.parseInt(mSong.getMusicId())
//                ) {
//            //Service has register and musicID not equal
//            playPauseDrawable.transformToPlay(true);
//            isPlayState = false;
//        } else {
//            int duration = application.getMusicContainer().getmCurrentSongPlay().duration;
//            int position = application.getMusicContainer().getmCurrentSongPlay().position;
//            int progress = application.getMusicContainer().getmCurrentSongPlay().progress;
//            if (duration != 0 && position != 0 && progress != 0) {
//                txtTotalTime.setText(Utils.getTimeString(duration));
//                txtCurrentTime.setText(Utils.getTimeString(position));
//                seekBar.setProgress(progress);
//            }
//            //Service has register and musicId equal with current ID
//            if (mService.isPlaying()) {
//                playPauseDrawable.transformToPause(true);
//                isPlayState = true;
//            } else {
//                playPauseDrawable.transformToPlay(true);
//                isPlayState = false;
//            }
//        }
//        txtTitle.setText(mSong.getMusicTitle());
//        txtArtist.setText(mSong.getMusicArtist());
    }

    @Override
    protected void setViewEvents() {
        fabPlayPause.setOnClickListener(mFLoatingButtonListener);
    }

    private UpdateSeekbarReceiver updateSeekbar = new UpdateSeekbarReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                switch (action) {
                    case MusicPlayService.BUFFER_UPDATE:
                        if (mService.getCurrentId() == Integer.parseInt(mSong.getMusicId())
                                && mService.duration() > -1) {
                            int progress = intent.getIntExtra(UpdateSeekbarReceiver.KEY_UPDATE_PROGRESS, 0);
                            seekBar.setProgress(progress);
                            txtCurrentTime.setText(Utils.getTimeString(mService.position()));
                            txtTotalTime.setText(Utils.getTimeString(mService.duration()));
                        }
                        break;
                    case Actions.ACTION_PLAYPAUSE_NOTIFICATION:
                        updateStatePlayPause();
                        break;
                }

            }
        }
    };

    @Override
    protected void getDataCallBack() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSong = bundle.getParcelable(KEY_SONG);
            mLyric = bundle.getParcelable(KEY_LYRIC);
        }
    }


    @Override
    protected String TAG() {
        return TAG;
    }


    private final View.OnClickListener mFLoatingButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(MusicPlayService.ACTION_TOGGLE_PLAYPAUSE);
            Intent intent = new Intent(getActivity(), MusicPlayService.class);
            intent.setAction(Actions.ACTION_TOGGLE);
            intent.putExtras(initDataCurrentPlay());
            mOnRegisterCallback.onServicePreparing(intent);
            Log.i(TAG, "onClick " + isPlayState);
            if (isPlayState) {
                playPauseDrawable.transformToPause(true);
                playPauseDrawable.transformToPlay(true);
            } else {
                playPauseDrawable.transformToPlay(true);
                playPauseDrawable.transformToPause(true);
            }
        }
    };


    private Bundle initDataCurrentPlay() {
        Bundle bundle = new Bundle();
        currentPlay.musicId = mSong.getMusicId();
        currentPlay.song = mSong;
        currentPlay.lyric = mLyric;
        bundle.putParcelable(Constants.KEY_PASS_DATA_SERVICE, currentPlay);
        return bundle;
    }

    private void updateStatePlayPause() {
        if (mService.isPlaying()) {
            playPauseDrawable.transformToPause(true);
            isPlayState = true;
        } else {
            playPauseDrawable.transformToPlay(true);
            isPlayState = false;
        }
    }


}
