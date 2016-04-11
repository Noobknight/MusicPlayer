package com.tadev.musicplayer.abstracts;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

import com.tadev.musicplayer.MusicPlayerApplication;
import com.tadev.musicplayer.R;

/**
 * Created by Iris Louis on 25/03/2016.
 */
public abstract class BaseMenuActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public FragmentManager mFragmentManager;
    public FragmentTransaction transaction;
    protected MusicPlayerApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutById());
        Log.i(TAG(), "onCreate ");
        mFragmentManager = getSupportFragmentManager();
        application = MusicPlayerApplication.getInstance();
        setTitle("");
        initToolbar();
        initView(savedInstanceState);
        setEvents();
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
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            toolbar.setPadding(0,getStatusBarHeight(),0,0);
        }
    }


    private Toolbar findToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected MusicPlayerApplication getMusicPlayerApplication(){
        return application;
    }


    protected abstract String TAG();

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0){
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
