package com.tadev.musicplayer.models.music;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by Iris Louis on 02/04/2016.
 */
public class Download implements Parcelable {
    private int id;
    private String name;
    private HashMap<Integer, String> url = new HashMap<>();
    private String musicFilesize;
    private String music320Filesize;
    private String musicM4aFilesize;
    private String musicLosslessFilesize;
    private String urlChoose;

    public Download() {
    }

    public Download(int id, String name, HashMap<Integer, String> url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, String> getUrl() {
        return url;
    }

    public void setUrl(HashMap<Integer, String> url) {
        this.url = url;
    }

    public String getUrlChoose() {
        return urlChoose;
    }

    public void setUrlChoose(String urlChoose) {
        this.urlChoose = urlChoose;
    }

    public String getMusicFilesize() {
        return musicFilesize;
    }

    public void setMusicFilesize(String musicFilesize) {
        this.musicFilesize = musicFilesize;
    }

    public String getMusic320Filesize() {
        return music320Filesize;
    }

    public void setMusic320Filesize(String music320Filesize) {
        this.music320Filesize = music320Filesize;
    }

    public String getMusicM4aFilesize() {
        return musicM4aFilesize;
    }

    public void setMusicM4aFilesize(String musicM4aFilesize) {
        this.musicM4aFilesize = musicM4aFilesize;
    }

    public String getMusicLosslessFilesize() {
        return musicLosslessFilesize;
    }

    public void setMusicLosslessFilesize(String musicLosslessFilesize) {
        this.musicLosslessFilesize = musicLosslessFilesize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Download download = (Download) o;

        return id != download.id;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.musicFilesize);
        dest.writeString(this.music320Filesize);
        dest.writeString(this.musicM4aFilesize);
        dest.writeString(this.musicLosslessFilesize);
        dest.writeSerializable(this.url);
        dest.writeString(this.urlChoose);
    }

    protected Download(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.musicFilesize = in.readString();
        this.music320Filesize = in.readString();
        this.musicM4aFilesize = in.readString();
        this.musicLosslessFilesize = in.readString();
        this.url = (HashMap<Integer, String>) in.readSerializable();
        this.urlChoose = in.readString();
    }

    public static final Creator<Download> CREATOR = new Creator<Download>() {
        @Override
        public Download createFromParcel(Parcel source) {
            return new Download(source);
        }

        @Override
        public Download[] newArray(int size) {
            return new Download[size];
        }
    };

    @Override
    public String toString() {
        return "Download{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url=" + url +
                ", musicFilesize='" + musicFilesize + '\'' +
                ", music320Filesize='" + music320Filesize + '\'' +
                ", musicM4aFilesize='" + musicM4aFilesize + '\'' +
                ", musicLosslessFilesize='" + musicLosslessFilesize + '\'' +
                ", urlChoose='" + urlChoose + '\'' +
                '}';
    }
}
