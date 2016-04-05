package com.tadev.musicplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by Iris Louis on 24/03/2016.
 */
@Generated("org.jsonschema2pojo")
public class Music implements Parcelable {
    @SerializedName("music_id")
    @Expose
    protected String musicId;
    @SerializedName("music_title_url")
    @Expose
    protected String musicTitleUrl;
    @SerializedName("music_title")
    @Expose
    protected String musicTitle;
    @SerializedName("music_artist")
    @Expose
    protected String musicArtist;
    @SerializedName("music_composer")
    @Expose
    protected String musicComposer;
    @SerializedName("music_album")
    @Expose
    protected String musicAlbum;
    @SerializedName("music_production")
    @Expose
    protected String musicProduction;
    @SerializedName("music_year")
    @Expose
    protected String musicYear;
    @SerializedName("music_listen")
    @Expose
    protected String musicListen;
    @SerializedName("music_downloads")
    @Expose
    protected String musicDownloads;
    @SerializedName("music_time")
    @Expose
    protected String musicTime;
    @SerializedName("music_bitrate")
    @Expose
    protected String musicBitrate;
    @SerializedName("music_length")
    @Expose
    protected String musicLength;
    @SerializedName("music_32_filesize")
    @Expose
    protected String music32Filesize;
    @SerializedName("music_filesize")
    @Expose
    protected String musicFilesize;
    @SerializedName("music_320_filesize")
    @Expose
    protected String music320Filesize;
    @SerializedName("music_m4a_filesize")
    @Expose
    protected String musicM4aFilesize;
    @SerializedName("music_lossless_filesize")
    @Expose
    protected String musicLosslessFilesize;
    @SerializedName("music_username")
    @Expose
    protected String musicUsername;
    @SerializedName("music_lyric")
    @Expose
    protected String musicLyric;
    @SerializedName("music_img")
    @Expose
    protected String musicImg;
    @SerializedName("file_url")
    @Expose
    protected String fileUrl;
    @SerializedName("file_32_url")
    @Expose
    protected String file32Url;
    @SerializedName("file_320_url")
    @Expose
    protected String file320Url;
    @SerializedName("file_m4a_url")
    @Expose
    protected String fileM4aUrl;
    @SerializedName("file_lossless_url")
    @Expose
    protected String fileLosslessUrl;
    @SerializedName("full_url")
    @Expose
    protected String fullUrl;
    @SerializedName("music_genre")
    @Expose
    protected String musicGenre;

    public Music() {
    }


    public Music(String musicId, String musicTitleUrl, String musicTitle, String musicArtist,
                 String musicComposer, String musicAlbum, String musicProduction, String musicYear,
                 String musicListen, String musicDownloads, String musicTime, String musicBitrate,
                 String musicLength, String music32Filesize, String musicFilesize,
                 String music320Filesize, String musicM4aFilesize, String musicLosslessFilesize,
                 String musicUsername, String musicLyric, String musicImg, String fileUrl,
                 String file32Url, String file320Url, String fileM4aUrl, String fileLosslessUrl,
                 String fullUrl, String musicGenre) {
        this.musicId = musicId;
        this.musicTitleUrl = musicTitleUrl;
        this.musicTitle = musicTitle;
        this.musicArtist = musicArtist;
        this.musicComposer = musicComposer;
        this.musicAlbum = musicAlbum;
        this.musicProduction = musicProduction;
        this.musicYear = musicYear;
        this.musicListen = musicListen;
        this.musicDownloads = musicDownloads;
        this.musicTime = musicTime;
        this.musicBitrate = musicBitrate;
        this.musicLength = musicLength;
        this.music32Filesize = music32Filesize;
        this.musicFilesize = musicFilesize;
        this.music320Filesize = music320Filesize;
        this.musicM4aFilesize = musicM4aFilesize;
        this.musicLosslessFilesize = musicLosslessFilesize;
        this.musicUsername = musicUsername;
        this.musicLyric = musicLyric;
        this.musicImg = musicImg;
        this.fileUrl = fileUrl;
        this.file32Url = file32Url;
        this.file320Url = file320Url;
        this.fileM4aUrl = fileM4aUrl;
        this.fileLosslessUrl = fileLosslessUrl;
        this.fullUrl = fullUrl;
        this.musicGenre = musicGenre;
    }


    /**
     * @return The musicId
     */
    public String getMusicId() {
        return musicId;
    }

