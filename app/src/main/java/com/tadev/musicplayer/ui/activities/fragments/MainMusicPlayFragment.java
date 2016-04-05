package com.tadev.musicplayer.ui.activities.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.adapters.ViewPagerApdater;
import com.tadev.musicplayer.interfaces.OnMusicInfoLoadListener;
import com.tadev.musicplayer.models.BaseModel;
import com.tadev.musicplayer.models.Lyric;
import com.tadev.musicplayer.models.Music;
import com.tadev.musicplayer.models.Song;
import com.tadev.musicplayer.services.loaders.MusicInfoLoaderTask;
import com.tadev.musicplayer.utils.design.blurry.BlurImageUtils;
import com.tadev.musicplayer.utils.design.viewpager.CircleIndicator;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class MainMusicPlayFragment extends BaseFragment implements OnMusicInfoLoadListener {
    public static final String TAG = "MainMusicPlayFragment";
    private static final String KEY_EXTRA = "extras";
    private ViewPager mViewPager;
    private ViewPagerApdater mAdapter;
    private CircleIndicator mCircleIndicator;
    private ImageView imgBackground;
    private BaseModel modelMusic;
    private Toolbar toolbar;
    private FrameLayout frameViewPager;
    private ProgressDialog dialogLoading;
    private Bitmap btmBlur;

    public static MainMusicPlayFragment newInstance(BaseModel baseModel) {
        MainMusicPlayFragment fragment = new MainMusicPlayFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_EXTRA, baseModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate ");
        setHasOptionsMenu(true);
    }

    @Override
    protected int setLayoutById() {
        return R.layout.fragment_main_music_play;
    }

    @Override
    protected void initView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mCircleIndicator = (CircleIndicator) view.findViewById(R.id.circleIndicator);
        imgBackground = (ImageView) view.findViewById(R.id.fragment_main_music_play_imgBackground);
        frameViewPager = (FrameLayout) view.findViewById(R.id.frameViewPager);
        initToolbar();
    }

    @Override
    protected void initViewData() {
//        mAdapter = new ViewPagerApdater(getChildFragmentManager(), initSongData(music), initLyricData(music));
//        mViewPager.setAdapter(mAdapter);
//        mCircleIndicator.setViewPager(mViewPager);
    }

    @Override
    protected void setViewEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                baseMenuActivity.mFragmentManager.popBackStack();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        baseMenuActivity.getToolbar().setVisibility(View.VISIBLE);
                    }
                }, getResources().getInteger(R.integer.fragmentAnimationTime));
            }
        });
        new MusicInfoLoaderTask(modelMusic.getMusic_id(), modelMusic.getMusic_title_url(), this)
                .execute();
    }

    @Override
    protected String TAG() {
        return TAG;
    }


    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button
                    Handler handler = new Handler();
                    baseMenuActivity.mFragmentManager.popBackStack();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            baseMenuActivity.getToolbar().setVisibility(View.VISIBLE);
                        }
                    }, getResources().getInteger(R.integer.fragmentAnimationTime));
                    return true;

                }
                return false;
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_playing_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initToolbar() {
        if (toolbar != null) {
            baseMenuActivity.setSupportActionBar(toolbar);
            ActionBar actionBar = baseMenuActivity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    @Override
    public void onTaskLoadCompleted(final Music musicReponse) {
        dialogLoading.dismiss();
        new Thread(new Runnable() {
            @Override
            public void run() {
                imgBackground.post(new Runnable() {
                    @Override
                    public void run() {
                        boolean isImageEmpty = musicReponse.getMusicImg() == null
                                || musicReponse.getMusicImg().isEmpty();
                        if (isImageEmpty) {
                            imgBackground.buildDrawingCache();
                            btmBlur = imgBackground.getDrawingCache();
                            btmBlur = BlurImageUtils.blurRenderScript(context, btmBlur, 10);
                            imgBackground.setImageBitmap(btmBlur);
                        } else {
                            loadImageBlur(musicReponse.getMusicImg());
//                            btmBlur = BlurImageUtils.blurRenderScript(context,
//                                    ImageUtils.loadBitmap(musicReponse.getMusicImg()),
//                                    10);
//                            imgBackground.setImageBitmap(btmBlur);
                        }
                    }
                });
            }
        }).start();
        mAdapter = new ViewPagerApdater(getChildFragmentManager(), initSongData(musicReponse),
                initLyricData(musicReponse), btmBlur);
        mViewPager.setAdapter(mAdapter);
        mCircleIndicator.setViewPager(mViewPager);
    }

    @Override
    public void onTaskLoadFailed(Exception e) {
        dialogLoading.dismiss();
        Toast.makeText(context, "Load failed !!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPreparing() {
        dialogLoading = ProgressDialog.show(context, "", "Waiting...");
    }


    private Song initSongData(Music music) {
        if (music != null) {
            Song song = new Song();
            song.setMusicId(music.getMusicId());
            song.setMusicTitleUrl(music.getMusicTitleUrl());
            song.setMusicTitle(music.getMusicTitle());
            song.setMusicArtist(music.getMusicArtist());
            song.setMusicImg(music.getMusicImg());
            song.setFileUrl(music.getFileUrl());
            song.setFile320Url(music.getFile320Url());
            song.setFileM4aUrl(music.getFileM4aUrl());
            return song;
        }
        return null;

    }

    private Lyric initLyricData(Music music) {
        if (music != null) {
            Lyric lyric = new Lyric();
            lyric.setMusicTitle(music.getMusicTitle());
            lyric.setMusicArtist(music.getMusicArtist());
            lyric.setMusicAlbum(music.getMusicAlbum());
            lyric.setMusicComposer(music.getMusicComposer());
            lyric.setMusicLyric(music.getMusicLyric());
            lyric.setMusicProduction(music.getMusicProduction());
            lyric.setMusicYear(music.getMusicYear());
            return lyric;
        }
        return null;

    }

    @Override
    protected void getDataCallBack() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            modelMusic = bundle.getParcelable(KEY_EXTRA);
        }
    }

    private void loadImageBlur(String url) {
        Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                btmBlur = BlurImageUtils.blurRenderScript(context, bitmap, 10);
                imgBackground.setImageBitmap(btmBlur);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                imgBackground.buildDrawingCache();
                btmBlur = imgBackground.getDrawingCache();
                btmBlur = BlurImageUtils.blurRenderScript(context, btmBlur, 10);
                imgBackground.setImageBitmap(btmBlur);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                imgBackground.setImageDrawable(placeHolderDrawable);
            }
        });
    }
}
