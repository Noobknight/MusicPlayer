package com.tadev.musicplayer.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.constant.Extras;
import com.tadev.musicplayer.models.music.Download;
import com.tadev.musicplayer.services.downloads.DownloadThread;
import com.tadev.musicplayer.utils.support.Utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Iris Louis on 11/04/2016.
 */
public class DownloaderService extends Service {
    private static String TAG = "DownloaderService";
    public static final String KEY_EXTRA = "extras";
    private static final int NOTIFICATION_ID = 0x100;
    private static Map<Integer, NotificationCompat.Builder> notifications;
    private static NotificationManager manager;
    private static Context mContext;
    private long mLastUpdateTime = -1;
    private Download objDownloading;
    private ResultResponse mHandler;
    private static final int UPDATE_INTERVAL = 2000;
    private int oldId = 0;

    public DownloaderService() {
    }

    @Override
    public void onCreate() {
        objDownloading = new Download();
        mContext = DownloaderService.this;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifications = new HashMap<>();
        mHandler = new ResultResponse(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
//            Download download = intent.getExtras().getParcelable(KEY_EXTRA);
            int id = intent.getIntExtra("id", 0);
            String url = intent.getStringExtra("url");
            String name = intent.getStringExtra("name");
            if (enableDownload(id)) {
                oldId = id;
                addNotification(NOTIFICATION_ID, name);
                DownloadThread mDownloader = new DownloadThread(url,
                        validateFileName(name, url).trim(), mHandler);
                new Thread(mDownloader).start();
            } else {
                Toast.makeText(DownloaderService.this, "Plz... wait for download completed !!!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        cancelNotification(mContext, hashCode());
        stopSelf();
        super.onDestroy();
    }


    private boolean enableDownload(Download download) {
        return !objDownloading.equals(download);
    }

    private void addNotification(int id, String title) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        mLastUpdateTime = System.currentTimeMillis();
        builder.setContentTitle(title);
        builder.setContentText("Đang tải về... !");
        builder.setSmallIcon(R.drawable.ic_download_notification);
        TaskStackBuilder task = TaskStackBuilder.create(this);
        task.addNextIntent(Utils.getNowPlayingIntent(mContext));
        PendingIntent pengdingIntent = task.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pengdingIntent);
        builder.setProgress(100, 0, false);
        builder.setAutoCancel(true);
        builder.setTicker("Đang tải....");
        notifications.put(id, builder);
        manager.notify(id, builder.build());
    }

    private void updateNotification(int id, int progress) {
        if (checkUpdateInterval()) {
            NotificationCompat.Builder notification = notifications.get(id);
            notification.setContentText("Tải về : " + progress + "%");
            notification.setProgress(100, progress, false);
            manager.notify(id, notification.build());
        }
    }

    private boolean checkUpdateInterval() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastUpdateTime > UPDATE_INTERVAL) {
            mLastUpdateTime = currentTime;
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class ResultResponse extends Handler {
        private WeakReference<DownloaderService> mService;

        private ResultResponse(DownloaderService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            DownloaderService mTarget = mService.get();
            Log.i(TAG, "handleMessage msg " + msg);
            switch (msg.what) {
                case Extras.MSG_UPDATING:
                    Log.i(TAG, "handleMessage progress " + msg.arg1);
                    mTarget.updateNotification(NOTIFICATION_ID, msg.arg1);
                    break;
                case Extras.MSG_SUCCESSED:
                    NotificationCompat.Builder notification = notifications
                            .get(NOTIFICATION_ID);
                    notification.setTicker("Đã lưu xong !");
                    notification.setContentText("Đã lưu xong!");
                    manager.notify(NOTIFICATION_ID, notification.build());
                    notifications.remove(NOTIFICATION_ID);
                    cancelNotification(mContext, NOTIFICATION_ID);
                    mTarget.stopSelf();
                    break;
                case Extras.MSG_FAILED:
                    notification = notifications.get(hashCode());
                    notification.setContentTitle("Lưu thất bại !");
                    notification.setContentText("Thất bại!");
                    manager.notify(hashCode(), notification.build());
                    cancelNotification(mContext, NOTIFICATION_ID);
                    mTarget.stopSelf();
                    break;
            }
        }
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx
                .getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    private String validateFileName(String musicName, String fileStream) {
        String[] ReservedChars = {"|", "\\", "?", "*", "<", "\"", ":", ">"};
        if (fileStream.endsWith(".flac")) {
            return StringUtils.replaceEach(musicName, ReservedChars,
                    new String[]{" ", " ", " ", " ", " ", " ", " ", " "}).concat(".flac");
        } else if (fileStream.endsWith(".m4a")) {
            return  StringUtils.replaceEach(musicName, ReservedChars,
                    new String[]{" ", " ", " ", " ", " ", " ", " ", " "}).concat(".m4a");
        } else {
            return  StringUtils.replaceEach(musicName, ReservedChars,
                    new String[]{" ", " ", " ", " ", " ", " ", " ", " "}).concat(".mp3");
        }
    }

    private boolean enableDownload(int newId) {
        return oldId == 0 || oldId != newId;
    }
}
