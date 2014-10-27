package com.msolo.stockeye.business.stock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.msolo.stockeye.business.notification.TransactionDataWorkNotificationBuilder;
import com.msolo.stockeye.service.daemon.StockServiceWakefulReceiver;
import com.msolo.stockeye.setting.StockSetting;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by mSolo on 2014/8/13.
 */
public class DownloadStockTransactionDataService extends Service {

    protected TransactionDataWorkNotificationBuilder buildNotification = null;

    private Intent serviceIntent = null;
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

    @Override
    public void onCreate() {

        super.onCreate();

        buildNotification = new TransactionDataWorkNotificationBuilder();
        buildNotification.init();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        executor.shutdown();

        if (StockSetting.getInstance().getWakefulFlag()) {
            StockServiceWakefulReceiver.completeWakefulIntent(serviceIntent);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        serviceIntent = intent;

        executor.execute(new BuildTransactionDataWork(this));

        return START_STICKY;

    }

    protected TransactionDataWorkNotificationBuilder getBuildTransactionDataWorkNotification() {
        return buildNotification;
    }

}
