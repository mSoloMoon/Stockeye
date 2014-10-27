package com.msolo.stockeye.business.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import com.msolo.stockeye.R;
import com.msolo.stockeye.StockeyeApp;

/**
 * Created by mSolo on 2014/9/4.
 */
public class TransactionDataWorkNotificationBuilder {

    private String stockname = null;

    private NotificationManager notificationManager = null;
    private Notification.Builder builder = null;

    private PendingIntent downloadPendingIntent;

    public TransactionDataWorkNotificationBuilder() {}

    public void init() {

        notificationManager = (NotificationManager) StockeyeApp.appContext.getSystemService(StockeyeApp.appContext.NOTIFICATION_SERVICE);

        builder = new Notification.Builder(StockeyeApp.appContext);
        builder.setSmallIcon(R.drawable.icon_download);

        downloadPendingIntent = StockeyeApp.activityMainPendingIntent;

    }

    public void setupNotification(String name, String date) {

        stockname = name;

        String dateFmt = new StringBuilder().append(date.substring(0, 4)).append("-")
                .append(date.substring(4, 6)).append("-")
                .append(date.substring(6, 8)).toString();
        builder.setContentTitle("股票数据下载中...");
        builder.setContentText( String.format("%s : 更新至 %s, 进度: 0%%", stockname, dateFmt) );
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            builder.setProgress(100, 0, false);
        }

        displayNotification(false);

    }

    public void updateNotification(int alreadyDownloadNumber, int totalNumberOfRecord, String date) {

        float progressVal = (float)alreadyDownloadNumber / totalNumberOfRecord * 100;

        if (android.os.Build.VERSION.SDK_INT >= 14) {
            builder.setProgress(100, (int)progressVal, false);
        }
        builder.setContentText( String.format("%s : 更新至 %s, 进度: %.2f%%", stockname, date, progressVal) );

        if ( 100 == (int)progressVal ) {
            builder.setContentTitle("股票数据下载完成!");
            displayNotification(true);
        } else {
            displayNotification(false);
        }

    }

    private void displayNotification(boolean isAutoClear) {

        Notification notification;

        if (android.os.Build.VERSION.SDK_INT <16) {
            notification = builder.getNotification();
        } else {
            notification = builder.build();
        }

        if (isAutoClear) {
            notification.flags = Notification.FLAG_AUTO_CANCEL;
        } else {
            notification.flags = Notification.FLAG_ONGOING_EVENT;
        }

        notification.contentIntent = downloadPendingIntent;

        notificationManager.notify(0, notification);

    }

}
