package com.tadev.musicplayer.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.models.Song;
import com.tadev.musicplayer.utils.design.TextViewTitle;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class MusicPlayingFragment extends BaseFragment {
    private static final String TAG = "MusicPlayingFragment";
    private static MusicPlayingFragment sInstance;
    private TextViewTitle txtTitle, txtArtist;
    private SeekBar seekBar;
    private ImageView imgShuffle, imgRepeat, imgNext, imgPrevious;
    private Song mSong;

    public static MusicPlayingFragment newInstance(Song song) {
        if (sInstance == null) {
            sInstance = new MusicPlayingFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(TAG, song);
            sInstance.setArguments(bundle);
        }
        return sInstance;
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
    }

    @Override
    protected void initViewData() {
        txtTitle.setText(mSong.getMusicTitle());
        txtArtist.setText(mSong.getMusicArtist());
    }

    @Override
    protected void setViewEvents() {

    }

    @Override
    protected void getDataCallBack() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSong = bundle.getParcelable(TAG);
        }
    }

    @Override
    protected String TAG() {
        return TAG;
    }
}
