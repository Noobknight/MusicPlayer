package com.tadev.musicplayer.models.video;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tadev.musicplayer.constant.Enums;

import javax.annotation.Generated;

/**
 * Created by Iris Louis on 13/04/2016.
 */
@Generated("org.jsonschema2pojo")
public class Video implements Parcelable {
    @SerializedName("music_id")
    @Expose
    private String musicId;
    @SerializedName("music_title")
    @Expose
    private String musicTitle;
    @SerializedName("music_artist")
    @Expose
    private String musicArtist;
    @SerializedName("music_title_url")
    @Expose
    private String musicTitleUrl;
    @SerializedName("music_downloads")
    @Expose
    private String musicDownloads;
    @SerializedName("music_bitrate")
    @Expose
    private String musicBitrate;
    @SerializedName("music_width")
    @Expose
    private String musicWidth;
    @SerializedName("music_height")
    @Expose
    private String musicHeight;
    @SerializedName("music_length")
    @Expose
    private String musicLength;
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailUrl;


    public Video() {
    }

    public Video(String musicId, String musicTitle, String musicArtist, String musicTitleUrl,
                 String musicDownloads, String musicBitrate,
                 String musicWidth, String musicHeight, String musicLength, String thumbnailUrl) {
        this.musicId = musicId;
        this.musicTitle = musicTitle;
        this.musicArtist = musicArtist;
        this.musicTitleUrl = musicTitleUrl;
        this.musicDownloads = musicDownloads;
        this.musicBitrate = musicBitrate;
        this.musicWidth = musicWidth;
        this.musicHeight = musicHeight;
        this.musicLength = musicLength;
        this.thumbnailUrl = thumbnailUrl;
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
     * @return The musicWidth
     */
    public String getMusicWidth() {
        return musicWidth;
    }

    /**
     * @param musicWidth The music_width
     */
    public void setMusicWidth(String musicWidth) {
        this.musicWidth = musicWidth;
    }

    /**
     * @return The musicHeight
     */
    public String getMusicHeight() {
        return validateBitrate();
    }

    private String validateBitrate() {
        if (musicWidth.equals("720") && musicHeight.equals("480")) {
            return Enums.VideoBitrate.MV480.toString();
        } else if (musicWidth.equals("1280") && musicHeight.equals("720")) {
            return Enums.VideoBitrate.HD720.toString();
        } else if (musicWidth.equals("1920") && musicHeight.equals("1080")) {
            return Enums.VideoBitrate.HD1080.toString();
        } else {
            return musicHeight + "x" + musicWidth;
        }
    }

    /**
     * @param musicHeight The music_height
     */
    public void setMusicHeight(String musicHeight) {
        this.musicHeight = musicHeight;
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
     * @return The thumbnailUrl
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getThumbStandard() {
        String temp = thumbnailUrl;
        if (!TextUtils.isEmpty(temp) || temp != null) {
            String sLink = temp.substring(0, temp.lastIndexOf(".")).concat("_prv");
            String fileType = temp.substring(temp.lastIndexOf("."));
            return sLink + fileType;
        }
        return "";
    }

    /**
     * @param thumbnailUrl The thumbnail_url
     */
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.musicId);
        dest.writeString(this.musicTitle);
        dest.writeString(this.musicArtist);
        dest.writeString(this.musicTitleUrl);
        dest.writeString(this.musicDownloads);
        dest.writeString(this.musicBitrate);
        dest.writeString(this.musicWidth);
        dest.writeString(this.musicHeight);
        dest.writeString(this.musicLength);
        dest.writeString(this.thumbnailUrl);
    }


    protected Video(Parcel in) {
        this.musicId = in.readString();
        this.musicTitle = in.readString();
        this.musicArtist = in.readString();
        this.musicTitleUrl = in.readString();
        this.musicDownloads = in.readString();
        this.musicBitrate = in.readString();
        this.musicWidth = in.readString();
        this.musicHeight = in.readString();
        this.musicLength = in.readString();
        this.thumbnailUrl = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public String toString() {
        return "Video{" +
                "musicArtist='" + musicArtist + '\'' +
                ", musicId='" + musicId + '\'' +
                ", musicTitle='" + musicTitle + '\'' +
                ", musicTitleUrl='" + musicTitleUrl + '\'' +
                ", musicDownloads='" + musicDownloads + '\'' +
                ", musicBitrate='" + musicBitrate + '\'' +
                ", musicWidth='" + musicWidth + '\'' +
                ", musicHeight='" + musicHeight + '\'' +
                ", musicLength='" + musicLength + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }
}
