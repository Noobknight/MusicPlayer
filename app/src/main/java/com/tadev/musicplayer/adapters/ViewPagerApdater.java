package com.tadev.musicplayer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.tadev.musicplayer.interfaces.ISongInfoListener;
import com.tadev.musicplayer.models.Lyric;
import com.tadev.musicplayer.models.Song;
import com.tadev.musicplayer.ui.fragments.MusicLyricFragment;
import com.tadev.musicplayer.ui.fragments.MusicPlayingFragment;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class ViewPagerApdater extends FragmentPagerAdapter implements ISongInfoListener {
    private final String TAG = "ViewPagerApdater";
    private ISongInfoListener iSongInfoListener;

    public ViewPagerApdater(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerApdater(FragmentManager fragmentManager, ISongInfoListener iSongInfoListener) {
        super(fragmentManager);
        this.iSongInfoListener = iSongInfoListener;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MusicPlayingFragment.newInstance(new Song());
            case 1:
                return MusicLyricFragment.newInstance(new Lyric());
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public void onDataMusicCallBack(Song song) {
        Log.i(TAG, "onDataMusicCallBack " + song);
    }
}
