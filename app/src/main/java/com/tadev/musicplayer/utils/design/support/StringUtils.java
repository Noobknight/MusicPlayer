package com.tadev.musicplayer.utils.design.support;

import android.content.Context;

/**
 * Created by Iris Louis on 06/04/2016.
 */
public class StringUtils {
    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static String getStringRes(int idStringRes) {
        return sContext.getResources().getString(idStringRes);
    }
}
