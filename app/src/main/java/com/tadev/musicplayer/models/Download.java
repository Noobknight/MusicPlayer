package com.tadev.musicplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Iris Louis on 02/04/2016.
 */
public class Download extends Music implements Parcelable {

    public Download() {
    }

    protected Download(Parcel in) {
//        String[] data = new String[]{musicTitle, musicArtist, musicDownloads, music32Filesize, musicFilesize,
//                music320Filesize, musicM4aFilesize, musicLosslessFilesize, file32Url, fileUrl, file320Url,
//                fileM4aUrl, fileLosslessUrl};
//        in.readStringArray(data);
//        musicTitle = data[0];
//        musicArtist = data[1];
//        musicDownloads = data[2];
//        music32Filesize = data[3];
//        musicFilesize = data[4];
//        music320Filesize = data[5];
//        musicM4aFilesize = data[6];
//        musicLosslessFilesize = data[7];
//        file32Url = data[8];
//        fileUrl = data[9];
//        file320Url = data[10];
//        fileM4aUrl = data[11];
//        fileLosslessUrl = data[12];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeStringArray(new String[]{musicTitle, musicArtist, musicDownloads, music32Filesize,
//                musicFilesize, music320Filesize, musicM4aFilesize, musicLosslessFilesize, file32Url, fileUrl, file320Url,
//                fileM4aUrl, fileLosslessUrl});
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Download> CREATOR = new Creator<Download>() {
        @Override
        public Download createFromParcel(Parcel in) {
            return new Download(in);
        }

        @Override
        public Download[] newArray(int size) {
            return new Download[size];
        }
    };
}
