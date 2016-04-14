package com.tadev.musicplayer.models.music;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iris Louis on 05/04/2016.
 */
public class CurrentSongPlay implements Parcelable {

    public String musicId;
    public Song song;
    public Lyric lyric;
    public int position;
    public int duration;
    public int progress;

    public boolean equals(String id) {
        if (id.equals(musicId)) {
            return true;
        }
        return false;
    }


    public CurrentSongPlay() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.musicId);
        dest.writeInt(this.duration);
        dest.writeInt(this.position);
        dest.writeInt(this.progress);
        dest.writeParcelable(this.song, flags);
        dest.writeParcelable(this.lyric, flags);


    }

    protected CurrentSongPlay(Parcel in) {
        this.musicId = in.readString();
        this.progress = in.readInt();
        this.duration = in.readInt();
        this.position= in.readInt();
        this.song = in.readParcelable(Song.class.getClassLoader());
        this.lyric = in.readParcelable(Lyric.class.getClassLoader());
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
