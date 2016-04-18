package com.tadev.musicplayer.models;

import android.graphics.Bitmap;

import com.tadev.musicplayer.constant.MusicTypeEnum;

/**
 * Created by Iris Louis on 17/04/2016.
 */
public class MusicOffline{

    private MusicTypeEnum type;
    private long id;
    private String title;
    private String artist;
    private String album;
    private long duration;
    private String uri;
    private String coverUri;
    private String fileName;
    private Bitmap cover;

    public MusicTypeEnum getType() {
        return type;
    }

    public void setType(MusicTypeEnum type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist.replace(";"," ft");
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MusicOffline)) {
            return false;
        }
        return this.getId() == ((MusicOffline) o).getId();
    }

    @Override
    public String toString() {
        return "MusicOffline{" +
                "type=" + type +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", duration=" + duration +
                ", uri='" + uri + '\'' +
                ", coverUri='" + coverUri + '\'' +
                ", fileName='" + fileName + '\'' +
                ", cover=" + cover +
                '}';
    }
}
