package com.tadev.musicplayer.utils.support;

import com.tadev.musicplayer.constant.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Iris Louis on 07/03/2016.
 */
public class JsonUtils {

    public static JSONObject getJsonResponse(String link) throws IOException, JSONException {
        StringBuilder sBuilder = new StringBuilder();
        String result = "";
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                "UTF-8"), 8);
        String line = null;
        while ((line = reader.readLine()) != null) {
            sBuilder.append(line + "\n");
        }
        result = sBuilder.toString();
        return new JSONObject(result);
    }


}
