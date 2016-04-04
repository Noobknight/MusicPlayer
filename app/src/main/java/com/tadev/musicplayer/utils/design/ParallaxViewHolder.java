package com.tadev.musicplayer.utils.design;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Iris Louis on 25/03/2016.
 */
public class ParallaxViewHolder {
    View view;
    Float parallaxY;
    Float parallaxX;

    public ParallaxViewHolder(View view, Float parallaxY) {
        this.view = view;
        this.parallaxY = parallaxY;
    }

    public ParallaxViewHolder(ParallaxView parallaxView) {
        this.view = parallaxView;
        this.parallaxX = parallaxView.parallaxHorizontal;
        this.parallaxY = parallaxView.parallaxVertical;
    }

    public void onParallax(int offset) {
        if (parallaxY != null)
            ViewHelper.setTranslationY(view, offset * parallaxY);
        if (parallaxX != null)
            ViewHelper.setTranslationX(view, offset * parallaxX);
    }
}
