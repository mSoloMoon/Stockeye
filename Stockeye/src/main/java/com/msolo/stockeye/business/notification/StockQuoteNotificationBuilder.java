package com.msolo.stockeye.business.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.msolo.stockeye.R;
import com.msolo.stockeye.StockeyeApp;

/**
 * Created by mSolo on 2014/10/17.
 */
public class StockQuoteNotificationBuilder {

    private static final StockQuoteNotificationBuilder INSTANCE = new StockQuoteNotificationBuilder();

    private StringBuilder stockQuoteStrBuilder = null;

    private NotificationManager notificationManager = null;
    private NotificationCompat.Builder builder = null;

    private PendingIntent StockQuotePendingIntent;
    private RemoteViews remoteViews;

    private StockQuoteNotificationBuilder() {

        notificationManager = (NotificationManager) StockeyeApp.appContext.getSystemService(StockeyeApp.appContext.NOTIFICATION_SERVICE);

        builder = new NotificationCompat.Builder(StockeyeApp.appContext);

        StockQuotePendingIntent = StockeyeApp.activityMainPendingIntent;

        remoteViews = new RemoteViews(StockeyeApp.appContext.getPackageName(), R.layout.notification_stocke_quote);

        stockQuoteStrBuilder = new StringBuilder();
    }

    public static StockQuoteNotificationBuilder getInstance() {
        return INSTANCE;
    }

    public void setupNotification(String[] stocknameArray, String[] priceArray) {

        stockQuoteStrBuilder.append("股市眼 - 股票报价:\n");
        for (int i=0, size=stocknameArray.length; i<size; i++) {
            stockQuoteStrBuilder.append(String.format("%s : \t %s\n", stocknameArray[i], priceArray[i]));
        }

        stockQuoteStrBuilder.deleteCharAt(stockQuoteStrBuilder.length() - 1);
        remoteViews.setTextViewText( R.id.noti_stock_quote, stockQuoteStrBuilder.toString());
        builder.setContent(remoteViews);

        displayNotification();

    }

    private void displayNotification() {

        Notification notification;

        if (android.os.Build.VERSION.SDK_INT < 16) {
            notification = builder.getNotification();
        } else {
            notification = builder.build();
        }

        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notification.contentIntent = StockQuotePendingIntent;

        notificationManager.notify(1, notification);

    }

}
