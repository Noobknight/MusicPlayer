package com.tadev.musicplayer.metadata;

import com.tadev.musicplayer.constant.Constants;
import com.tadev.musicplayer.models.BaseModel;
import com.tadev.musicplayer.models.CurrentSongPlay;
import com.tadev.musicplayer.models.MusicKorea;
import com.tadev.musicplayer.models.MusicUSUK;
import com.tadev.musicplayer.models.MusicVietNam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iris Louis on 30/03/2016.
 */
public class MusicContainer {
    private static MusicContainer mInstance;
    public List<MusicVietNam> musicVietNamList;
    public List<MusicKorea> musicKoreaList;
    public List<MusicUSUK> musicUSUKList;
    public List<BaseModel> baseModelsMusic;
    private CurrentSongPlay mCurrentSongPlay;

    public static MusicContainer getInstance() {
        if (mInstance == null) {
            mInstance = new MusicContainer();
        }
        return mInstance;
    }

    public MusicContainer() {
        musicVietNamList = new ArrayList<>();
        musicKoreaList = new ArrayList<>();
        musicUSUKList = new ArrayList<>();
        mCurrentSongPlay = new CurrentSongPlay();
    }


    public List getListNeed(String type) throws IOException {
        if (baseModelsMusic == null) {
            return null;
        }
        switch (type) {
            case Constants.VIETNAM_TAG:
                for (BaseModel model :
                        baseModelsMusic) {
                    if (model instanceof MusicVietNam && !musicVietNamList.contains(model)) {
                        musicVietNamList.add((MusicVietNam) model);
                    }
                }
                return musicVietNamList;
            case Constants.KOREA_TAG:
                for (BaseModel model :
                        baseModelsMusic) {
                    if (model instanceof MusicKorea && !musicKoreaList.contains(model)) {
                        musicKoreaList.add((MusicKorea) model);
                    }
                }
                return musicKoreaList;
            case Constants.US_TAG:
                for (BaseModel model :
                        baseModelsMusic) {
                    if (model instanceof MusicUSUK  && !musicUSUKList.contains(model)) {
                        musicUSUKList.add((MusicUSUK) model);
                    }
                }
                return musicUSUKList;
            default:
                throw new IOException("Parameters must be not null");
        }
    }


    public List<MusicVietNam> getMusicVietNamList() {
        for (BaseModel model :
                baseModelsMusic) {
            if (model instanceof MusicVietNam) {
                musicVietNamList.add((MusicVietNam) model);
            }
        }
        return musicVietNamList;
    }

    public List<MusicKorea> getMusicKoreaList() {
        for (BaseModel model :
                baseModelsMusic) {
            if (model instanceof MusicKorea) {
                musicKoreaList.add((MusicKorea) model);
            }
        }
        return musicKoreaList;
    }

    public List<MusicUSUK> getMusicUSUKList() {
        for (BaseModel model :
                baseModelsMusic) {
            if (model instanceof MusicUSUK) {
                musicUSUKList.add((MusicUSUK) model);
            }
        }
        return musicUSUKList;
    }

    public void setMusicVietNamList(List<MusicVietNam> musicVietNamList) {
        this.musicVietNamList = musicVietNamList;
    }

    public void setMusicKoreaList(List<MusicKorea> musicKoreaList) {
        this.musicKoreaList = musicKoreaList;
    }

    public void setMusicUSUKList(List<MusicUSUK> musicUSUKList) {
        this.musicUSUKList = musicUSUKList;
    }

    public List<BaseModel> getBaseModelsMusic() {
        return baseModelsMusic;
    }

    public void setBaseModelsMusic(List<BaseModel> baseModelsMusic) {
        this.baseModelsMusic = baseModelsMusic;
    }

    public CurrentSongPlay getmCurrentSongPlay() {
        return mCurrentSongPlay;
    }

    public MusicContainer setmCurrentSongPlay(CurrentSongPlay mCurrentSongPlay) {
        this.mCurrentSongPlay = mCurrentSongPlay;
        return this;
    }
}
