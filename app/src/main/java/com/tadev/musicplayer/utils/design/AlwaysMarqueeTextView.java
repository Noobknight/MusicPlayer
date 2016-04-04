package com.tadev.musicplayer.utils.design;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public class AlwaysMarqueeTextView extends TextView {
    protected boolean a;

    public AlwaysMarqueeTextView(Context context) {
        super(context);
        a = false;
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        a = false;
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attributeset, int i) {
        super(context, attributeset, i);
        a = false;
    }

    public boolean isFocused() {
        return a || super.isFocused();
    }

    public void setAlwaysMarquee(boolean flag) {
        setSelected(flag);
        setSingleLine(flag);
        if (flag)
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
        a = flag;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused)

            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if (focused)
            super.onWindowFocusChanged(focused);
    }
}