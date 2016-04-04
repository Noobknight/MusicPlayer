package com.tadev.musicplayer.utils.design;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.flaviofaria.kenburnsview.KenBurnsView;


/**
 * Created by Iris Louis on 25/03/2016.
 */
public class MaterialImageHeader extends KenBurnsView {
    //region construct

    public MaterialImageHeader(Context context) {
        super(context);
    }

    public MaterialImageHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialImageHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //endregion

    /**
     * change the image with a fade
     * @param urlImage
     * @param fadeDuration
     *
     * TODO : remove Picasso
     */
//    public void setImageUrl(final String urlImage, final int fadeDuration) {
//        MaterialImageHelper.setImageUrl(this, urlImage, fadeDuration);
//    }

    /**
     * change the image with a fade
     * @param drawable
     * @param fadeDuration
     */
    public void setImageDrawable(final Drawable drawable, final int fadeDuration) {
        MaterialImageHelper.setImageDrawable(this, drawable, fadeDuration);
    }
}
