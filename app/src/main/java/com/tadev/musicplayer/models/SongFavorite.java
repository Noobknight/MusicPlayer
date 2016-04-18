package com.tadev.musicplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iris Louis on 17/04/2016.
 */
public class SongFavorite implements Parcelable {
    private String id;
    private String name;
    private String urlTitle;
    private String image;
    private String artist;

    public SongFavorite() {
    }

    public SongFavorite(String artist, String id, String image, String name, String urlTitle) {
        this.artist = artist;
        this.id = id;
        this.image = image;
        this.name = name;
        this.urlTitle = urlTitle;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.urlTitle);
        dest.writeString(this.image);
        dest.writeString(this.artist);
    }

    protected SongFavorite(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.urlTitle = in.readString();
        this.image = in.readString();
        this.artist = in.readString();
    }

    public static final Creator<SongFavorite> CREATOR = new Creator<SongFavorite>() {
        @Override
        public SongFavorite createFromParcel(Parcel source) {
            return new SongFavorite(source);
        }

        @Override
        public SongFavorite[] newArray(int size) {
            return new SongFavorite[size];
        }
    };

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlTitle() {
        return urlTitle;
    }

    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

    @Override
    public String toString() {
        return "SongFavorite{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", urlTitle='" + urlTitle + '\'' +
                ", image='" + image + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}
