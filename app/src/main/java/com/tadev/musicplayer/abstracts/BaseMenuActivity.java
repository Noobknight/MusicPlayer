package com.tadev.musicplayer.abstracts;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.squareup.otto.Bus;
import com.tadev.musicplayer.MusicPlayerApplication;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.utils.networks.BusWrapper;
import com.tadev.musicplayer.utils.networks.NetworkEvents;
import com.tadev.musicplayer.utils.support.DialogUtils;

/**
 * Created by Iris Louis on 25/03/2016.
 */
public abstract class BaseMenuActivity extends AppCompatActivity implements DialogUtils.DialogListener {
    private Toolbar toolbar;
    public FragmentManager mFragmentManager;
    public FragmentTransaction transaction;
    protected MusicPlayerApplication application;
    protected BusWrapper busWrapper;
    protected NetworkEvents networkEvents;
    protected DialogUtils dialogUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutById());
        Log.i(TAG(), "onCreate ");
        setTranslucentStatusBar();
        mFragmentManager = getSupportFragmentManager();
        application = MusicPlayerApplication.getInstance();
        busWrapper = getOttoBusWrapper(new Bus());
        networkEvents = new NetworkEvents(getApplicationContext(), busWrapper);
        networkEvents.enableInternetCheck();
        dialogUtils = new DialogUtils(this);
        setTitle("");
        initToolbar();
        initView(savedInstanceState);
        setEvents();
    }


    protected void setTranslucentStatusBar() {

    }

    protected abstract int layoutById();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void setEvents();


    protected void initToolbar() {
        toolbar = findToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }
    }


    private Toolbar findToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected MusicPlayerApplication getMusicPlayerApplication() {
        return application;
    }


    protected abstract String TAG();

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private BusWrapper getOttoBusWrapper(final Bus bus) {
        return new BusWrapper() {
            @Override
            public void register(Object object) {
                bus.register(object);
            }

            @Override
            public void unregister(Object object) {
                bus.unregister(object);
            }

            @Override
            public void post(Object event) {
                bus.post(event);
            }
        };
    }


}
