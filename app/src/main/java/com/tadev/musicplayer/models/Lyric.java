package com.tadev.musicplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iris Louis on 02/04/2016.
 */
public class Lyric extends Music implements Parcelable {


    public Lyric() {
    }


    protected Lyric(Parcel in) {
//        String[] data = new String[]{musicTitle, musicArtist, musicComposer, musicAlbum, musicProduction,
//                musicYear, musicUsername, musicLyric};
        String[] data = new String[8];
        in.readStringArray(data);
//        musicTitle = data[0];
//        musicArtist = data[1];
//        musicComposer = data[2];
//        musicAlbum = data[3];
//        musicProduction = data[4];
//        musicYear = data[5];
//        musicUsername = data[6];
//        musicLyric = data[7];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(musicTitle);
//        dest.writeString(musicArtist);
//        dest.writeString(musicComposer);
//        dest.writeString(musicAlbum);
//        dest.writeString(musicProduction);
//        dest.writeString(musicYear);
//        dest.writeString(musicUsername);
//        dest.writeString(musicLyric);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Lyric> CREATOR = new Creator<Lyric>() {
        @Override
        public Lyric createFromParcel(Parcel in) {
            return new Lyric(in);
        }

        @Override
        public Lyric[] newArray(int size) {
            return new Lyric[size];
        }
    };

//    @Override
//    public String toString() {
//        return "Lyric{ musicTitle : " + musicTitle + ", musicArtist : " + musicArtist + ", musicComposer : "
//                + musicComposer + ", musicAlbum : " + musicAlbum + ", musicProduction : " + musicProduction
//                + ", musicYear: " + musicYear + ", musicUsername : "
//                + musicUsername + ", musicLyric : " + musicLyric + "}";
//
//    }
}
