package com.tadev.musicplayer.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iris Louis on 05/04/2016.
 */
public class CurrentSongPlay implements Parcelable {

    public String musicId;
    public Bitmap btmImage;
    public String title;
    public String artist;
    public String fileUrl;

    public boolean equals(String id) {
        if (id.equals(musicId)) {
            return true;
        }
        return false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.musicId);
        dest.writeParcelable(this.btmImage, flags);
        dest.writeString(this.title);
        dest.writeString(this.artist);
        dest.writeString(this.fileUrl);
    }

    public CurrentSongPlay() {
    }

    protected CurrentSongPlay(Parcel in) {
        this.musicId = in.readString();
        this.btmImage = in.readParcelable(Bitmap.class.getClassLoader());
        this.title = in.readString();
        this.artist = in.readString();
        this.fileUrl = in.readString();
    }

    public static final Creator<CurrentSongPlay> CREATOR = new Creator<CurrentSongPlay>() {
        @Override
        public CurrentSongPlay createFromParcel(Parcel source) {
            return new CurrentSongPlay(source);
        }

        @Override
        public CurrentSongPlay[] newArray(int size) {
            return new CurrentSongPlay[size];
        }
    };
}
