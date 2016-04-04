package com.tadev.musicplayer.ui.fragments.activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.utils.design.MaterialImageHeader;
import com.tadev.musicplayer.utils.design.ScrollView;
import com.tadev.musicplayer.utils.design.support.Utils;

/**
 * Created by Iris Louis on 25/03/2016.
 */
public class ParallaxActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {
    private final String TAG = "ParallaxActivity";
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    //    private View mToolbarView;
    private ScrollView mScrollView;
    private int mParallaxImageHeight;
    private MaterialImageHeader kenburnsView;
    private FrameLayout header;
    private int mActionBarSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_music_vietnam);
        setTitle("");
        mActionBarSize = getActionBarSize();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mScrollView = (ScrollView) findViewById(R.id.overvableScrollView);
        header = (FrameLayout) findViewById(R.id.header);
        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        kenburnsView = (MaterialImageHeader) findViewById(R.id.imageHeader);
        kenburnsView.setImageDrawable(ContextCompat.getDrawable(ParallaxActivity.this, R.drawable.header_viewpager_cn_480), 300);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawer.closeDrawers();
                return false;
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(ParallaxActivity.this, mDrawer, 0, 0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawer.addDrawerListener(mDrawerToggle);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDownMotionEvent() {
        //Do Something
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        //Do something
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        int baseColor = Utils.getColorRes(this, R.color.colorPrimary);
        // Change alpha of overlay
//        float scrollYAlpha = ScrollUtils.getFloat((float) scrollY / mParallaxImageHeight, 0, 1);
        float scrollYAlpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        float alpha = 1.0f - scrollYAlpha;
//        int x = ScrollUtils.getColorWithAlpha(alpha, baseColor);
        ViewHelper.setAlpha(kenburnsView, alpha);

//        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
//        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        //Let's change background's color from blue to red.
//        ColorDrawable[] color = {new ColorDrawable(Color.TRANSPARENT), new ColorDrawable(baseColor)};
//        TransitionDrawable trans = new TransitionDrawable(color);
        //This will work also on old devices. The latest API says you have to use setBackground instead.
//        btn.setBackgroundDrawable(trans);

        if ((float) scrollY >= mParallaxImageHeight) {
            toolbar.setBackgroundColor(baseColor);
        } else {
            toolbar.setBackgroundColor(Utils.getColorRes(this, android.R.color.transparent));
        }

        ViewHelper.setTranslationY(kenburnsView, scrollY / 2);
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }
}
