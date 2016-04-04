package com.tadev.musicplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.tadev.musicplayer.interfaces.ISongInfoListener;

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
    private ISongInfoListener listener;

    public Song(ISongInfoListener listener) {
        this.listener = listener;
    }

    public Song(){}

    protected Song(Parcel in) {
        String[] data = new String[9];
        in.readStringArray(data);
        this.musicId = data[0];
        this.musicTitleUrl = data[1];
        this.musicTitle = data[2];
        this.musicArtist = data[3];
        this.musicImg = data[4];
        this.fileUrl = data[5];
        this.file320Url = data[6];
        this.fileM4aUrl = data[7];
        this.musicLength = data[8];

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.readStringArray(new String[]{this.musicId, this.musicTitleUrl, this.musicTitle, this.musicArtist, this.musicImg
                , this.fileUrl, this.file320Url, this.fileM4aUrl, this.musicLength});
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

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
}
