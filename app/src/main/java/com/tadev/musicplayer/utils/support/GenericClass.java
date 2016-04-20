package com.tadev.musicplayer.utils.support;

/**
 * Created by Iris Louis on 31/03/2016.
 */
public class GenericClass<T> {
    private final Class<T> type;

    public GenericClass(Class<T> type) {
        this.type = type;
    }

    public Class<T> getMyType() {
        return this.type;
    }
}
