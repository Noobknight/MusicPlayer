package com.tadev.musicplayer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tadev.musicplayer.common.Api;
import com.tadev.musicplayer.ui.activities.fragments.VideoFragment;

/**
 * Created by Iris Louis on 13/04/2016.
 */
public class BaseViewPagerAdapter extends FragmentStatePagerAdapter {
    private final String TAG = "BaseViewPagerAdapter";
    private final String[] title = {"VIỆT NAM", "CHÂU ÂU", "HÀN QUỐC"};

    public BaseViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new VideoFragment().newInstance(Api.getApiVideoVietNam());
            case 1:
                return new VideoFragment().newInstance(Api.getApiVideoUSUK());
            case 2:
                return new VideoFragment().newInstance(Api.getApiVideoKorea());
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return title.length;
    }


}
