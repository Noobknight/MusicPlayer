package com.tadev.musicplayer.supports.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tadev.musicplayer.R;

/**
 * Created by Iris Louis on 25/03/2016.
 */
public class ParallaxView extends FrameLayout {

    public float parallaxVertical;
    public float parallaxHorizontal;

    private void handleAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParallaxView);
        parallaxVertical = a.getFloat(R.styleable.ParallaxView_parallaxVertical, 0);
        parallaxHorizontal = a.getFloat(R.styleable.ParallaxView_parallaxHorizontal, 0);
        a.recycle();
    }

    public ParallaxView(Context context) {
        super(context);
    }

    public ParallaxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttributes(context, attrs);
    }

    public ParallaxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        handleAttributes(context, attrs);
    }
}
