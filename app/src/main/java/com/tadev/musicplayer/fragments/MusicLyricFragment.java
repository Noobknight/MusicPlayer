package com.tadev.musicplayer.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.interfaces.UpdateableFragment;
import com.tadev.musicplayer.models.music.CurrentSongPlay;
import com.tadev.musicplayer.models.music.Lyric;
import com.tadev.musicplayer.services.MusicPlayService;
import com.tadev.musicplayer.utils.support.StringUtils;

import java.io.File;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class MusicLyricFragment extends BaseFragment implements UpdateableFragment {
    private static final String TAG = "MusicLyricFragment";
    //    private static MusicLyricFragment sInstance;
    private TextView txtLyric, txtArtist, txtYear, txtTitle;
    private ImageView imgArtist;
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
        txtArtist = (TextView) view.findViewById(R.id.fragment_music_lyric_txtArtist);
        txtYear = (TextView) view.findViewById(R.id.fragment_music_lyric_txtYear);
        txtTitle = (TextView) view.findViewById(R.id.fragment_music_lyric_txtTitle);
        imgArtist = (ImageView) view.findViewById(R.id.fragment_music_lyric_imgArtist);
    }

    @Override
    protected void initViewData() {
        if (lyric != null) {
            String year = lyric.getMusicYear() != null ? lyric.getMusicYear() : "";
            txtTitle.setText(lyric.getMusicTitle());
            txtArtist.setText(StringUtils.getStringRes(R.string.msg_lyric_artist) + lyric.getMusicArtist());
            txtYear.setText(StringUtils.getStringRes(R.string.msg_lyric_year) + year);
            boolean isEmptyLyric = TextUtils.isEmpty(lyric.getMusicLyric());
            if (isEmptyLyric) {
                txtLyric.setText(getResources().getString(R.string.lyric_empty));
            } else {
                txtLyric.setText(lyric.getMusicLyric());
                txtLyric.setMovementMethod(ScrollingMovementMethod.getInstance());
            }
            String imageCover = lyric.getMusicImage();
            if (TextUtils.isEmpty(imageCover)) {
                imgArtist.setImageResource(R.drawable.ic_default_cover);
            } else {
                if (imageCover.startsWith(MusicPlayService.PATH_URI)) {
                    Glide.with(context).load(new File(imageCover)).override(200, 140).into(imgArtist);
                } else {
                    Glide.with(context).load(lyric.getMusicImage()).override(200, 140).into(imgArtist);
                }
            }
        } else {
            txtLyric.setText(StringUtils.getStringRes(R.string.msg_lyric_not_found));
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


    @Override
    public void update(CurrentSongPlay currentSongPlay) {
        Lyric lyric = currentSongPlay.lyric;
        if (lyric != null) {
            String year = lyric.getMusicYear() != null ? lyric.getMusicYear() : "";
            txtTitle.setText(lyric.getMusicTitle());
            txtArtist.setText(StringUtils.getStringRes(R.string.msg_lyric_artist) + lyric.getMusicArtist());
            txtYear.setText(StringUtils.getStringRes(R.string.msg_lyric_year) + year);
            boolean isEmptyLyric = TextUtils.isEmpty(lyric.getMusicLyric());
            if (isEmptyLyric) {
                txtLyric.setText(getResources().getString(R.string.lyric_empty));
            } else {
                txtLyric.setText(lyric.getMusicLyric());
                txtLyric.setMovementMethod(ScrollingMovementMethod.getInstance());
            }
            String imageCover = lyric.getMusicImage();
            if (TextUtils.isEmpty(imageCover)) {
                imgArtist.setImageResource(R.drawable.ic_default_cover);
            } else {
                if (imageCover.startsWith(MusicPlayService.PATH_URI)) {
                    Glide.with(context).load(new File(imageCover)).override(200, 140).into(imgArtist);
                } else {
                    Glide.with(context).load(lyric.getMusicImage()).override(200, 140).into(imgArtist);
                }
            }
        } else {
            txtLyric.setText(StringUtils.getStringRes(R.string.msg_lyric_not_found));
        }
    }
}
