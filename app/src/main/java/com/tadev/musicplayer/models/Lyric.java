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
    private String musicImage;

    public Lyric() {
    }


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

    public String getMusicImage() {
        return musicImage;
    }

    public Lyric setMusicImage(String musicImage) {
        this.musicImage = musicImage;
        return this;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.musicTitle);
        dest.writeString(this.musicArtist);
        dest.writeString(this.musicComposer);
        dest.writeString(this.musicAlbum);
        dest.writeString(this.musicProduction);
        dest.writeString(this.musicYear);
        dest.writeString(this.musicUsername);
        dest.writeString(this.musicLyric);
        dest.writeString(this.musicImage);
    }

    protected Lyric(Parcel in) {
        this.musicTitle = in.readString();
        this.musicArtist = in.readString();
        this.musicComposer = in.readString();
        this.musicAlbum = in.readString();
        this.musicProduction = in.readString();
        this.musicYear = in.readString();
        this.musicUsername = in.readString();
        this.musicLyric = in.readString();
        this.musicImage = in.readString();
    }

    public static final Creator<Lyric> CREATOR = new Creator<Lyric>() {
        @Override
        public Lyric createFromParcel(Parcel source) {
            return new Lyric(source);
        }

        @Override
        public Lyric[] newArray(int size) {
            return new Lyric[size];
        }
    };
}
