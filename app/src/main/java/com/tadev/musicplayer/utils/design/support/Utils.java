package com.tadev.musicplayer.utils.design.support;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tadev.musicplayer.R;

/**
 * Created by Iris Louis on 25/03/2016.
 */
public class Utils {

    public static int getColorRes(Context context, int colorId){
        return ContextCompat.getColor(context, colorId);
    }

    public static int getDimensRes(Context context,int dimens){
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

    public static String getTitleVN(Context context){
        return context.getResources().getString(R.string.category_vietnam);
    }
    public static String getTitleKorea(Context context){
        return context.getResources().getString(R.string.category_korea);
    }
    public static String getTitleUS(Context context){
        return context.getResources().getString(R.string.category_us_uk);
    }
}
