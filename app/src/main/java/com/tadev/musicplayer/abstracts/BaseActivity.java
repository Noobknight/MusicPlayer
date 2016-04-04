package com.tadev.musicplayer.abstracts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Iris Louis on 24/03/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutById());
        initView();
        addEvents();
    }

    protected abstract void initView();

    protected abstract int setLayoutById();

    protected abstract void addEvents();
}
