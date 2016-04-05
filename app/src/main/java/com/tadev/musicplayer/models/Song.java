package com.tadev.musicplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iris Louis on 02/04/2016.
 */
public class Song implements Parcelable {
    private String musicId;
    private String musicTitleUrl;
    private String musicTitle;
    private String musicArtist;
    private String musicImg;
    private String fileUrl;
    private String file320Url;
    private String fileM4aUrl;
    private String musicLength;

    public Song(){}


    public String getMusicId() {
        return musicId;
    }

    public Song setMusicId(String musicId) {
        this.musicId = musicId;
        return this;
    }

    public String getMusicTitleUrl() {
        return musicTitleUrl;
    }

    public Song setMusicTitleUrl(String musicTitleUrl) {
        this.musicTitleUrl = musicTitleUrl;
        return this;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public Song setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
        return this;
    }

    public String getMusicArtist() {
        return musicArtist;
    }

    public Song setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
        return this;
    }

    public String getMusicImg() {
        return musicImg;
    }

    public Song setMusicImg(String musicImg) {
        this.musicImg = musicImg;
        return this;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public Song setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public String getFile320Url() {
        return file320Url;
    }

    public Song setFile320Url(String file320Url) {
        this.file320Url = file320Url;
        return this;
    }

    public String getFileM4aUrl() {
        return fileM4aUrl;
    }

    public Song setFileM4aUrl(String fileM4aUrl) {
        this.fileM4aUrl = fileM4aUrl;
        return this;
    }

    public String getMusicLength() {
        return musicLength;
    }

    public Song setMusicLength(String musicLength) {
        this.musicLength = musicLength;
        return this;
    }

    @Override
    public String toString() {
        return "Song{" +
                "musicId='" + musicId + '\'' +
                ", musicTitleUrl='" + musicTitleUrl + '\'' +
                ", musicTitle='" + musicTitle + '\'' +
                ", musicArtist='" + musicArtist + '\'' +
                ", musicImg='" + musicImg + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", file320Url='" + file320Url + '\'' +
                ", fileM4aUrl='" + fileM4aUrl + '\'' +
                ", musicLength='" + musicLength + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.musicId);
        dest.writeString(this.musicTitleUrl);
        dest.writeString(this.musicTitle);
        dest.writeString(this.musicArtist);
        dest.writeString(this.musicImg);
        dest.writeString(this.fileUrl);
        dest.writeString(this.file320Url);
        dest.writeString(this.fileM4aUrl);
        dest.writeString(this.musicLength);
    }

    protected Song(Parcel in) {
        this.musicId = in.readString();
        this.musicTitleUrl = in.readString();
        this.musicTitle = in.readString();
        this.musicArtist = in.readString();
        this.musicImg = in.readString();
        this.fileUrl = in.readString();
        this.file320Url = in.readString();
        this.fileM4aUrl = in.readString();
        this.musicLength = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
