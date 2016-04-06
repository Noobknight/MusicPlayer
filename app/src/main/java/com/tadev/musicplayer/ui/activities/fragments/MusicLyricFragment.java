package com.tadev.musicplayer.ui.activities.fragments;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.models.Lyric;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class MusicLyricFragment extends BaseFragment {
    private static final String TAG = "MusicLyricFragment";
    //    private static MusicLyricFragment sInstance;
    private TextView txtLyric;
    private Lyric lyric;

//    public static MusicLyricFragment newInstance(Lyric lyric) {
//        if (sInstance == null) {
//            sInstance = new MusicLyricFragment();
//            Bundle bundle = new Bundle();
//            bundle.putParcelable(TAG, lyric);
//            sInstance.setArguments(bundle);
//        }
//        return sInstance;
//    }

    public static MusicLyricFragment newInstance(Lyric lyric) {
        MusicLyricFragment fragment = new MusicLyricFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, lyric);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutById() {
        return R.layout.fragment_music_lyric;
    }

    @Override
    protected void initView(View view) {
        txtLyric = (TextView) view.findViewById(R.id.fragment_music_lyric_txtLyric);
    }

    @Override
    protected void initViewData() {
        if (lyric != null) {
            boolean isEmptyLyric = lyric.getMusicLyric().isEmpty() || lyric.getMusicLyric() == null;
            if (isEmptyLyric) {
                txtLyric.setText(getResources().getString(R.string.lyric_empty));
            }
            txtLyric.setText(lyric.getMusicLyric());
            txtLyric.setMovementMethod(ScrollingMovementMethod.getInstance());
        } else {
            txtLyric.setText("Lyric not found");
        }
    }

    @Override
    protected void setViewEvents() {

    }

    @Override
    protected void getDataCallBack() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            lyric = bundle.getParcelable(TAG);
        }
    }

    @Override
    protected String TAG() {
        return TAG;
    }


}
