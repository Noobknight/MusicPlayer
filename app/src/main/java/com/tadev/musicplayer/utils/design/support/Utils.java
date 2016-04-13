package com.tadev.musicplayer.utils.design.support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tadev.musicplayer.MainActivity;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.constant.Constants;

import java.text.DecimalFormat;

/**
 * Created by Iris Louis on 25/03/2016.
 */
public class Utils {

    public static int getColorRes(Context context, int colorId) {
        return ContextCompat.getColor(context, colorId);
    }

    public static int getDimensRes(Context context, int dimens) {
        return context.getResources().getDimensionPixelSize(dimens);
    }

    public static void animateOut(View view, final Context context) {
        Animation slideAnim = AnimationUtils.loadAnimation(context, R.anim.transition_slide_in_bottom);
        slideAnim.setFillAfter(true);
        slideAnim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation paramAnimation) {
            }

            public void onAnimationRepeat(Animation paramAnimation) {
            }

            public void onAnimationEnd(Animation paramAnimation) {
                ((Activity) context).finish();
                // if you call NavUtils.navigateUpFromSameTask(activity); instead,
                // the screen will flicker once after the animation. Since FrontActivity is
                // in front of BackActivity, calling finish() should give the same result.
                ((Activity) context).overridePendingTransition(0, 0);
            }
        });
        view.startAnimation(slideAnim);
    }

    public static String getTitleVN(Context context) {
        return context.getResources().getString(R.string.category_vietnam);
    }

    public static String getTitleKorea(Context context) {
        return context.getResources().getString(R.string.category_korea);
    }

    public static String getTitleUS(Context context) {
        return context.getResources().getString(R.string.category_us_uk);
    }


    public static String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf.append(
                String.format("%02d", hours)).append(":")
                .append(String.format("%02d", minutes)).append(":")
                .append(String.format("%02d", seconds));
//        buf.append(String.format("%02d", minutes)).append(":")
//                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    public static int getProgressPercentage(long currentDuration,
                                            long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static Intent getNowPlayingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constants.NAVIGATE_NOWPLAYING);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return " ";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat(" #,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
