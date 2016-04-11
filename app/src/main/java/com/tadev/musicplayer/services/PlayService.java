package com.tadev.musicplayer.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.tadev.musicplayer.MainActivity;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.constant.Constants;
import com.tadev.musicplayer.constant.Extras;
import com.tadev.musicplayer.interfaces.OnServiceListener;
import com.tadev.musicplayer.interfaces.PlayBack;
import com.tadev.musicplayer.metadata.MusicContainer;
import com.tadev.musicplayer.models.CurrentSongPlay;
import com.tadev.musicplayer.receivers.RemoteControlReceiver;
import com.tadev.musicplayer.receivers.UpdateSeekbarReceiver;
import com.tadev.musicplayer.services.loaders.BitmapLoader;
import com.tadev.musicplayer.utils.design.actions.Actions;
import com.tadev.musicplayer.utils.design.support.VersionUtils;

import java.io.IOException;

/**
 * Created by Iris Louis on 08/04/2016.
 */
public class PlayService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener, PlayBack,
        BitmapLoader.BitmapLoaderListener {
    private final String TAG = "PlayService";
    public static final String STATE_PLAY = "com.tadev.musicmMediaPlayer.state.STATE_PLAY";
    public static final String STATE_PAUSE = "com.tadev.musicmMediaPlayer.state.STATE_PAUSE";
    //define support constants
    private static final int NOTIFICATION_ID = 0x111;
    private static final long TIME_UPDATE = 100L;

    private MediaPlayer mMediaPlayer;
    private IntentFilter mNoisyFilter;
    private AudioManager mAudioManager;
    private NotificationManager mNotificationManager;
    private boolean isPause = false;
    private LocalBroadcastManager localBroadcastManager;
    private UpdateSeekbarReceiver receiver;
    private boolean mServiceInUse = false;
    private OnServiceListener mListener;
    private CurrentSongPlay mCurrentSongPlay;
    private MusicContainer musicContainer;
    private boolean mPausedByTransientLossOfFocus;
    private ComponentName mMediaButtonReceiverComponent;
    private PowerManager.WakeLock mWakeLock;
    private boolean hasDataSource = false;
    private int currentId = 0;
    private int mPlayingPosition;
    private Handler mHandler;
    private Runnable mBackgroundRunnable;
    private MediaSessionCompat mMediaSessionCompat;
    private Bitmap artWork;
    private int mServiceStartId = -1;
    private NotificationManagerCompat mNotificationManagerCompat;

    @Override
    public void onCreate() {
        super.onCreate();
        initPlayBack();
        registerReceiver(mReceiver, initIntentFilter());
//        mMediaButtonReceiverComponent = new ComponentName(getPackageName(),
//                RemoteControlReceiver.class.getName());
//        mAudioManager.registerMediaButtonEventReceiver(mMediaButtonReceiverComponent);
        mNotificationManagerCompat = NotificationManagerCompat.from(this);
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        mWakeLock.setReferenceCounted(false);
        musicContainer = MusicContainer.getInstance();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        mHandler = new Handler();
        mMediaButtonReceiverComponent = new ComponentName(this, RemoteControlReceiver.class);
        mMediaSessionCompat = new MediaSessionCompat(this, TAG, mMediaButtonReceiverComponent, null);

    }

    public void setListenerService(OnServiceListener mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BinderService();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommandIntent(intent);
        return START_NOT_STICKY;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        primaryUpdateSeekBar();
        start();

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (mMediaPlayer == null) {
            // this activity has handed its MediaPlayer off to the next activity
            // (e.g. portrait/landscape switch) and should abandon its focus
            mAudioManager.abandonAudioFocus(this);
            return;
        }
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                mPausedByTransientLossOfFocus = false;
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mMediaPlayer.isPlaying()) {
                    mPausedByTransientLossOfFocus = true;
                    pause();
                }
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
//                if (isPlaying()) {
//                    mMediaPlayer.setVolume(0.2f, 0.2f);
//                }
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mPausedByTransientLossOfFocus) {
                    mPausedByTransientLossOfFocus = false;
                    start();
                }
                break;
        }

    }



    @Override
    public void initPlayBack() {
        createMediaPlayer();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void start() {
        mMediaPlayer.start();
        isPause = false;
        mHandler.post(mBackgroundRunnable);
    }

    @Override
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            stopForeground(true);
            mHandler.removeCallbacks(mBackgroundRunnable);
        }
    }


    @Override
    public void play(String url) {
        requestAudioFocus();
        if (!TextUtils.isEmpty(url)) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepareAsync();
                hasDataSource = true;
                isPause = false;
                mListener.onChangeSongPlay(mCurrentSongPlay);
                updateNotification();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID);
        startForeground(NOTIFICATION_ID, createNotification());
    }

    private void cancelNotification() {
        stopForeground(true);
        mNotificationManager.notify(NOTIFICATION_ID, createNotification());
    }

    private Notification createNotification() {
        return null;
    }

    private void setBitmapArtwork(String url) {
        Glide.with(this).load(url).asBitmap().into(new BitmapLoader(this));
    }

    private void requestAudioFocus() {
        int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return;
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaSessionCompat.setPlaybackState(
                    new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PAUSED, getCurrentStreamPosition(), 1.0f)
                            .build());
            mMediaPlayer.pause();
        }
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public int getCurrentStreamPosition() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public int getDuration() {
        return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
    }

    @Override
    public void seekTo(int msec) {
        if (isPlaying() || isPause()) {
            mMediaPlayer.seekTo(msec);
            if (mListener != null) {
                mListener.onPublish(msec);
            }
        }
    }


    public boolean isPause() {
        return mMediaPlayer != null && isPause;
    }


    @Override
    public CurrentSongPlay getCurrentSongPlay() {
        return mCurrentSongPlay;
    }

    @Override
    public int getCurrentSongId() {
        return currentId;
    }

    @Override
    public void createMediaPlayer() {
        if (mMediaPlayer != null) {
            return;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void playOrPause() {
        if (mMediaPlayer != null) {
            if (isPlaying()) {
                pause();
            }else if(isPause()){
                resume();
            }
        }
    }

    private void resume(){
        mMediaPlayer.start();
    }

    @Override
    public void onBitmapResult(Bitmap bitmap) {
        artWork = bitmap;
        String title = mCurrentSongPlay.song.getMusicTitle();
        String artist = mCurrentSongPlay.song.getMusicArtist();
        boolean isPlaying = isPlaying();
        int playButtonResId = isPlaying ? R.drawable.ic_play_arrow_white_36dp
                : R.drawable.ic_pause_white_36dp;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Extras.FROM_NOTIFICATION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(artist)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(bitmap)
                .addAction(playButtonResId, "",
                        retrievePlaybackAction(Actions.ACTION_PLAY_PAUSE));
        if (VersionUtils.isJellyBeanMR1()) {
            builder.setShowWhen(false);
        } else if (bitmap != null && VersionUtils.isLollipop()) {
            builder.setColor(Palette.from(bitmap).generate().getVibrantColor(Color.parseColor("#403f4d")));
        }
        startForeground(NOTIFICATION_ID, builder.build());
    }

    public class BinderService extends Binder {
        public PlayService getService() {
            mServiceInUse = true;
            return PlayService.this;
        }
    }

    private IntentFilter initIntentFilter() {
        // Initialize the intent filter and each action
        IntentFilter filter = new IntentFilter();
        filter.addAction(Actions.ACTION_TOGGLE);
        filter.addAction(Actions.ACTION_PREVIOUS);
        filter.addAction(Actions.ACTION_NEXT);
        filter.addAction(Actions.ACTION_PLAY);
        filter.addAction(Actions.ACTION_PLAY_BAR);
        filter.addAction(Actions.ACTION_UPDATE);
        filter.addAction(Actions.VOLUME_CHANGED_ACTION);
        filter.addAction(Actions.ACTION_PAUSE);
        filter.addAction(Actions.ACTION_PLAY_PAUSE);
        return filter;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleCommandIntent(intent);
        }
    };

    private void handleCommandIntent(Intent intent) {
        if (intent != null) {
            boolean isCurrentId = false;
            String action = intent.getAction();
            switch (action) {
                case Actions.ACTION_TOGGLE:
                    mCurrentSongPlay = intent.getExtras().getParcelable(Constants.KEY_PASS_DATA_SERVICE);
                    if (currentId == 0) {
                        currentId = Integer.parseInt(mCurrentSongPlay.musicId);
                    } else {
                        isCurrentId = Integer.parseInt(mCurrentSongPlay.musicId) == currentId;
                        currentId = Integer.parseInt(mCurrentSongPlay.musicId);
                    }
                    if (isCurrentId) {
                        if (mMediaPlayer != null && hasDataSource) {
                            if (isPlaying()) {
                                pause();
                                localBroadcastManager.sendBroadcast(new Intent(STATE_PAUSE));
                            } else {
                                start();
                                localBroadcastManager.sendBroadcast(new Intent(STATE_PLAY));
                            }
                            primaryUpdateSeekBar();
                            break;
                        }
                    } else {
                        play(mCurrentSongPlay.song.getFileUrl());
                        mListener.onChangeSongPlay(mCurrentSongPlay);
                        getDuration();
                        musicContainer.setCurrentSongPlay(mCurrentSongPlay);
                        updateNotification();
                    }
                    break;
                case Actions.ACTION_PLAY:
                    playOrPause();
                    break;
                case Actions.ACTION_PAUSE:
                    pause();
                    break;
                case Actions.ACTION_NEXT:
                    break;
                case Actions.ACTION_PREVIOUS:
                    break;
                case Actions.VOLUME_CHANGED_ACTION:
                    break;
                case Actions.ACTION_PLAY_BAR:
                    playOrPause();
                    break;
                case Actions.ACTION_PLAY_PAUSE:
                    playOrPause();
                    break;
                case Actions.ACTION_STOP:
                    stop();
                    break;
            }
        }
    }


//    private Runnable mBackgroundRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (isPlaying() && mListener != null) {
//                mListener.onPublish(mMediaPlayer.getCurrentPosition());
//                int progress = (int) ((((float) getCurrentStreamPosition() / getDuration()) * 100));
//                sendProgressUpdate(progress);
//            }
//            mHandler.postDelayed(this, TIME_UPDATE);
//        }
//    };


    private PendingIntent retrievePlaybackAction(String action) {
        final ComponentName serviceName = new ComponentName(this, MusicPlayService.class);
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);
        return PendingIntent.getService(this, 0, intent, 0);
    }

    private void setupMediaSession() {
        requestAudioFocus();
        mMediaSessionCompat = new MediaSessionCompat(getApplication(), TAG, mMediaButtonReceiverComponent, null);
        mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSessionCompat.setCallback(mMediaSessionCallback);
        mMediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 0)
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SEEK_TO |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .build());
        mMediaSessionCompat.setActive(true);
    }


    private void updateMediaSessionMetaData() {
        mMediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .setState(
                        isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED,
                        getCurrentStreamPosition(),
                        1.0f)
                .build());
        mMediaSessionCompat.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mCurrentSongPlay.song.getMusicArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, mCurrentSongPlay.lyric.getMusicAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, mCurrentSongPlay.song.getMusicTitle())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, getDuration())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, artWork != null ? artWork : null)
                .build());
        mMediaSessionCompat.setActive(true);
        //}
    }

    private final MediaSessionCompat.Callback mMediaSessionCallback = new MediaSessionCompat.Callback() {

        @Override
        public void onPlay() {
            playOrPause();
        }

        @Override
        public void onPause() {
            playOrPause();
            mPlayingPosition = getCurrentStreamPosition();
            mPausedByTransientLossOfFocus = false;
        }

        @Override
        public void onStop() {
            stop();
            mPlayingPosition = getCurrentStreamPosition();
            mPausedByTransientLossOfFocus = false;
        }

        @Override
        public void onSeekTo(long pos) {
            seekTo((int) pos);
        }
    };


    private PendingIntent retrievePlaybackActions(final int which) {
        Intent action;
        PendingIntent pendingIntent;
        final ComponentName serviceName = new ComponentName(getBaseContext(), PlayService.class);
        switch (which) {
            case 1:
                // Play and pause
                action = new Intent(Actions.ACTION_PLAY_PAUSE);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(getBaseContext(), 1, action, 0);
                return pendingIntent;
            case 2:
                // Skip tracks
                action = new Intent(Actions.ACTION_NEXT);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(getBaseContext(), 2, action, 0);
                return pendingIntent;
            case 3:
                // Previous tracks
                action = new Intent(Actions.ACTION_PREVIOUS);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(getBaseContext(), 3, action, 0);
                return pendingIntent;
            case 4:
                // Stop and collapse the notification
                action = new Intent(Actions.ACTION_STOP);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(getBaseContext(), 4, action, 0);
                return pendingIntent;
            case 5:
                Intent player = new Intent(getBaseContext(), MainActivity.class);
                player.putExtra("NOW_PLAYING", true);
                pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, player, PendingIntent.FLAG_UPDATE_CURRENT);
                return pendingIntent;
            default:
                break;
        }

        return null;
    }

    private void releaseMediaSession() {
        if (mMediaSessionCompat != null && mMediaSessionCompat.isActive()) {
            Log.d(TAG, "Un-registering Media Session");
            mMediaSessionCompat.release();
            mMediaSessionCompat.setActive(false);
            cancelNotification();
            if (!mServiceInUse) {
                stopSelf(mServiceStartId);
            }
        }
    }

    public void releaseAudioManager() {
        if (mAudioManager != null) {
            Log.d(TAG, "Un-registering Audio focus listener");
            mAudioManager.abandonAudioFocus(this);
            //Log.d(LOG_TAG, "Un-registering Remote controller");
            //mAudioManager.unregisterRemoteControlClient(mRemoteControlClient);
        }
    }

    private void releaseMediaPlayer() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        unregisterReceiver(mReceiver);
        mWakeLock.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        releaseMediaSession();
        releaseAudioManager();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mServiceInUse = false;
        if (mPausedByTransientLossOfFocus) {
            return true;
        }
        stopSelf(mServiceStartId);
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        mServiceInUse = true;
    }

    private void releaseServiceUiAndStop() {
        if (isPlaying()
                || mPausedByTransientLossOfFocus
                ) {
            return;
        }

        cancelNotification();
        mAudioManager.abandonAudioFocus(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        mMediaSessionCompat.setActive(false);

        if (!mServiceInUse) {
            stopSelf(mServiceStartId);
        }
    }

    private void sendProgressUpdate(int progress) {
        Intent intent = new Intent(Actions.ACTION_UPDATE);
        intent.putExtra(UpdateSeekbarReceiver.KEY_UPDATE_PROGRESS, progress);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void primaryUpdateSeekBar() {
        final int progress = (int) ((((float) getCurrentStreamPosition() / getDuration()) * 100));
        sendProgressUpdate(progress);
        if (mMediaPlayer.isPlaying()) {
            mBackgroundRunnable = new Runnable() {
                @Override
                public void run() {
                    if (mMediaPlayer != null) {
                        mListener.onPublish(progress);
                        mListener.position(getDuration());
                        primaryUpdateSeekBar();
                    }
                }
            };
            mHandler.postDelayed(mBackgroundRunnable, TIME_UPDATE);
        }
    }
}
