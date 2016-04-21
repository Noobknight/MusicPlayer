package com.tadev.musicplayer.utils.support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tadev.musicplayer.MainActivity;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.activities.ExiterActivity;
import com.tadev.musicplayer.constant.Constants;

import java.text.DecimalFormat;

/**
 * Created by Iris Louis on 25/03/2016.
 */
public class Utils {
    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static int getColorRes(int colorId) {
        return ContextCompat.getColor(sContext, colorId);
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

    public static String getTitleVN(Context context, int typeTitle) {
        switch (typeTitle) {
            case Constants.TYPE_TTTLE_MUSIC:
                return context.getResources().getString(R.string.category_vietnam);
            case Constants.TYPE_TTTLE_VIDEO:
                return context.getResources()
                        .getString(R.string.category_vietnam).replace("Nhạc", "Video");
            default:
                return null;
        }
    }

    public static String getTitleKorea(Context context, int typeTitle) {
        switch (typeTitle) {
            case Constants.TYPE_TTTLE_MUSIC:
                return context.getResources().getString(R.string.category_korea);
            case Constants.TYPE_TTTLE_VIDEO:
                return context.getResources()
                        .getString(R.string.category_korea).replace("Nhạc", "Video");
            default:
                return null;
        }
    }

    public static String getTitleUS(Context context, int typeTitle) {
        switch (typeTitle) {
            case Constants.TYPE_TTTLE_MUSIC:
                return context.getResources().getString(R.string.category_us_uk);
            case Constants.TYPE_TTTLE_VIDEO:
                return context.getResources()
                        .getString(R.string.category_us_uk).replace("Nhạc", "Video");
            default:
                return null;
        }
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

    public static String getTimeDuration(long millis) {
        StringBuffer buf = new StringBuffer();

        // int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf.append(String.format("%02d", minutes)).append(":")
                .append(String.format("%02d", seconds));

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

    public static void NavigationAnimation(View view, int animId) {
        Animation slideAnim = AnimationUtils.loadAnimation(sContext, R.anim.slide_out_bottom);
        slideAnim.setFillAfter(true);
        slideAnim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation paramAnimation) {
            }

            public void onAnimationRepeat(Animation paramAnimation) {
            }

            public void onAnimationEnd(Animation paramAnimation) {
                ((Activity) sContext).finish();
                // if you call NavUtils.navigateUpFromSameTask(activity); instead,
                // the screen will flicker once after the animation. Since FrontActivity is
                // in front of BackActivity, calling finish() should give the same result.
                ((Activity) sContext).overridePendingTransition(0, 0);
            }
        });
        view.startAnimation(slideAnim);
    }


    public static String getColorTransparent(int baseColor, int percent) {
        String colorPrimary = Integer.toHexString(ContextCompat.getColor(sContext, baseColor));
        colorPrimary = colorPrimary.substring(2).toUpperCase();
        String hexTransparent;
        switch (percent) {
            case 0:
                hexTransparent = "#00";
                break;
            case 5:
                hexTransparent = "#0D";
                break;
            case 10:
                hexTransparent = "#1A";
                break;
            case 15:
                hexTransparent = "#26";
                break;
            case 20:
                hexTransparent = "#33";
                break;
            case 25:
                hexTransparent = "#40";
                break;
            case 30:
                hexTransparent = "#4D";
                break;
            case 35:
                hexTransparent = "#59";
                break;
            case 40:
                hexTransparent = "#66";
                break;
            case 45:
                hexTransparent = "#73";
                break;
            case 50:
                hexTransparent = "#80";
                break;
            case 55:
                hexTransparent = "#8C";
                break;
            case 60:
                hexTransparent = "#99";
                break;
            case 65:
                hexTransparent = "#A6";
                break;
            case 70:
                hexTransparent = "#B3";
                break;
            case 75:
                hexTransparent = "#BF";
                break;
            case 80:
                hexTransparent = "#CC";
                break;
            case 85:
                hexTransparent = "#D9";
                break;
            case 90:
                hexTransparent = "#E6";
                break;
            case 95:
                hexTransparent = "#F2";
                break;
            case 100:
                hexTransparent = "#FF";
                break;
            default:
                hexTransparent = "#FF";
                break;
        }
        return hexTransparent + colorPrimary;
    }


    public static void finishAndHide(Context context) {
        final Intent relaunch = new Intent(context, ExiterActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK // CLEAR_TASK requires
                        // this
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK // finish everything
                        // else in the task
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); // hide
        context.startActivity(relaunch);
    }

}
