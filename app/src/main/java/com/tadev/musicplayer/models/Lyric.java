package com.tadev.musicplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iris Louis on 02/04/2016.
 */
public class Lyric implements Parcelable {
    private String musicTitle;
    private String musicArtist;
    private String musicComposer;
    private String musicAlbum;
    private String musicProduction;
    private String musicYear;
    private String musicUsername;
    private String musicLyric;

    public Lyric() {
    }


    protected Lyric(Parcel in) {
//        String[] data = new String[]{musicTitle, musicArtist, musicComposer, musicAlbum, musicProduction,
//                musicYear, musicUsername, musicLyric};
        String[] data = new String[8];
        in.readStringArray(data);
        musicTitle = data[0];
        musicArtist = data[1];
        musicComposer = data[2];
        musicAlbum = data[3];
        musicProduction = data[4];
        musicYear = data[5];
        musicUsername = data[6];
        musicLyric = data[7];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(musicTitle);
        dest.writeString(musicArtist);
        dest.writeString(musicComposer);
        dest.writeString(musicAlbum);
        dest.writeString(musicProduction);
        dest.writeString(musicYear);
        dest.writeString(musicUsername);
        dest.writeString(musicLyric);
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


    public String getMusicTitle() {
        return musicTitle;
    }

    public Lyric setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
        return this;
    }

    public String getMusicArtist() {
        return musicArtist;
    }

    public Lyric setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
        return this;
    }

    public String getMusicComposer() {
        return musicComposer;
    }

    public Lyric setMusicComposer(String musicComposer) {
        this.musicComposer = musicComposer;
        return this;
    }

    public String getMusicAlbum() {
        return musicAlbum;
    }

    public Lyric setMusicAlbum(String musicAlbum) {
        this.musicAlbum = musicAlbum;
        return this;
    }

    public String getMusicProduction() {
        return musicProduction;
    }

    public Lyric setMusicProduction(String musicProduction) {
        this.musicProduction = musicProduction;
        return this;
    }

    public String getMusicYear() {
        return musicYear;
    }

    public Lyric setMusicYear(String musicYear) {
        this.musicYear = musicYear;
        return this;
    }

    public String getMusicUsername() {
        return musicUsername;
    }

    public Lyric setMusicUsername(String musicUsername) {
        this.musicUsername = musicUsername;
        return this;
    }

    public String getMusicLyric() {
        return musicLyric;
    }

    public Lyric setMusicLyric(String musicLyric) {
        this.musicLyric = musicLyric;
        return this;
    }

    @Override
    public String toString() {
        return "Lyric{ musicTitle : " + musicTitle + ", musicArtist : " + musicArtist + ", musicComposer : "
                + musicComposer + ", musicAlbum : " + musicAlbum + ", musicProduction : " + musicProduction
                + ", musicYear: " + musicYear + ", musicUsername : "
                + musicUsername + ", musicLyric : " + musicLyric + "}";

    }
}
