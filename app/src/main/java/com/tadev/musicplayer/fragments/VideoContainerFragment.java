package com.tadev.musicplayer.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.adapters.BaseViewPagerAdapter;
import com.tadev.musicplayer.interfaces.OnBackFragmentListener;
import com.tadev.musicplayer.utils.design.viewpager.PagerSlidingTabStrip;

/**
 * Created by Iris Louis on 13/04/2016.
 */
public class VideoContainerFragment extends BaseFragment {
    public static final String TAG = "VideoContainerFragment";
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private OnBackFragmentListener mOnBackFragmentListener;
    private static boolean isBackFromVideoActivity;
    private LinearLayout lrlSlidingLayout;

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
        lrlSlidingLayout = (LinearLayout) view.findViewById(R.id.fragment_video_content_sliding);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int paddingHeight = getResources().getDimensionPixelSize(R.dimen.padding_sliding_tab);
            lrlSlidingLayout.setPadding(0, paddingHeight, 0, 0);
        }
    }

    @Override
    protected void initViewData() {
        mActivityMain.getToolbar().setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        mActivityMain.getToolbar().setTitle("Video");
        mViewPager.setAdapter(new BaseViewPagerAdapter(getChildFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);
        setUpTabs(mViewPager, mPagerSlidingTabStrip);
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

    public static class BackEvent extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                isBackFromVideoActivity = true;
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isBackFromVideoActivity) {
            mOnBackFragmentListener.onBack(true);
            isBackFromVideoActivity = false;
        }
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    public void onAttach(Context context) {
        AppCompatActivity activity;
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
            try {
                mOnBackFragmentListener = (OnBackFragmentListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " class must be implement" +
                        " OnBackFragmentListener");
            }
        }
        super.onAttach(context);
    }

}
