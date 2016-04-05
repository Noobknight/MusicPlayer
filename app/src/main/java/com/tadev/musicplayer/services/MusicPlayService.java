package com.tadev.musicplayer.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.tadev.musicplayer.constant.Constants;
import com.tadev.musicplayer.interfaces.IServicePlayer;
import com.tadev.musicplayer.metadata.MusicContainer;
import com.tadev.musicplayer.models.CurrentSongPlay;
import com.tadev.musicplayer.receivers.UpdateSeekbarReceiver;

import java.io.IOException;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class MusicPlayService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {
    private final String TAG = "MusicPlayService";
    public static final String ACTION_PLAY = "com.tadev.musicmMediaPlayer.action.PLAY";
    public static final String ACTION_PAUSE = "com.tadev.musicmMediaPlayer.action.PAUSE";
    public static final String ACTION_TOGGLE_PLAYPAUSE = "com.tadev.musicmMediaPlayer.action.PLAY_PAUSE";
    public static final String BUFFER_UPDATE = "com.tadev.musicplayer.action.ACTION_UPDATE";

    private boolean isPause = false;
    private MediaPlayer mMediaPlayer;
    private boolean hasDataSource = false;
    private int currentId = 0;
    private Handler mHandler;
    private LocalBroadcastManager localBroadcastManager;
    private UpdateSeekbarReceiver receiver;
    private IServicePlayer mListener;
    private CurrentSongPlay mCurrentSongPlay;
    private MusicContainer musicContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        createMediaIfNeed();
        mHandler = new Handler();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        receiver = new UpdateSeekbarReceiver();
        localBroadcastManager.registerReceiver(receiver, new IntentFilter(BUFFER_UPDATE));
        musicContainer = MusicContainer.getInstance();
    }

    public void setOnMusicPlayListener(IServicePlayer mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mListener = null;
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mCurrentSongPlay = intent.getExtras().getParcelable(Constants.KEY_PASS_DATA_SERVICE);
            boolean isCurrentId = false;
            if (currentId == 0) {
                currentId = Integer.parseInt(mCurrentSongPlay.musicId);
            } else {
                isCurrentId = Integer.parseInt(mCurrentSongPlay.musicId) == currentId;
                currentId = Integer.parseInt(mCurrentSongPlay.musicId);
            }
//            String url = intent.getStringExtra("url");
//            if (currentId == 0) {
//                currentId = intent.getIntExtra("id", 0);
//            } else {
//                isCurrentId = intent.getIntExtra("id", 0) == currentId;
//                currentId = intent.getIntExtra("id", 0);
//            }
            String action = intent.getAction();
            switch (action) {
                case ACTION_TOGGLE_PLAYPAUSE:
                    if (isCurrentId) {
                        if (mMediaPlayer != null && hasDataSource) {
                            if (mMediaPlayer.isPlaying()) {
                                pause();
                            } else {
                                mMediaPlayer.start();
                            }
                            primaryUpdateSeekBar();
                            break;
                        }
                    } else {
                        mMediaPlayer.stop();
                        mMediaPlayer.reset();
                        play(mCurrentSongPlay.fileUrl);
                        duration();
                        musicContainer.setmCurrentSongPlay(mCurrentSongPlay);
                    }
                    break;
                case ACTION_PLAY:
                    if (mMediaPlayer.isPlaying()) break;
                    play(mCurrentSongPlay.fileUrl);
                    break;
                case ACTION_PAUSE:
                    pause();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
//        duration = mMediaPlayer.getDuration();
        mListener.duration(mMediaPlayer.getDuration());
        mMediaPlayer.start();
        primaryUpdateSeekBar();
    }

    public void play(String url) {
        requestAudioFocus();
        if (!TextUtils.isEmpty(url)) {
            try {
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepareAsync();
                hasDataSource = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void playOrPause() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        } else {
            mMediaPlayer.pause();
        }
    }

    public int duration() {
        return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
    }

    public int position() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }

    public boolean isPause() {
        return isPause && mMediaPlayer != null;
    }


    private void stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }

    public MusicPlayService setIsPause(boolean isPause) {
        this.isPause = isPause;
        return this;
    }

    public void seekTo(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    private void createMediaIfNeed() {
        if (mMediaPlayer != null) {
            return;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    private void requestAudioFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // TODO: We need to handle the case where we can't get audio focus
        }
    }

    public CurrentSongPlay getCurrentSongPlay() {
        return mCurrentSongPlay;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // TODO: We've gained audio focus, handle it
                if (mMediaPlayer != null) {
                    if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
                    mMediaPlayer.setVolume(1.0f, 1.0f);
                } else {
                    // TODO: Init the mMediaPlayer?
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                stopPlayback();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                mMediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mMediaPlayer != null) {
                    mMediaPlayer.setVolume(0.3f, 0.3f);
                }
                break;
        }
    }


    @Override
    public void onDestroy() {
        stopPlayback();
        localBroadcastManager.unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void sendProgressUpdate(int progress, int currentPosition) {
        Intent intent = new Intent(BUFFER_UPDATE);
        intent.putExtra(UpdateSeekbarReceiver.KEY_UPDATE_PROGRESS, progress);
        intent.putExtra(UpdateSeekbarReceiver.KEY_CURRENT_POSITION, currentPosition);
        localBroadcastManager.sendBroadcast(intent);

    }

    private void sendProgressUpdate(int progress) {
        Intent intent = new Intent(BUFFER_UPDATE);
        intent.putExtra(UpdateSeekbarReceiver.KEY_UPDATE_PROGRESS, progress);
//        intent.putExtra(UpdateSeekbarReceiver.KEY_CURRENT_POSITION, currentPosition);
        localBroadcastManager.sendBroadcast(intent);

    }

    public int getCurrentId() {
        return currentId;
    }


    private void primaryUpdateSeekBar() {
        final int progress = (int) ((((float) position() / duration()) * 100));
        sendProgressUpdate(progress);
        if (mMediaPlayer.isPlaying()) {
            Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (mMediaPlayer != null) {
                        mListener.onPublish(progress);
                        primaryUpdateSeekBar();
                    }
                }
            };
            mHandler.postDelayed(update, 1000);
        }
    }

    public class PlayBinder extends Binder {
        public MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }

}
