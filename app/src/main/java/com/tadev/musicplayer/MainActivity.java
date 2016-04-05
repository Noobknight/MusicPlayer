package com.tadev.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import com.tadev.musicplayer.callbacks.OnRegisterCallback;
import com.tadev.musicplayer.interfaces.IServicePlayer;
import com.tadev.musicplayer.models.CurrentSongPlay;
import com.tadev.musicplayer.services.MusicPlayService;
import com.tadev.musicplayer.ui.activities.fragments.MusicKoreaFragment;
import com.tadev.musicplayer.ui.activities.fragments.MusicUsUkFragment;
import com.tadev.musicplayer.ui.activities.fragments.MusicVietNamFragment;

public class MainActivity extends BaseMenuActivity implements
        FragmentManager.OnBackStackChangedListener, OnRegisterCallback
        , IServicePlayer {
    private final String TAG = "MainActivity";
    private final String LAST_ITEM_CHECKED = "last_item_checked";
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private int mLastItemChecked;
    private ServiceConnection mServiceConnection;
    private Intent intentRegistedService;
    private MusicPlayService mService;


    @Override
    protected int layoutById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        bindService();
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
    protected void onDestroy() {
        if (mPlayServiceConnection != null || intentRegistedService != null) {
            Log.i(TAG, "onDestroy here");
            unbindService(mPlayServiceConnection);
            stopService(intentRegistedService);
        }
        super.onDestroy();
    }

    @Override
    public void onBackStackChanged() {
        Log.i(TAG, "onBackStackChanged ");
    }


    @Override
    public void onServiceRegister(Intent intent, ServiceConnection mServiceConnection) {
//        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
//        this.mServiceConnection = mServiceConnection;
    }

    @Override
    public void onServicePreparing(Intent intent) {
        intentRegistedService = intent;
        startService(intent);
    }

    @Override
    public void onUnRegisterReceiver(boolean enable) {

    }

    private ServiceConnection mPlayServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MusicPlayService.PlayBinder) service).getService();
            mService.setOnMusicPlayListener(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, MusicPlayService.class);
//        mOnRegisterCallback.onServiceRegister(intent, mPlayServiceConnection);
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
//        mServiceCallBack.onServiceCallback(mPlayServiceConnection);
    }

    public MusicPlayService getService() {
        return mService;
    }


    @Override
    public void duration(int duration) {
        Log.i(TAG, "duration " + duration);
    }

    @Override
    public void position(int currentPosition) {

    }

    @Override
    public void currentID(int currentId) {

    }

    @Override
    public void onChange(int musicId) {

    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void currentSongPlay(CurrentSongPlay currentSongPlay) {

    }
}
