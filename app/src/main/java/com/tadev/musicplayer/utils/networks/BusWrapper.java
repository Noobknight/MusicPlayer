package com.tadev.musicplayer.utils.networks;

/**
 * Created by Iris Louis on 20/04/2016.
 */
public interface BusWrapper {
    void register(Object object);

    void unregister(Object object);

    void post(Object event);
}
