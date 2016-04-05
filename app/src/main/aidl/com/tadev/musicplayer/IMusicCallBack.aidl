// IMusicCallBack.aidl
package com.tadev.musicplayer;

// Declare any non-default types here with import statements

interface IMusicCallBack {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void duration(long duration);

        void position(int currentPosition);

        void currentID(int currentId);
}
