package com.tadev.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tadev.musicplayer.abstracts.BaseMenuActivity;
import com.tadev.musicplayer.callbacks.OnRegisterCallback;
import com.tadev.musicplayer.interfaces.IServicePlayer;
import com.tadev.musicplayer.interfaces.OnPlayBarBottomListener;
import com.tadev.musicplayer.models.music.CurrentSongPlay;
import com.tadev.musicplayer.services.MusicPlayService;
import com.tadev.musicplayer.ui.activities.fragments.MainMusicPlayFragment;
import com.tadev.musicplayer.ui.activities.fragments.MainMusicPlayFragment.OnBackFragmentListener;
import com.tadev.musicplayer.ui.activities.fragments.MusicKoreaFragment;
import com.tadev.musicplayer.ui.activities.fragments.MusicUsUkFragment;
import com.tadev.musicplayer.ui.activities.fragments.MusicVietNamFragment;
import com.tadev.musicplayer.ui.activities.fragments.PlayBarBottomFragment;
import com.tadev.musicplayer.ui.activities.fragments.VideoContainerFragment;

public class MainActivity extends BaseMenuActivity implements OnRegisterCallback
        , IServicePlayer, OnBackFragmentListener, OnPlayBarBottomListener,
        View.OnClickListener {
    private final String TAG = "MainActivity";
    public static final String UPDATE_MUSIC_PLAYBAR = "com.tadev.musicplayer.UPDATE_MUSIC_PLAYBAR";
    public static final String EXTRA_CURRENT_PLAY = "current_play";
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private int mLastItemChecked;
    private Intent intentRegistedService;
    private MusicPlayService mService;
    private PlayBarBottomFragment mPlayBarBottomFragment;
    //    private FrameLayout containerBottom;
    private CardView cardPlayBar;
    private Handler handler;
    private LocalBroadcastManager localBroadcastManager;
    private boolean isBound;


    @Override
    protected int layoutById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        handler = new Handler();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        bindService();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.left_drawer);
//        containerBottom = (FrameLayout) findViewById(R.id.container_bottom);
        cardPlayBar = (CardView) findViewById(R.id.cardPlayBar);
//        containerBottom.setOnClickListener(this);
        cardPlayBar.setOnClickListener(this);
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

//        transaction.setCustomAnimations(R.anim.transition_slide_in_bottom, R.anim.transition_slide_out_bottom);
//        if (mService != null) {
//            hidePlayBarBottom();
//        }
        transaction = mFragmentManager.beginTransaction();
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
            case R.id.music_video:
                transaction.replace(R.id.container, VideoContainerFragment.newInstance(),
                        VideoContainerFragment.TAG)
                        .addToBackStack(VideoContainerFragment.TAG);
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
        showPlayBarBottom();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        final String LAST_ITEM_CHECKED = "last_item_checked";
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
        Log.i(TAG, "onBackPressed " + mFragmentManager.getBackStackEntryCount());
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            this.finish();
        }

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
        if (isBound && intentRegistedService != null) {
            Log.i(TAG, "onDestroy here");
            unbindService(mPlayServiceConnection);
            stopService(intentRegistedService);
        }
        super.onDestroy();
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

    @Override
    public void onDownloadRegister(Intent intent) {
        startService(intent);
    }

    private ServiceConnection mPlayServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MusicPlayService.PlayBinder) service).getService();
            mService.setOnMusicPlayListener(MainActivity.this);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            isBound = false;
        }
    };

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, MusicPlayService.class);
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public MusicPlayService getService() {
        return mService;
    }


    private void showPlayBarBottom() {
        if (mPlayBarBottomFragment != null) {
            transaction = mFragmentManager.beginTransaction();
            transaction.show(mPlayBarBottomFragment);
            transaction.commit();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    containerBottom.setVisibility(View.VISIBLE);
                    cardPlayBar.setVisibility(View.VISIBLE);
                }
            }, getResources().getInteger(R.integer.fragmentAnimationTime));
        }
    }


    private void hidePlayBarBottom() {
        transaction = mFragmentManager.beginTransaction();
        transaction.hide(mPlayBarBottomFragment);
        transaction.commit();
//        containerBottom.setVisibility(View.GONE);
        cardPlayBar.setVisibility(View.GONE);
    }

    private void initPlayBarBottom() {
        if (mPlayBarBottomFragment == null) {
            transaction = mFragmentManager.beginTransaction();
            mPlayBarBottomFragment = new PlayBarBottomFragment();
            transaction.replace(R.id.container_bottom, mPlayBarBottomFragment);
            transaction.commit();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    containerBottom.setVisibility(View.VISIBLE);
                    cardPlayBar.setVisibility(View.VISIBLE);
                }
            }, getResources().getInteger(R.integer.fragmentAnimationTime));
        }

    }


    @Override
    public void duration(int duration) {
        application.getMusicContainer().getmCurrentSongPlay().duration = duration;
    }

    @Override
    public void position(int currentPosition) {
        application.getMusicContainer().getmCurrentSongPlay().position = currentPosition;
    }

    @Override
    public void currentID(int currentId) {

    }

    @Override
    public void onChange(int musicId) {
    }

    @Override
    public void onPublish(int progress) {
        application.getMusicContainer().getmCurrentSongPlay().progress = progress;
    }

    @Override
    public void currentSongPlay(CurrentSongPlay currentSongPlay) {
        sendMessage(currentSongPlay);
    }

    @Override
    public void onBack(boolean isBack) {
        if (intentRegistedService != null && mPlayBarBottomFragment == null) {
            initPlayBarBottom();
        } else if (isBack) {
            showPlayBarBottom();
        }

    }

    public boolean isBound() {
        return isBound;
    }

    @Override
    public void onPlayBarShowHide(boolean enable) {
        if (mPlayBarBottomFragment != null) {
            hidePlayBarBottom();
        }
    }

    private void sendMessage(CurrentSongPlay currentSongPlay) {
        Intent intent = new Intent(UPDATE_MUSIC_PLAYBAR);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CURRENT_PLAY, currentSongPlay);
        intent.putExtras(bundle);
        localBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.container_bottom:
                hidePlayBarBottom();
                transaction = mFragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.transition_slide_in_bottom, R.anim.transition_slide_out_bottom,
                        R.anim.transition_slide_in_bottom, R.anim.transition_slide_out_bottom);
                transaction.replace(R.id.container, MainMusicPlayFragment.newInstance(true));
                transaction.addToBackStack(MainMusicPlayFragment.TAG);
                transaction.commit();
                getToolbar().setVisibility(View.INVISIBLE);
                break;
            case R.id.cardPlayBar:
                hidePlayBarBottom();
                transaction = mFragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.transition_slide_in_bottom, R.anim.transition_slide_out_bottom,
                        R.anim.transition_slide_in_bottom, R.anim.transition_slide_out_bottom);
                transaction.replace(R.id.container, MainMusicPlayFragment.newInstance(true));
                transaction.addToBackStack(MainMusicPlayFragment.TAG);
                transaction.commit();
                getToolbar().setVisibility(View.INVISIBLE);
                break;

        }
    }
}