    /**
     * @param musicId The music_id
     */
    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }


    /**
     * @return The musicTitleUrl
     */
    public String getMusicTitleUrl() {
        return musicTitleUrl;
    }

    /**
     * @param musicTitleUrl The music_title_url
     */
    public void setMusicTitleUrl(String musicTitleUrl) {
        this.musicTitleUrl = musicTitleUrl;
    }

    /**
     * @return The musicTitle
     */
    public String getMusicTitle() {
        return musicTitle;
    }

    /**
     * @param musicTitle The music_title
     */
    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    /**
     * @return The musicArtist
     */
    public String getMusicArtist() {
        return musicArtist.replace(";", " ft");
    }

    /**
     * @param musicArtist The music_artist
     */
    public void setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
    }

    /**
     * @return The musicComposer
     */
    public String getMusicComposer() {
        return musicComposer;
    }

    /**
     * @param musicComposer The music_composer
     */
    public void setMusicComposer(String musicComposer) {
        this.musicComposer = musicComposer;
    }

    /**
     * @return The musicAlbum
     */
    public String getMusicAlbum() {
        return musicAlbum;
    }

    /**
     * @param musicAlbum The music_album
     */
    public void setMusicAlbum(String musicAlbum) {
        this.musicAlbum = musicAlbum;
    }

    /**
     * @return The musicProduction
     */
    public String getMusicProduction() {
        return musicProduction;
    }

    /**
     * @param musicProduction The music_production
     */
    public void setMusicProduction(String musicProduction) {
        this.musicProduction = musicProduction;
    }


    /**
     * @return The musicYear
     */
    public String getMusicYear() {
        return musicYear;
    }

    /**
     * @param musicYear The music_year
     */
    public void setMusicYear(String musicYear) {
        this.musicYear = musicYear;
    }

    /**
     * @return The musicListen
     */
    public String getMusicListen() {
        return musicListen;
    }

    /**
     * @param musicListen The music_listen
     */
    public void setMusicListen(String musicListen) {
        this.musicListen = musicListen;
    }

    /**
     * @return The musicDownloads
     */
    public String getMusicDownloads() {
        return musicDownloads;
    }

    /**
     * @param musicDownloads The music_downloads
     */
    public void setMusicDownloads(String musicDownloads) {
        this.musicDownloads = musicDownloads;
    }

    /**
     * @return The musicTime
     */
    public String getMusicTime() {
        return musicTime;
    }

    /**
     * @param musicTime The music_time
     */
    public void setMusicTime(String musicTime) {
        this.musicTime = musicTime;
    }

    /**
     * @return The musicBitrate
     */
    public String getMusicBitrate() {
        return musicBitrate;
    }

    /**
     * @param musicBitrate The music_bitrate
     */
    public void setMusicBitrate(String musicBitrate) {
        this.musicBitrate = musicBitrate;
    }

    /**
     * @return The musicLength
     */
    public String getMusicLength() {
        return musicLength;
    }

    /**
     * @param musicLength The music_length
     */
    public void setMusicLength(String musicLength) {
        this.musicLength = musicLength;
    }

    /**
     * @return The music32Filesize
     */
    public String getMusic32Filesize() {
        return music32Filesize;
    }

    /**
     * @param music32Filesize The music_32_filesize
     */
    public void setMusic32Filesize(String music32Filesize) {
        this.music32Filesize = music32Filesize;
    }

    /**
     * @return The musicFilesize
     */
    public String getMusicFilesize() {
        return musicFilesize;
    }

    /**
     * @param musicFilesize The music_filesize
     */
    public void setMusicFilesize(String musicFilesize) {
        this.musicFilesize = musicFilesize;
    }

    /**
     * @return The music320Filesize
     */
    public String getMusic320Filesize() {
        return music320Filesize;
    }

    /**
     * @param music320Filesize The music_320_filesize
     */
    public void setMusic320Filesize(String music320Filesize) {
        this.music320Filesize = music320Filesize;
    }

    /**
     * @return The musicM4aFilesize
     */
    public String getMusicM4aFilesize() {
        return musicM4aFilesize;
    }

    /**
     * @param musicM4aFilesize The music_m4a_filesize
     */
    public void setMusicM4aFilesize(String musicM4aFilesize) {
        this.musicM4aFilesize = musicM4aFilesize;
    }

    /**
     * @return The musicLosslessFilesize
     */
    public String getMusicLosslessFilesize() {
        return musicLosslessFilesize;
    }

    /**
     * @param musicLosslessFilesize The music_lossless_filesize
     */
    public void setMusicLosslessFilesize(String musicLosslessFilesize) {
        this.musicLosslessFilesize = musicLosslessFilesize;
    }


    /**
     * @return The musicUsername
     */
    public String getMusicUsername() {
        return musicUsername;
    }

    /**
     * @param musicUsername The music_username
     */
    public void setMusicUsername(String musicUsername) {
        this.musicUsername = musicUsername;
    }

    /**
     * @return The musicLyric
     */
    public String getMusicLyric() {
        return musicLyric;
    }

    /**
     * @param musicLyric The music_lyric
     */
    public void setMusicLyric(String musicLyric) {
        this.musicLyric = musicLyric;
    }


    /**
     * @return The musicImg
     */
    public String getMusicImg() {
        return musicImg;
    }

    /**
     * @param musicImg The music_img
     */
    public void setMusicImg(String musicImg) {
        this.musicImg = musicImg;
    }

    /**
     * @return The fileUrl
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * @param fileUrl The file_url
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * @return The file32Url
     */
    public String getFile32Url() {
        return file32Url;
    }

    /**
     * @param file32Url The file_32_url
     */
    public void setFile32Url(String file32Url) {
        this.file32Url = file32Url;
    }

    /**
     * @return The file320Url
     */
    public String getFile320Url() {
        return file320Url;
    }

    /**
     * @param file320Url The file_320_url
     */
    public void setFile320Url(String file320Url) {
        this.file320Url = file320Url;
    }

    /**
     * @return The fileM4aUrl
     */
    public String getFileM4aUrl() {
        return fileM4aUrl;
    }

    /**
     * @param fileM4aUrl The file_m4a_url
     */
    public void setFileM4aUrl(String fileM4aUrl) {
        this.fileM4aUrl = fileM4aUrl;
    }

    /**
     * @return The fileLosslessUrl
     */
    public String getFileLosslessUrl() {
        return fileLosslessUrl;
    }

    /**
     * @param fileLosslessUrl The file_lossless_url
     */
    public void setFileLosslessUrl(String fileLosslessUrl) {
        this.fileLosslessUrl = fileLosslessUrl;
    }

    /**
     * @return The fullUrl
     */
    public String getFullUrl() {
        return fullUrl;
    }

    /**
     * @param fullUrl The full_url
     */
    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    /**
     * @return The musicGenre
     */
    public String getMusicGenre() {
        return musicGenre;
    }

    /**
     * @param musicGenre The music_genre
     */
    public void setMusicGenre(String musicGenre) {
        this.musicGenre = musicGenre;
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
        dest.writeString(this.musicComposer);
        dest.writeString(this.musicAlbum);
        dest.writeString(this.musicProduction);
        dest.writeString(this.musicYear);
        dest.writeString(this.musicListen);
        dest.writeString(this.musicDownloads);
        dest.writeString(this.musicTime);
        dest.writeString(this.musicBitrate);
        dest.writeString(this.musicLength);
        dest.writeString(this.music32Filesize);
        dest.writeString(this.musicFilesize);
        dest.writeString(this.music320Filesize);
        dest.writeString(this.musicM4aFilesize);
        dest.writeString(this.musicLosslessFilesize);
        dest.writeString(this.musicUsername);
        dest.writeString(this.musicLyric);
        dest.writeString(this.musicImg);
        dest.writeString(this.fileUrl);
        dest.writeString(this.file32Url);
        dest.writeString(this.file320Url);
        dest.writeString(this.fileM4aUrl);
        dest.writeString(this.fileLosslessUrl);
        dest.writeString(this.fullUrl);
        dest.writeString(this.musicGenre);
    }

    protected Music(Parcel in) {
        this.musicId = in.readString();
        this.musicTitleUrl = in.readString();
        this.musicTitle = in.readString();
        this.musicArtist = in.readString();
        this.musicComposer = in.readString();
        this.musicAlbum = in.readString();
        this.musicProduction = in.readString();
        this.musicYear = in.readString();
        this.musicListen = in.readString();
        this.musicDownloads = in.readString();
        this.musicTime = in.readString();
        this.musicBitrate = in.readString();
        this.musicLength = in.readString();
        this.music32Filesize = in.readString();
        this.musicFilesize = in.readString();
        this.music320Filesize = in.readString();
        this.musicM4aFilesize = in.readString();
        this.musicLosslessFilesize = in.readString();
        this.musicUsername = in.readString();
        this.musicLyric = in.readString();
        this.musicImg = in.readString();
        this.fileUrl = in.readString();
        this.file32Url = in.readString();
        this.file320Url = in.readString();
        this.fileM4aUrl = in.readString();
        this.fileLosslessUrl = in.readString();
        this.fullUrl = in.readString();
        this.musicGenre = in.readString();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    @Override
    public String toString() {
        return "Music{" +
                "musicId='" + musicId + '\'' +
                ", musicTitleUrl='" + musicTitleUrl + '\'' +
                ", musicTitle='" + musicTitle + '\'' +
                ", musicArtist='" + musicArtist + '\'' +
                ", musicComposer='" + musicComposer + '\'' +
                ", musicAlbum='" + musicAlbum + '\'' +
                ", musicProduction='" + musicProduction + '\'' +
                ", musicYear='" + musicYear + '\'' +
                ", musicListen='" + musicListen + '\'' +
                ", musicDownloads='" + musicDownloads + '\'' +
                ", musicTime='" + musicTime + '\'' +
                ", musicBitrate='" + musicBitrate + '\'' +
                ", musicLength='" + musicLength + '\'' +
                ", music32Filesize='" + music32Filesize + '\'' +
                ", musicFilesize='" + musicFilesize + '\'' +
                ", music320Filesize='" + music320Filesize + '\'' +
                ", musicM4aFilesize='" + musicM4aFilesize + '\'' +
                ", musicLosslessFilesize='" + musicLosslessFilesize + '\'' +
                ", musicUsername='" + musicUsername + '\'' +
                ", musicImg='" + musicImg + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", file32Url='" + file32Url + '\'' +
                ", file320Url='" + file320Url + '\'' +
                ", fileM4aUrl='" + fileM4aUrl + '\'' +
                ", fileLosslessUrl='" + fileLosslessUrl + '\'' +
                ", fullUrl='" + fullUrl + '\'' +
                ", musicGenre='" + musicGenre + '\'' +
                '}';
    }


}
