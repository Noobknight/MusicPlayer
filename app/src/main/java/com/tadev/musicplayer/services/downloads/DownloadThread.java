package com.tadev.musicplayer.services.downloads;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.tadev.musicplayer.constant.Extras;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Iris Louis on 11/04/2016.
 */
public class DownloadThread implements Runnable {
    private String[] ReservedChars = {"|", "\\", "?", "*", "<", "\"", ":", ">"};
    private final String TAG = "DownloadThread";
    private String url;
    private Handler handler;
    private int fileLength = 0;
    private String fileName;

    public DownloadThread(String url, String fileName, Handler handler) {
        this.url = url;
        this.handler = handler;
        this.fileName = fileName;
    }


    @Override
    public void run() {
        downloadImage(url, fileName);
    }

    private void downloadImage(String param, String fileName) {
        InputStream is = null;
        FileOutputStream out = null;
        final int IO_BUFFER_SIZE = 1024;
        long total = 0;
        int publishProgress = 0;
        try {
            URL url = new URL(param);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String path = Environment.getExternalStorageDirectory() + "/MusicDownload";
                File createFolder = new File(path);
                if (!createFolder.exists()) {
                    createFolder.mkdir();
                }
                path = Environment
                        .getExternalStorageDirectory()
                        + "/MusicDownload/" + fileName;
                out = new FileOutputStream(path);
                is = conn.getInputStream();
                fileLength = conn.getContentLength();
                int bytesRead;
                byte[] buffer = new byte[IO_BUFFER_SIZE];
                if (fileLength > 0) {
                    while ((bytesRead = is.read(buffer)) != -1) {
                        total += bytesRead;
                        publishProgress = (int) (total * 100L / fileLength);
                        sendMessage(Extras.MSG_UPDATING, publishProgress);
                        out.write(buffer, 0, bytesRead);
                    }
                }
                out.flush();
                if (total == fileLength) {
                    sendMessage(Extras.MSG_SUCCESSED, 100);
                } else {
                    sendMessage(Extras.MSG_FAILED, publishProgress);
                }
            }
        } catch (IOException e) {
            sendMessage(Extras.MSG_FAILED, publishProgress);
        } finally {
            if (is != null) {
                try {
                    out.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendMessage(int what, int percentage) {
        Message message = new Message();
        message.what = what;
        message.arg1 = percentage;
        handler.sendMessage(message);
    }


    private String validateFileName(String NameBlog) {
        return StringUtils.replaceEach(NameBlog, ReservedChars,
                new String[]{" ", " ", " ", " ", " ", " ", " ", " "})
                .concat(".mp3");
    }

}