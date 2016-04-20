package com.tadev.musicplayer.supports.design;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Iris Louis on 29/03/2016.
 */
public class TextViewTitle extends TextView {
    public TextViewTitle(Context context) {
        super(context);
        init();
    }

    public TextViewTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextViewTitle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/VNF-LeagueGothicRegular.ttf");
        setTypeface(tf);
    }

}
