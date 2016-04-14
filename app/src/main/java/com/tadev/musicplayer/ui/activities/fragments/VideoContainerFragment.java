package com.tadev.musicplayer.ui.activities.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.adapters.BaseViewPagerAdapter;
import com.tadev.musicplayer.utils.design.viewpager.PagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * Created by Iris Louis on 13/04/2016.
 */
public class VideoContainerFragment extends BaseFragment {
    public static final String TAG = "VideoContainerFragment";
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;

    public static VideoContainerFragment newInstance() {
        return new VideoContainerFragment();
    }

    @Override
    protected int setLayoutById() {
        return R.layout.fragment_video_content;
    }

    @Override
    protected void initView(View view) {
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.fragment_video_list_slidingtabs);
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_video_list_viewpager);
    }

    @Override
    protected void initViewData() {
        mActivityMain.getToolbar().setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        mActivityMain.getToolbar().setTitle("Video");
        mViewPager.setAdapter(new BaseViewPagerAdapter(getChildFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);
        setUpTabs(mViewPager,mPagerSlidingTabStrip);
    }

    public void setUpTabs(ViewPager viewPager, PagerSlidingTabStrip pagerSlidingTabStrip) {
        pagerSlidingTabStrip.setViewPager(viewPager);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void setViewEvents() {

    }

    @Override
    protected String TAG() {
        return TAG;
    }

}
