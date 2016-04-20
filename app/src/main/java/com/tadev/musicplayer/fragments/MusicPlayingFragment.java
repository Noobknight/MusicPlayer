package com.tadev.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.callbacks.OnRegisterCallback;
import com.tadev.musicplayer.common.Api;
import com.tadev.musicplayer.constant.Extras;
import com.tadev.musicplayer.constant.MusicTypeEnum;
import com.tadev.musicplayer.interfaces.UpdateableFragment;
import com.tadev.musicplayer.models.music.CurrentSongPlay;
import com.tadev.musicplayer.models.music.Download;
import com.tadev.musicplayer.models.music.Lyric;
import com.tadev.musicplayer.models.music.Song;
import com.tadev.musicplayer.receivers.UpdateSeekbarReceiver;
import com.tadev.musicplayer.services.DownloaderService;
import com.tadev.musicplayer.services.MusicPlayService;
import com.tadev.musicplayer.supports.design.PlayPauseDrawable;
import com.tadev.musicplayer.supports.design.TextViewTitle;
import com.tadev.musicplayer.utils.actions.Actions;
import com.tadev.musicplayer.utils.support.Utils;

import java.util.HashMap;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class MusicPlayingFragment extends BaseFragment implements View.OnClickListener,
        UpdateableFragment {
    private static final String TAG = "MusicPlayingFragment";
    private static final String KEY_SONG = "song";
    private static final String KEY_LYRIC = "lyric";
    private TextViewTitle txtTitle, txtArtist;
    private SeekBar seekBar;
    private ImageView imgShuffle, imgRepeat, imgNext, imgPrevious, imgDownoad;
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
    private FrameLayout framePlaying;

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
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayService.BUFFER_UPDATE);
        intentFilter.addAction(Actions.ACTION_PLAYPAUSE_NOTIFICATION);
        intentFilter.addAction(Actions.ACTION_PLAY);
        localBroadcastManager.registerReceiver(updateSeekbar,
                intentFilter);
    }

    @Override
    public void onStart() {
        if (mService != null &&
                mService.getCurrentId() != Integer.parseInt(mSong.getMusicId())
                ) {
            Intent intent = new Intent(getActivity(), MusicPlayService.class);
            if (mSong.getType() == MusicTypeEnum.ONLINE) {
                intent.setAction(Actions.ACTION_TOGGLE);
                intent.putExtras(initDataCurrentPlay());
            } else {
                intent.setAction(Actions.ACTION_PLAY_PAUSE);
                intent.putExtra(Extras.KEY_MODE_OFFLINE, true);
            }
            mOnRegisterCallback.onServicePreparing(intent);
            //Service has register and musicID not equal
            playPauseDrawable.transformToPause(true);
            isPlayState = true;
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
        imgDownoad = (ImageView) rootView.findViewById(R.id.btnDownload);
        framePlaying = (FrameLayout) rootView.findViewById(R.id.framePlaying);
    }

    @Override
    protected void initViewData() {
        fabPlayPause.setImageDrawable(playPauseDrawable);
        switch (mSong.getType()) {
            case LOCAL:
                imgDownoad.setVisibility(View.GONE);
                break;
            case ONLINE:
                imgDownoad.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    protected void setViewEvents() {
        fabPlayPause.setOnClickListener(mFLoatingButtonListener);
        imgDownoad.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgPrevious.setOnClickListener(this);
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
                    case Actions.ACTION_PLAY:
                        if (!isPlayState) {
                            Intent pause = new Intent(context, MusicPlayService.class);
                            pause.setAction(Actions.ACTION_PAUSE);
                            mOnRegisterCallback.onServicePreparing(pause);
                        }
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
            Intent intent = new Intent(getActivity(), MusicPlayService.class);
            if (mSong.getType() == MusicTypeEnum.ONLINE) {
                intent.setAction(Actions.ACTION_PLAY_PAUSE);
            } else {
                intent.setAction(Actions.ACTION_PLAY_PAUSE);
                intent.putExtra(Extras.KEY_MODE_OFFLINE, true);
            }
            mOnRegisterCallback.onServicePreparing(intent);
            if (isPlayState) {
                playPauseDrawable.transformToPause(true);
                playPauseDrawable.transformToPlay(true);
                isPlayState = false;
            } else {
                playPauseDrawable.transformToPlay(true);
                playPauseDrawable.transformToPause(true);
                isPlayState = true;
            }
        }
    };


    private Bundle initDataCurrentPlay() {
        Bundle bundle = new Bundle();
        currentPlay.musicId = mSong.getMusicId();
        currentPlay.song = mSong;
        currentPlay.lyric = mLyric;
        bundle.putParcelable(Extras.KEY_PASS_DATA_SERVICE, currentPlay);
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


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btnDownload:
                DialogDownloadFragment mDialogDownload = DialogDownloadFragment
                        .newInstance(buildDownload());
                mDialogDownload.setTargetFragment(this, Extras.REQUEST_CODE);
                mDialogDownload.show(getFragmentManager(), "dialog");
                break;
            case R.id.fragment_music_playing_imgNext:
                intent = new Intent(getActivity(), MusicPlayService.class);
                intent.setAction(Actions.ACTION_NEXT);
                mOnRegisterCallback.onServicePreparing(intent);
                break;
            case R.id.fragment_music_playing_imgPrevious:
                intent = new Intent(getActivity(), MusicPlayService.class);
                intent.setAction(Actions.ACTION_PREVIOUS);
                mOnRegisterCallback.onServicePreparing(intent);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Extras.REQUEST_CODE && resultCode == Extras.RESULT_CODE) {
            if (data != null) {
                Download download = data.getExtras().getParcelable(Extras.KEY_EXTRA_DATA);
                Intent intent = new Intent(getActivity(), DownloaderService.class);
                intent.putExtra("id", download.getId());
                intent.putExtra("name", download.getName());
                intent.putExtra("url", download.getUrlChoose());
                mOnRegisterCallback.onDownloadRegister(intent);
            }
        }
    }

    private Download buildDownload() {
        Download download = new Download();
        download.setId(Integer.parseInt(mSong.getMusicId()));
        download.setName(mSong.getMusicTitle());
        download.setMusicFilesize(mSong.getMusicFilesize());
        download.setMusic320Filesize(mSong.getMusic320Filesize());
        download.setMusicM4aFilesize(mSong.getMusicM4aFilesize());
        download.setMusicLosslessFilesize(mSong.getMusicLosslessFilesize());
        HashMap<Integer, String> url = new HashMap<>();
        url.put(Api.MUSIC_128, mSong.getFileUrl());
        url.put(Api.MUSIC_320, mSong.getFile320Url());
        url.put(Api.MUSIC_500, mSong.getFileM4aUrl());
        url.put(Api.MUSIC_1000, mSong.getFileLossless());
        download.setUrl(url);
        return download;
    }

    @Override
    public void update(CurrentSongPlay currentSongPlay) {
        currentPlay = currentSongPlay;
        mSong = currentSongPlay.song;
        txtTitle.setText(currentSongPlay.song.getMusicTitle());
        txtArtist.setText(currentSongPlay.song.getMusicArtist());
    }
}
