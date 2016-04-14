package com.tadev.musicplayer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tadev.musicplayer.models.music.Lyric;
import com.tadev.musicplayer.models.music.Song;
import com.tadev.musicplayer.ui.activities.fragments.MusicLyricFragment;
import com.tadev.musicplayer.ui.activities.fragments.MusicPlayingFragment;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class ViewPagerApdater extends FragmentPagerAdapter {
    private final String TAG = "ViewPagerApdater";
    private Song song;
    private Lyric lyric;

    public ViewPagerApdater(FragmentManager fm, Song song, Lyric lyric) {
        super(fm);
        this.song = song;
        this.lyric = lyric;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MusicPlayingFragment.newInstance(song, lyric);
            case 1:
                return MusicLyricFragment.newInstance(lyric);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}
