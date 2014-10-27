package com.msolo.stockeye.gui.activity.quotation;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.service.common.CommonServiceFacade;
import com.msolo.stockeye.service.userdata.StockPriceRefreshing;
import com.msolo.stockeye.service.userdata.StockQuoteData;

/**
 * Created by mSolo on 2014/8/8.
 */
public class StockQuoteMediator implements StockQuoteMediatorImp {

    private static final StockQuoteMediator INSTANCE = new StockQuoteMediator();

    private StockQuoteData stockQuoteData = null;
    private StockQuoteView stockQuoteView = null;

    private StockPriceRefreshing stockPriceRefreshing;

    private Activity activityMain = null;
    private FragmentManager supportFragmentManager = null;

    private StockQuoteMediator() {}

    public static StockQuoteMediator getInstance() {
        return INSTANCE;
    }

    public void setActivityMain(Activity activity) {

        if (activityMain != null) {
            return ;
        }
        activityMain = activity;

    }

    public void setSupportFragmentManager(FragmentManager fm) {
        supportFragmentManager = fm;
    }

    public void startStockPriceRefreshing() {

        stockPriceRefreshing = new StockPriceRefreshing();

        stockQuoteData.attachObserveStockcode(stockPriceRefreshing.hashCode(), stockPriceRefreshing);
        stockQuoteData.attachObserveStockcode(stockQuoteView.hashCode(), stockQuoteView);

        stockQuoteData.attachObserveStockPrice(stockQuoteView.hashCode(), stockQuoteView);
        stockPriceRefreshing.attachObserveStockPrice(stockQuoteData.hashCode(), stockQuoteData);

        if (CommonServiceFacade.getInstance().isNetworkConnected()) {
            stockPriceRefreshing.startWork(stockQuoteData.hashCode(), stockQuoteData.getStockcodeArray());
        }

    }

    public void endStockPriceRefreshing() {

        stockQuoteData.detachObserveStockcode(stockPriceRefreshing.hashCode());
        stockQuoteData.detachObserveStockcode(stockQuoteView.hashCode());

        stockQuoteData.detachObserveStockPrice(stockQuoteView.hashCode());
        stockPriceRefreshing.detachObserveStockPrice(stockQuoteData.hashCode());

        if (CommonServiceFacade.getInstance().isNetworkConnected()) {
            stockPriceRefreshing.shutdownWork(stockQuoteData.hashCode());
        }

        stockPriceRefreshing = null;

    }

    protected Activity getActivityMain() {
        return activityMain;
    }

    protected FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }

    @Override
    public String[] getStockcodeArray() {
        return stockQuoteData.getStockcodeArray();
    }

    @Override
    public String[] getStockDateArray() {
        return stockQuoteData.getStockDateArray();
    }

    @Override
    public String[] getStocknameArray() {
        return stockQuoteData.getStocknameArray();
    }

    @Override
    public String[] getStockPriceArray() {
        return stockQuoteData.getStockPriceArray();
    }

    @Override
    public void addOneStockToQuote(String stockcode, String stockname) {
        stockQuoteData.addOneStock(stockcode, stockname);
    }

    @Override
    public void removeOneStock(String stockcode) {
        stockQuoteData.removeOneFromStockList(stockcode);
    }

    @Override
    public void setUpStockQuoteData(StockQuoteData quoteData) {
        stockQuoteData = quoteData;
    }

    @Override
    public void setUpStockQuoteView(StockQuoteView quoteView) {
        stockQuoteView = quoteView;
    }

    @Override
    public void toastMessage(final String message) {

        activityMain.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StockeyeApp.appContext, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void updateStockCurrentUpdatedDate(String stockcode, String date) {
        stockQuoteData.updateStockCurrentUpdatedDate(stockcode, date);
    }

}
