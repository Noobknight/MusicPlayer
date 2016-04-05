package com.tadev.musicplayer.utils.design.support;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Iris Louis on 17/09/2015.
 */
public class ImageUtils {
    /**
     * @param url: Pass your url to download
     */
    public static byte[] downloadImage(String url) {

        byte[] imageRaw = null;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url_Conn = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) url_Conn
                        .openConnection();
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int c = 0;
                while ((c = in.read(buffer)) != -1) {
                    out.write(buffer, 0, c);
                }
                out.flush();

                imageRaw = out.toByteArray();

                urlConnection.disconnect();
                in.close();
                out.close();
            } catch (IOException e) {
                return null;
            }
        }

        return imageRaw;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static String convertImageToString(String URL) {
        // byte[] imgByte = downloadImage(URL);
        // String imgBase64 = Base64.encodeToString(imgByte, Base64.DEFAULT);
        Bitmap bmp = loadBitmap(URL);
        String imgBase64 = convertImageToString(bmp);
        return imgBase64;
    }

    /**
     * @param bmp : NonNull
     */
    public static String convertImageToString(Bitmap bmp) {
        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imgBytes = baos.toByteArray();
            String base64String = Base64.encodeToString(imgBytes,
                    Base64.DEFAULT);
            return base64String;
        } else {
            return null;
        }
    }

    public static Bitmap decodedString(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
                decodedString.length);
        return decodedByte;
    }


    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        int IO_BUFFER_SIZE = 4 * 1024;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URI uri = new URI(url);
                url = uri.toASCIIString();
                in = new BufferedInputStream(new URL(url).openStream(),
                        IO_BUFFER_SIZE);
                final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
                int bytesRead;
                byte[] buffer = new byte[IO_BUFFER_SIZE];
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
                final byte[] data = dataStream.toByteArray();
                BitmapFactory.Options options = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
                        options);
            } catch (IOException e) {
                return null;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
