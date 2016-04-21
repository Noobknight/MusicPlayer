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
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tadev.musicplayer.MusicPlayerApplication;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.constant.Extras;
import com.tadev.musicplayer.constant.MusicTypeEnum;
import com.tadev.musicplayer.fragments.SettingFragment;
import com.tadev.musicplayer.helpers.NotificationHelper;
import com.tadev.musicplayer.interfaces.IServicePlayer;
import com.tadev.musicplayer.metadata.MusicContainer;
import com.tadev.musicplayer.models.MusicOffline;
import com.tadev.musicplayer.models.music.CurrentSongPlay;
import com.tadev.musicplayer.models.music.Lyric;
import com.tadev.musicplayer.models.music.Song;
import com.tadev.musicplayer.receivers.RemoteControlReceiver;
import com.tadev.musicplayer.receivers.UpdateSeekbarReceiver;
import com.tadev.musicplayer.utils.actions.Actions;
import com.tadev.musicplayer.utils.support.CoverLoader;
import com.tadev.musicplayer.utils.support.SharedPrefsUtils;
import com.tadev.musicplayer.utils.support.StringUtils;
import com.tadev.musicplayer.utils.support.Utils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class MusicPlayService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {
    private final String TAG = "MusicPlayService";
    public static final String BUFFER_UPDATE = "com.tadev.musicplayer.action.ACTION_UPDATE";
    public static final String PATH_URI = "/storage";
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
    private NotificationManager mNotificationManager;
    private long mNotificationPostTime = 0;
    private Bitmap btmArtWork;
    private MediaSessionCompat mMediaSessionCompat;
    private boolean mPausedByTransientLossOfFocus, isPlayOffline;
    private boolean mServiceInUse = false;
    private boolean isMusicRepared, isPauseFromNotification;
    private AudioManager mAudioManager;
    private int mServiceStartId = -1;
    private int mPlayingPosition = 0;
    private ArrayList<MusicOffline> mListOffline;
    public int playingAtPosition, indexOfList;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate ");
        initPlayBack();
        mHandler = new Handler();
        registerReceiver(mReceiver, initIntentFilter());
        ComponentName mMediaButtonReceiverComponent = new ComponentName(this, RemoteControlReceiver.class);
        mMediaSessionCompat = new MediaSessionCompat(this, TAG, mMediaButtonReceiverComponent, null);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        receiver = new UpdateSeekbarReceiver();
        localBroadcastManager.registerReceiver(receiver, new IntentFilter(BUFFER_UPDATE));
        musicContainer = MusicContainer.getInstance();
    }

    private void setupMediaSessionCallback() {
        mMediaSessionCompat.setCallback(mMediaSessionCallback);
        mMediaSessionCompat.setPlaybackState(playbackState(PlaybackStateCompat.STATE_STOPPED,
                PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0));
        mMediaSessionCompat.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, mCurrentSongPlay.song.getMusicTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mCurrentSongPlay.song.getMusicArtist())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, getDuration())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, btmArtWork != null ? btmArtWork : null)
                .build());
        try {
            mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        } catch (Exception e) {
            mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        }

    }

    public PlaybackStateCompat playbackState(int state, long position, float playbackSpeed) {
        return new PlaybackStateCompat.Builder()
//                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
//                        | PlaybackStateCompat.ACTION_SKIP_TO_NEXT)
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                        PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT)
                .setState(state, position, playbackSpeed)
                .build();
    }

    private long getAvailableActions() {
        long actions =
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                        PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
        if (isPlaying()) {
            actions |= PlaybackStateCompat.ACTION_PAUSE;
        }
        return actions;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleCommandIntent(intent);
        }
    };

    public void setOnMusicPlayListener(IServicePlayer mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isPlayOffline = enableModePlayOffline(intent);
        handleCommandIntent(intent);
        return START_REDELIVER_INTENT;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isMusicRepared = true;
        mListener.duration(getDuration());
        localBroadcastManager.sendBroadcast(new Intent(Actions.ACTION_PLAY));
        start();
        createNotification();
        primaryUpdateSeekBar();
        mMediaSessionCompat.setPlaybackState(playbackState(PlaybackStateCompat.STATE_PLAYING,
                getCurrentStreamPosition(), 1));
        mMediaSessionCompat.setActive(true);
    }

    public boolean isMusicPrepared() {
        return isMusicRepared;
    }

    public boolean isHasDataSource() {
        return hasDataSource;
    }


    public int duration() {
        return mMediaPlayer != null || mMediaPlayer.getDuration() > 0 ? mMediaPlayer.getDuration() : 0;
    }

    public int position() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }


    private void requestAudioFocus() {
        int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return;
        }
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
                if (isPlaying()) {
                    mPausedByTransientLossOfFocus = true;
                }
                pause();
                break;
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
    public void onDestroy() {
//        stopPlayback();
        localBroadcastManager.unregisterReceiver(receiver);
        unregisterReceiver(mReceiver);
        cancelNotification();
        releaseAudioManager();
        releaseMediaPlayer();
        releaseMediaSession();

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
        localBroadcastManager.sendBroadcast(intent);
    }

    public int getCurrentId() {
        return currentId;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
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
                        mListener.position(position());
                        primaryUpdateSeekBar();
                    }
                }
            };
            mHandler.postDelayed(update, 1000);
        }
    }


    private void createNotification() {
        int notificationId = hashCode();
        startForeground(notificationId, buildNotification());
    }

    private Notification buildNotification() {
        downloadImageArtWork();
        if (mNotificationPostTime == 0) {
            mNotificationPostTime = System.currentTimeMillis();
        }
        String title = mCurrentSongPlay.song.getMusicTitle();
        String artist = mCurrentSongPlay.song.getMusicArtist();
        Intent nowPlayingIntent = Utils.getNowPlayingIntent(this);
        PendingIntent clickIntent = PendingIntent.getActivity(this, 0, nowPlayingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        boolean isPlaying = isPlaying();
        int stateButton = isPlaying ? R.drawable.ic_pause_white_36dp : R.drawable.ic_play_arrow_white_36dp;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(artist)
                .setLargeIcon(btmArtWork)
                .setWhen(mNotificationPostTime)
                .setContentIntent(clickIntent)
                .setColor(Utils.getColorRes(R.color.colorPrimary))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        isPlayOffline = mCurrentSongPlay.song.getType() == MusicTypeEnum.LOCAL;
        if (isPlayOffline) {
            builder.addAction(R.drawable.ic_skip_previous_white_36dp, StringUtils.getStringRes(R.string.action_previous),
                    NotificationHelper.getActionIntent(this, KeyEvent.KEYCODE_MEDIA_PREVIOUS));
            builder.addAction(stateButton, StringUtils.getStringRes(R.string.action_play_pause),
                    NotificationHelper.getActionIntent(this, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
            builder.addAction(R.drawable.ic_skip_next_white_36dp, StringUtils.getStringRes(R.string.action_next),
                    NotificationHelper.getActionIntent(this, KeyEvent.KEYCODE_MEDIA_NEXT));
        } else {
            builder.addAction(stateButton, StringUtils.getStringRes(R.string.action_play_pause),
                    NotificationHelper.getActionIntent(this, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
        }
        NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle();
        if (isPlayOffline) {
            style.setShowActionsInCompactView(0, 1, 2);
        } else {
            style.setShowActionsInCompactView(0);
        }
        style.setShowCancelButton(true)
                .setCancelButtonIntent(NotificationHelper.getActionIntent(this, KeyEvent.KEYCODE_MEDIA_STOP));
        builder.setStyle(style);
        return builder.build();
    }


    private void cancelNotification() {
        stopForeground(true);
        mNotificationManager.cancel(hashCode());
        mNotificationPostTime = 0;
    }


    public class PlayBinder extends Binder {
        public MusicPlayService getService() {
            mServiceInUse = true;
            return MusicPlayService.this;
        }
    }


    private void downloadImageArtWork() {
        String linkImage = mCurrentSongPlay.song.getMusicImg();
        if (!TextUtils.isEmpty(linkImage)) {
            if (linkImage.startsWith(PATH_URI)) {
                btmArtWork = CoverLoader.getInstance().loadThumbnail(linkImage);
            } else {
                Glide.with(this).load(linkImage).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        btmArtWork = resource;
                    }
                });
            }
        } else {
            btmArtWork = BitmapFactory.decodeResource(MusicPlayerApplication.getInstance().getResources(),
                    R.drawable.ic_default_cover);
        }
        setupMediaSessionCallback();

    }


    public void initPlayBack() {
        createMediaPlayer();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public void start() {
        mMediaPlayer.start();
        isPause = false;
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            stopForeground(true);
        }
    }

    public void play(String url) {
        requestAudioFocus();
        if (!TextUtils.isEmpty(url)) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepareAsync();
                hasDataSource = true;
                isPlayOffline = false;
                isPause = false;
                if (mListener != null) {
                    mListener.currentSongPlay(mCurrentSongPlay);
                }
                getDuration();
                musicContainer.setCurrentSongPlay(mCurrentSongPlay);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            updateStateMediaSession();
            mMediaPlayer.pause();
        }
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public int getCurrentStreamPosition() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0;
    }

    public int getDuration() {
        return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
    }

    public void seekTo(int msec) {
        if (isPlaying() || isPaused()) {
            mMediaPlayer.seekTo(msec);
            if (mListener != null) {
                mListener.onPublish(msec);
            }
        }
    }

    public int next() {
        if (mListOffline == null) {
            return -1;
        } else {
            playingAtPosition += 1;
            refreshData(playingAtPosition);
            playOffline(playingAtPosition);
            Intent intent = new Intent();
            intent.setAction(Actions.ACTION_UPDATE_MAIN_MUSIC);
            localBroadcastManager.sendBroadcast(intent);
        }
        return playingAtPosition;
    }

    public int previous() {
        if (mListOffline == null) {
            return -1;
        } else {
            playingAtPosition -= 1;
            refreshData(playingAtPosition);
            playOffline(playingAtPosition);
            Intent intent = new Intent(Actions.ACTION_UPDATE_MAIN_MUSIC);
            localBroadcastManager.sendBroadcast(intent);
        }
        return playingAtPosition;
    }


    public int playOffline(int indexOfList) {
        requestAudioFocus();
        if (mListOffline == null) {
            return -1;
        }
        if (indexOfList < 0) {
            indexOfList = getListOffline().size() - 1;
        } else if (indexOfList >= getListOffline().size()) {
            indexOfList = 0;
        }
        playingAtPosition = indexOfList;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(getListOffline().get(indexOfList).getUri());
            mMediaPlayer.prepareAsync();
            hasDataSource = true;
            isPause = false;
            isPlayOffline = true;
            mListener.currentSongPlay(mCurrentSongPlay);
            getDuration();
            musicContainer.setCurrentSongPlay(mCurrentSongPlay);
            if (mListener != null) {
                mListener.onChange((int) getListOffline().get(indexOfList).getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mPlayingPosition;
    }

    public boolean isPaused() {
        return mMediaPlayer != null && !isPlaying();
    }

    public CurrentSongPlay getCurrentSongPlay() {
        return mCurrentSongPlay;
    }

    public int getCurrentSongId() {
        return currentId;
    }

    public void createMediaPlayer() {
        if (mMediaPlayer != null) {
            return;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void playOrPause() {
        if (mMediaPlayer != null) {
            if (isPlaying()) {
                localBroadcastManager.sendBroadcast(new Intent(Actions.ACTION_PAUSE));
                pause();
            } else {
                localBroadcastManager.sendBroadcast(new Intent(Actions.ACTION_PLAY));
                resume();
            }
        }
    }

    private void playOrPauseNotification() {
        if (isPlaying()) {
            pause();
        } else {
            resume();
        }
        localBroadcastManager.sendBroadcast(new Intent(Actions.ACTION_PLAYPAUSE_NOTIFICATION));
    }

    public int getPlayingPosition() {
        return mPlayingPosition;
    }


    private void resume() {
        mMediaPlayer.start();
        updateStateMediaSession();
    }


    private void updateStateMediaSession() {
        mMediaSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(
                        isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED,
                        getCurrentStreamPosition(),
                        1)
                .build());
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
        public void onSkipToNext() {
            next();
        }

        @Override
        public void onSkipToPrevious() {
            previous();
        }

        @Override
        public void onSeekTo(long pos) {
            seekTo((int) pos);
        }
    };

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
        filter.addAction(Actions.ACTION_MEDIA_PLAY);
        return filter;
    }

    private void handleCommandIntent(Intent intent) {
        if (intent != null) {
            indexOfList = intent.getIntExtra(Extras.KEY_INDEX_LIST, 0);
            boolean isCurrentId = false;
            String action = intent.getAction();
            switch (action) {
                case Actions.ACTION_TOGGLE:
                    getDataIntent(intent, isPlayOffline);
                    if (currentId == 0) {
                        currentId = Integer.parseInt(mCurrentSongPlay.musicId);
                    } else {
                        isCurrentId = Integer.parseInt(mCurrentSongPlay.musicId) == currentId;
                        currentId = Integer.parseInt(mCurrentSongPlay.musicId);
                    }
                    if (isCurrentId) {
                        if (mMediaPlayer != null && hasDataSource) {
                            if (isPauseFromNotification) {
                                createNotification();
                            }
                            playOrPause();
                            updateStateButtonNotification();
                            break;
                        }
                    } else {
                        if (isPlayOffline) {
                            playOffline(indexOfList);
                        } else {
                            play(urlPlay());
                        }
                    }
                    break;
                case Actions.ACTION_PLAY:
                    playOrPause();
                    break;
                case Actions.ACTION_PAUSE:
                    pause();
                    break;
                case Actions.ACTION_NEXT:
                    next();
                    break;
                case Actions.ACTION_PREVIOUS:
                    previous();
                    break;
                case Actions.VOLUME_CHANGED_ACTION:
                    break;
                case Actions.ACTION_PLAY_BAR:
                    if (isPauseFromNotification) {
                        createNotification();
                    }
                    playOrPause();
                    updateStateButtonNotification();
                    break;
                case Actions.ACTION_PLAY_PAUSE:
                    playOrPause();
                    updateStateButtonNotification();
                    break;
                case Actions.ACTION_STOP:
                    stop();
                    break;
                case Actions.ACTION_MEDIA_PLAY:
                    KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                    if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
                        break;
                    }
                    switch (event.getKeyCode()) {
                        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                            playOrPauseNotification();
                            updateStateButtonNotification();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            previous();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            next();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_STOP:
                            isPauseFromNotification = true;
                            if (isPaused()) {
                                cancelNotification();
                            } else {
                                playOrPauseNotification();
                                cancelNotification();
                            }
                            break;
                    }
            }
            mMediaSessionCompat.setPlaybackState(playbackState(isPlaying() ?
                            PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED,
                    getCurrentStreamPosition(), 1));
            primaryUpdateSeekBar();
        }
    }

    private void updateStateButtonNotification() {
        mNotificationManager.cancel(hashCode());
        mNotificationPostTime = 0;
        createNotification();
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
        }
    }


    public boolean isPlayOffline() {
        return isPlayOffline;
    }


    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
//        unregisterReceiver(mReceiver);
    }

    private boolean enableModePlayOffline(Intent intent) {
        if (intent.hasExtra(Extras.KEY_MODE_OFFLINE)) {
            return true;
        }
        return false;
    }

    public ArrayList<MusicOffline> getListOffline() {
        return mListOffline;
    }

    public void setListOffline(ArrayList<MusicOffline> mListOffline) {
        this.mListOffline = mListOffline;
    }


    private CurrentSongPlay refreshData(int position) {
        if (position < 0) {
            position = getListOffline().size() - 1;
        } else if (position >= getListOffline().size()) {
            position = 0;
        }
        MusicOffline offline = mListOffline.get(position);
        Song newSong = new Song();
        newSong.setType(MusicTypeEnum.LOCAL);
        newSong.setMusicId(String.valueOf(offline.getId()));
        newSong.setMusicTitle(offline.getTitle());
        newSong.setMusicArtist(offline.getArtist());
        newSong.setMusicImg(offline.getCoverUri());
        Lyric lyric = new Lyric();
        lyric.setMusicTitle(offline.getTitle());
        lyric.setMusicArtist(offline.getArtist());
        lyric.setMusicImage(offline.getCoverUri());
        mCurrentSongPlay = new CurrentSongPlay();
        mCurrentSongPlay.song = newSong;
        mCurrentSongPlay.lyric = lyric;
        mCurrentSongPlay.musicId = String.valueOf(offline.getId());
        currentId = (int) offline.getId();
        return mCurrentSongPlay;
    }

    private void getDataIntent(Intent intent, boolean isPlayOffline) {
        if (isPlayOffline && mListOffline != null) {
            MusicOffline offline = mListOffline.get(indexOfList);
            Song newSong = new Song();
            newSong.setType(MusicTypeEnum.LOCAL);
            newSong.setMusicId(String.valueOf(offline.getId()));
            newSong.setMusicTitle(offline.getTitle());
            newSong.setMusicArtist(offline.getArtist());
            newSong.setMusicImg(offline.getCoverUri());
            Lyric lyric = new Lyric();
            lyric.setMusicTitle(offline.getTitle());
            lyric.setMusicArtist(offline.getArtist());
            lyric.setMusicImage(offline.getCoverUri());
            mCurrentSongPlay = new CurrentSongPlay();
            mCurrentSongPlay.song = newSong;
            mCurrentSongPlay.lyric = lyric;
            mCurrentSongPlay.musicId = String.valueOf(offline.getId());
        } else {
            mCurrentSongPlay = intent.getExtras().getParcelable(Extras.KEY_PASS_DATA_SERVICE);

        }
    }

    public String urlPlay() {
        int index = SharedPrefsUtils.getIntegerPreference(this, SettingFragment.KEY_POSITION_QUALITY, 0);
        String[] fileUrl = new String[]{mCurrentSongPlay.song.getFileUrl(), mCurrentSongPlay.song.getFile320Url(),
                mCurrentSongPlay.song.getFileM4aUrl(), mCurrentSongPlay.song.getFileLossless()};
        String url = fileUrl[index];
        while (url.isEmpty()) {
            url = fileUrl[index - 1];
        }
        return url;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        mListener = null;
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


}
