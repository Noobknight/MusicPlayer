package com.tadev.musicplayer.common;

import com.tadev.musicplayer.constant.Constants;

/**
 * Created by Iris Louis on 29/03/2016.
 */
public class Api {
    //http://chiasenhac.com/api/listen.php?code=duc_wp_2014&return=json&m=1121965&url=nong~bigdaddy-hanh-sino
    //http://search.chiasenhac.com/api/search.php?code=duc_wp_2014&return=json&s=
    private static final String VIETNAM = "vietnam";
    private static final String USUK = "us-uk";
    private static final String KOREA = "korea";

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

}
