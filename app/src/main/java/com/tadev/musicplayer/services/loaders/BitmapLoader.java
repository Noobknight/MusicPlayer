package com.tadev.musicplayer.services.loaders;

import android.graphics.Bitmap;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by Iris Louis on 08/04/2016.
 */
public class BitmapLoader extends SimpleTarget<Bitmap> {
    public interface BitmapLoaderListener {
        void onBitmapResult(Bitmap bitmap);
    }

    private BitmapLoaderListener btmListener;

    public BitmapLoader(BitmapLoaderListener btmListener) {
        this.btmListener = btmListener;
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        btmListener.onBitmapResult(resource);
    }

}
