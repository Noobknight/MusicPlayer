package com.tadev.musicplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.tadev.musicplayer.constant.Constants;

/**
 * Created by Iris Louis on 30/03/2016.
 */
public class BaseModel implements Parcelable {
    protected String music_id;

    protected String music_title;

    protected String music_artist;

    protected String music_title_url;

    protected String artist_face_url;

    protected String music_length;

    protected String music_bitrate;

    protected String music_downloads;

    protected String music_listen;


    public String getMusic_id() {
        return music_id;
    }

    public void setMusic_id(String music_id) {
        this.music_id = music_id;
    }

    public String getMusic_title() {
        return music_title;
    }

    public void setMusic_title(String music_title) {
        this.music_title = music_title;
    }

    public String getMusic_artist() {
        return music_artist;
    }

    public void setMusic_artist(String music_artist) {
        this.music_artist = music_artist;
    }

    public String getMusic_title_url() {
        return music_title_url;
    }

    public void setMusic_title_url(String music_title_url) {
        this.music_title_url = music_title_url;
    }

    public String getArtist_face_url() {
        return artist_face_url;
    }

    public void setArtist_face_url(String artist_face_url) {
        this.artist_face_url = artist_face_url;
    }

    public String getMusic_length() {
        return music_length;
    }

    public void setMusic_length(String music_length) {
        this.music_length = music_length;
    }

    public String getMusic_bitrate() {
        if (music_bitrate.equals("1000")) {
            return "Lossless";
        }
        return music_bitrate;
    }

    public void setMusic_bitrate(String music_bitrate) {
        this.music_bitrate = music_bitrate;
    }

    public String getMusic_downloads() {
        return music_downloads;
    }

    public void setMusic_downloads(String music_downloads) {
        this.music_downloads = music_downloads;
    }

    public String getMusic_listen() {
        return music_listen;
    }

    public void setMusic_listen(String music_listen) {
        this.music_listen = music_listen;
    }


    @Override
    public String toString() {
        return "BaseModel{" +
                "music_id='" + music_id + '\'' +
                ", music_title='" + music_title + '\'' +
                ", music_artist='" + music_artist + '\'' +
                ", music_title_url='" + music_title_url + '\'' +
                ", artist_face_url='" + artist_face_url + '\'' +
                ", music_length='" + music_length + '\'' +
                ", music_bitrate='" + music_bitrate + '\'' +
                ", music_downloads='" + music_downloads + '\'' +
                ", music_listen='" + music_listen + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.music_id);
        dest.writeString(this.music_title);
        dest.writeString(this.music_artist);
        dest.writeString(this.music_title_url);
        dest.writeString(this.artist_face_url);
        dest.writeString(this.music_length);
        dest.writeString(this.music_bitrate);
        dest.writeString(this.music_downloads);
        dest.writeString(this.music_listen);
    }

    public BaseModel() {
    }

    protected BaseModel(Parcel in) {
        this.music_id = in.readString();
        this.music_title = in.readString();
        this.music_artist = in.readString();
        this.music_title_url = in.readString();
        this.artist_face_url = in.readString();
        this.music_length = in.readString();
        this.music_bitrate = in.readString();
        this.music_downloads = in.readString();
        this.music_listen = in.readString();
    }

    public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>() {
        @Override
        public BaseModel createFromParcel(Parcel source) {
            return new BaseModel(source);
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };

    private String convertLink(String cat_url, String url_title, long id) {
        return Constants.BASE_URL_MUSIC + cat_url + "/" + url_title + "~" + id + ".html";
    }
}
