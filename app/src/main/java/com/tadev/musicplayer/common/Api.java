package com.tadev.musicplayer.common;

import com.tadev.musicplayer.constant.Constants;

/**
 * Created by Iris Louis on 29/03/2016.
 */
public class Api {
    //http://chiasenhac.com/api/listen.php?code=duc_wp_2014&return=json&m=1121965&url=nong~bigdaddy-hanh-sino
    //http://search.chiasenhac.com/api/search.php?code=duc_wp_2014&return=json&s=
    //http://chiasenhac.com/api/category.php?return=json&c=v-video
    private static final String VIETNAM = "vietnam";
    private static final String USUK = "us-uk";
    private static final String KOREA = "korea";

    private static final String VIDEO_VIETNAM = "v-video";
    private static final String VIDEO_USUK = "u-video";
    private static final String VIDEO_KOREA = "k-video";

    public static final int MUSIC_128 = 0;
    public static final int MUSIC_320 = 1;
    public static final int MUSIC_500 = 2;
    public static final int MUSIC_1000 = 3;

    public static final String KEY_CODE = "duc_wp_2014";

    public static String getApiMusicVietNam() {
        return Constants.BASE_URL_CATE_API + VIETNAM;
    }

    public static String getApiMusicUSUK() {
        return Constants.BASE_URL_CATE_API + USUK;
    }

    public static String getApiMusicKorea() {
        return Constants.BASE_URL_CATE_API + KOREA;
    }

    public static String getURLFindInfo(String idSong, String urlTitle) {
        return Constants.BASE_URL_GET_INFO + "?code=" + KEY_CODE + "&return=json&m="
                + idSong + "&url=" + urlTitle;
    }

    public static String getURLSearch(String keyword) {
        return Constants.BASE_URL_SEARCH + "?code=" + KEY_CODE + "&return=json&s=" + keyword;
    }

    public static String getApiVideoVietNam() {
        return Constants.BASE_URL_CATE_API + VIDEO_VIETNAM;
    }

    public static String getApiVideoKorea() {
        return Constants.BASE_URL_CATE_API + VIDEO_KOREA;
    }

    public static String getApiVideoUSUK() {
        return Constants.BASE_URL_CATE_API + VIDEO_USUK;
    }

}
