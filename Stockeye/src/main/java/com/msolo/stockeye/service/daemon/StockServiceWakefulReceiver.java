package com.msolo.stockeye.service.daemon;

/**
 * Created by mSolo on 2014/8/13.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.msolo.stockeye.business.stock.DownloadStockTransactionDataService;

public class StockServiceWakefulReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent serviceIntent = null;

        String serviceType = intent.getStringExtra("service_type");
        if (serviceType.equals("download")) {
            serviceIntent = new Intent(context, DownloadStockTransactionDataService.class);
        }

        startWakefulService(context, serviceIntent);

    }

}
