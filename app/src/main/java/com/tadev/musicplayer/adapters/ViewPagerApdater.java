package com.tadev.musicplayer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tadev.musicplayer.fragments.MusicLyricFragment;
import com.tadev.musicplayer.fragments.MusicPlayingFragment;
import com.tadev.musicplayer.interfaces.UpdateableFragment;
import com.tadev.musicplayer.models.music.CurrentSongPlay;
import com.tadev.musicplayer.models.music.Lyric;
import com.tadev.musicplayer.models.music.Song;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class ViewPagerApdater extends FragmentPagerAdapter {
    private final String TAG = "ViewPagerApdater";
    private Song song;
    private Lyric lyric;
    private CurrentSongPlay mSongPlay;

    public ViewPagerApdater(FragmentManager fm, Song song, Lyric lyric) {
        super(fm);
        this.song = song;
        this.lyric = lyric;
        mSongPlay = new CurrentSongPlay();
        mSongPlay.song = song;
        mSongPlay.lyric = lyric;

    }

    //call this method to update fragments in ViewPager dynamically
    public void update(CurrentSongPlay mSongPlay) {
        this.mSongPlay = mSongPlay;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof UpdateableFragment) {
            ((UpdateableFragment) object).update(mSongPlay);
        }
        return super.getItemPosition(object);
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
