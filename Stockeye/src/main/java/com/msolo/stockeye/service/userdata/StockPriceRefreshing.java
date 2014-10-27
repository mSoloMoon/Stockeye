package com.msolo.stockeye.service.userdata;

import android.os.AsyncTask;
import android.util.SparseArray;

import com.msolo.stockeye.service.common.CommonServiceFacade;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by mSolo on 2014/8/8.
 */
public class StockPriceRefreshing
    implements StockPriceSubjectImp, StockcodeObserverImp {

    private SparseArray<Integer> refreshCounterSparseArray = new SparseArray<Integer>(3);
    private SparseArray<Boolean> cancelRefreshWorkFlagSparseArray = new SparseArray<Boolean>(3);
    private SparseArray<Boolean> newRefreshingTaskFlagSparseArray = new SparseArray<Boolean>(3);

    private SparseArray<String[]> stockcodeSparseArray = new SparseArray<String[]>(3);
    private SparseArray<StockPriceObserverImp> observerStockPriceSparseArray = new SparseArray<StockPriceObserverImp>(3);

    // ----------------------------------------------------

    public StockPriceRefreshing() {}

    public void shutdownWork(int uniqueReg) {
        cancelRefreshWorkFlagSparseArray.put(uniqueReg, true);
    }

    public void startWork(int uniqueReg, String[] stockcodeArray) {

        stockcodeSparseArray.put(uniqueReg, stockcodeArray);
        newRefreshingTaskFlagSparseArray.put(uniqueReg, false);
        cancelRefreshWorkFlagSparseArray.put(uniqueReg, false);
        refreshCounterSparseArray.put(uniqueReg, 0);

        new RefreshingWork().executeOnExecutor(CommonServiceFacade.getInstance().getExecutorServiceCachedThreadPool(), uniqueReg);

    }

    /**
     * for stock price list update
     *
     * @param uniqueReg
     * @param observer
     */
    @Override
    public void attachObserveStockPrice(int uniqueReg, StockPriceObserverImp observer) {
        observerStockPriceSparseArray.put(uniqueReg, observer);
    }

    @Override
    public void detachObserveStockPrice(int uniqueReg) {
        observerStockPriceSparseArray.remove(uniqueReg);
    }

    @Override
    public void update(int uniqueRegNumber, String[] stockcodeArray) {

        if (refreshCounterSparseArray.get(uniqueRegNumber) == 0) {
            startWork(uniqueRegNumber, stockcodeArray);
        } else {
            stockcodeSparseArray.put(uniqueRegNumber, stockcodeArray);
            newRefreshingTaskFlagSparseArray.put(uniqueRegNumber, true);
        }

    }

    private void stockPriceArrayChangeNotify(int uniqueRegNumber, ArrayList<String> priceList) {

        int size = priceList.size();
        String[] stockPriceArray = new String[size];

        for (int i=0; i<size; i++) {
            stockPriceArray[i] = priceList.get(i);
        }

        observerStockPriceSparseArray.get(uniqueRegNumber).update(stockPriceArray);

    }

    private class RefreshingWork extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            int regNumber = params[0].intValue();

            CommonServiceFacade commonServiceFacade = CommonServiceFacade.getInstance();

            int refreshCounter = commonServiceFacade.getTransactionRefreshCounterByTenSecond();

            ArrayList<String> stockPriceList = new ArrayList<String>();
            stockPriceList.ensureCapacity(10);

            while (true) {

                if (cancelRefreshWorkFlagSparseArray.get(regNumber)) {
                    break;
                }

                String[] stockcodeArray = stockcodeSparseArray.get(regNumber);

                for (String stockcode : stockcodeArray) {

                    if (newRefreshingTaskFlagSparseArray.get(regNumber)) {
                        break;
                    }

                    String price = commonServiceFacade.getStockResourceString(1, stockcode);
                    if (null == price) {
                        price = "0.00(0.00%)";
                    }
                    stockPriceList.add( price );

                }

                if (newRefreshingTaskFlagSparseArray.get(regNumber)) {
                    newRefreshingTaskFlagSparseArray.put(regNumber, false);
                    continue;
                } else {

                    stockPriceArrayChangeNotify(regNumber, stockPriceList);
                    stockPriceList.clear();

                    refreshCounter --;
                    refreshCounterSparseArray.put(regNumber, refreshCounter);
                    if (refreshCounter == 0) {
                        break;
                    }

                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {

                    }

                }
            }

            return null;
        }

    }

}
