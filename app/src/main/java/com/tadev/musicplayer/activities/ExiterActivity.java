package com.tadev.musicplayer.activities;

/**
 * Created by Iris Louis on 17/10/2015.
 */
import android.app.Activity;
import android.os.Bundle;

public class ExiterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();

        // Android will clean up the process automatically. But hey, if you
        // need to kill the process yourself, don't let me stop you.
        System.exit(0);
    }
}