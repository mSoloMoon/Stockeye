package com.msolo.stockeye.business.stock;

import android.content.Intent;
import android.content.IntentFilter;

import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.gui.activity.quotation.StockQuoteMediator;
import com.msolo.stockeye.service.daemon.StockServiceWakefulReceiver;
import com.msolo.stockeye.setting.StockSetting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by mSolo on 2014/8/18.
 */
public class DownloadStockTransactionDataController {

    private static final DownloadStockTransactionDataController INSTANCE = new DownloadStockTransactionDataController();
    
    private boolean isActivityExist = true;

    private StockServiceWakefulReceiver stockServiceWakefulReceiver = null;

    private LinkedBlockingQueue<String> downloadStockcodeQueue = new LinkedBlockingQueue<String>(10);

    private Queue<String> downloadStocknameQueue = new LinkedList<String>();
    private Queue<String> downloadStockDateQueue = new LinkedList<String>();

    private ArrayList<String> downloadedStockcodeArrayList = new ArrayList<String>();

    private DownloadStockTransactionDataController() {}

    public static DownloadStockTransactionDataController getInstance() {
        return INSTANCE;
    }

    public boolean isActivityExist() {
        return isActivityExist;
    }

    public boolean isDownloadStockcodeListEmpty() {
        return downloadStockcodeQueue.isEmpty();
    }

    public String[] getOneStockcodeToDownload() {

        String[] stock = new String[3];
        try {

            stock[0] = downloadStockcodeQueue.take();
            stock[1] = downloadStocknameQueue.poll();
            stock[2] = downloadStockDateQueue.poll();

        } catch (InterruptedException e) {
        } finally {
            // log
            downloadedStockcodeArrayList.add(stock[0]);

            if (downloadStockcodeQueue.isEmpty()) {
                downloadedStockcodeArrayList.clear();
            }

        }

        return stock;

    }

    public int updateDownloadStockcodeList() {

        StockQuoteMediator mediator = StockQuoteMediator.getInstance();

        String[] stockcodeArray = mediator.getStockcodeArray();
        String[] stocknameArray = mediator.getStocknameArray();
        String[] stockDateArray = mediator.getStockDateArray();

        int size = stockcodeArray.length;
        if (size < 3) {
            return 0;
        }

        int counter = 0;
        for (int i=2; i<size; i++) {

            if ( !downloadStockcodeQueue.contains(stockcodeArray[i]) &&
                 !downloadedStockcodeArrayList.contains(stockcodeArray[i]) ) {
                downloadStocknameQueue.add(stocknameArray[i]);
                downloadStockDateQueue.add(stockDateArray[i-2]);
                downloadStockcodeQueue.add(stockcodeArray[i]);
                counter++;
            }

        }

        return counter;

    }

    public void startDownloadService() {

        isActivityExist = true;

        if ( StockSetting.getInstance().getWakefulFlag() ) {

            IntentFilter intentFilter = new IntentFilter("SERVICE_WAKEFUL_RECEIVER");
            stockServiceWakefulReceiver = new StockServiceWakefulReceiver();
            StockeyeApp.appContext.registerReceiver(stockServiceWakefulReceiver, intentFilter);

            Intent intent = new Intent("SERVICE_WAKEFUL_RECEIVER");
            intent.putExtra("service_type", "download");
            StockeyeApp.appContext.sendBroadcast(intent);

        } else {
            StockeyeApp.appContext.startService(new Intent(StockeyeApp.appContext, DownloadStockTransactionDataService.class));
        }

    }

    public void stopDownloadService() {

        isActivityExist = false;

        if ( StockSetting.getInstance().getWakefulFlag() &&
             stockServiceWakefulReceiver != null ) {

            StockeyeApp.appContext.unregisterReceiver(stockServiceWakefulReceiver);
            stockServiceWakefulReceiver = null;

            if (downloadStockcodeQueue.isEmpty()) {
                StockeyeApp.appContext.stopService(new Intent(StockeyeApp.appContext, DownloadStockTransactionDataService.class));
            }

        } else {
            StockeyeApp.appContext.stopService(new Intent(StockeyeApp.appContext, DownloadStockTransactionDataService.class));
        }

    }
    
}
