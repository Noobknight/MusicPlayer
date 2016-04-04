package com.tadev.musicplayer;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tadev.musicplayer.abstracts.BaseMenuActivity;
import com.tadev.musicplayer.ui.fragments.MusicKoreaFragment;
import com.tadev.musicplayer.ui.fragments.MusicUsUkFragment;
import com.tadev.musicplayer.ui.fragments.MusicVietNamFragment;

public class MainActivity extends BaseMenuActivity implements
        FragmentManager.OnBackStackChangedListener {
    private final String TAG = "MainActivity";
    private final String LAST_ITEM_CHECKED = "last_item_checked";
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private int mLastItemChecked;


    @Override
    protected int layoutById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mFragmentManager.addOnBackStackChangedListener(this);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.left_drawer);
        if (savedInstanceState != null) {
            if (mLastItemChecked != R.id.music_vietnam) {
                mNavigationView.getMenu().findItem(R.id.music_vietnam).setChecked(false);
                mNavigationView.getMenu().findItem(mLastItemChecked).setChecked(true);
            }
        } else {
            selectItem(R.id.music_vietnam);
        }
    }

    @Override
    protected void setEvents() {
        setupNavigationDrawerContent(mNavigationView);
        mDrawerToggle = new

                ActionBarDrawerToggle(MainActivity.this, mDrawer, 0, 0) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                }

        ;
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


    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int navItemId = menuItem.getItemId();
                selectItem(navItemId);
                return true;
            }
        });
    }


    private void selectItem(int itemIds) {
        int navItemId = itemIds;
        transaction = mFragmentManager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.transition_slide_in_bottom, R.anim.transition_slide_out_bottom);
        switch (itemIds) {
            case R.id.music_vietnam:
                transaction.replace(R.id.container, MusicVietNamFragment.newInstance(),
                        MusicVietNamFragment.TAG)
                        .addToBackStack(MusicVietNamFragment.TAG);
                break;
            case R.id.music_korea:
                transaction.replace(R.id.container, MusicKoreaFragment.newInstance(),
                        MusicKoreaFragment.TAG);
                transaction.addToBackStack(MusicKoreaFragment.TAG);
                break;
            case R.id.music_us_uk:
                transaction.replace(R.id.container, MusicUsUkFragment.newInstance(),
                        MusicUsUkFragment.TAG)
                        .addToBackStack(MusicUsUkFragment.TAG);
                break;
            case R.id.music_artist:
                Toast.makeText(MainActivity.this, "Music Artist", Toast.LENGTH_SHORT).show();
                break;
            case R.id.music_ranking:
                Toast.makeText(MainActivity.this, "Music Rankings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.music_setting:
                Toast.makeText(MainActivity.this, "Music Settings", Toast.LENGTH_SHORT).show();
                break;
        }
        if (navItemId != R.id.music_vietnam) {
            mNavigationView.getMenu().findItem(R.id.music_vietnam).setChecked(false);
        }
        transaction.commit();
        mDrawer.closeDrawer(GravityCompat.START);
        mLastItemChecked = navItemId;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LAST_ITEM_CHECKED, mLastItemChecked);
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(mNavigationView)) {
            mDrawer.closeDrawer(mNavigationView);
            return;
        }
        super.onBackPressed();
//        if (mFragmentManager.getBackStackEntryCount() > 0) {
//            mFragmentManager.popBackStackImmediate();
//            Fragment fragment = mFragmentManager.findFragmentById(R.id.container);
//            if (fragment instanceof MusicVietNamFragment) {
//                mNavigationView.setCheckedItem(R.id.music_vietnam);
//            } else if (fragment instanceof MusicKoreaFragment) {
//                mNavigationView.setCheckedItem(R.id.music_korea);
//            } else {
//                mNavigationView.setCheckedItem(R.id.music_us_uk);
//            }
//        } else {
//            super.onBackPressed();
//        }
    }

    @Override
    public void onBackStackChanged() {
        Log.i(TAG, "onBackStackChanged ");
    }

}
