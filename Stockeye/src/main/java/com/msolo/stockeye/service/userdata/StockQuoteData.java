package com.msolo.stockeye.service.userdata;

import android.content.SharedPreferences;
import android.util.SparseArray;

import com.msolo.stockeye.R;
import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.gui.activity.quotation.FactoryStockQuoteViewData;
import com.msolo.stockeye.setting.StockSetting;

import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/7.
 */
public final class StockQuoteData implements StockcodeSubjectImp, StockPriceSubjectImp, StockPriceObserverImp {

    private SparseArray<StockcodeObserverImp> observerStockcodSparseArray = null;
    private SparseArray<StockPriceObserverImp> observerStockPriceSparseArray = null;

    private ArrayList<Integer> observerStockcodeSparseArrayKey = null;
    private ArrayList<Integer> observerStockPriceSparseArrayKey = null;

    private ArrayList<String> stockcodeList = null;
    private ArrayList<String> stocknameList = null;
    private ArrayList<String> stockPriceList = null;
    private ArrayList<String> stockCurrentUpdatedDateList = null;

    public StockQuoteData() {

        stockcodeList = new ArrayList<String>();
        stockcodeList.ensureCapacity(10);
        stocknameList = new ArrayList<String>();
        stocknameList.ensureCapacity(10);
        stockPriceList = new ArrayList<String>();
        stockPriceList.ensureCapacity(10);
        stockCurrentUpdatedDateList = new ArrayList<String>();
        stockCurrentUpdatedDateList.ensureCapacity(10);

        observerStockcodSparseArray = new SparseArray<StockcodeObserverImp>(3);
        observerStockPriceSparseArray = new SparseArray<StockPriceObserverImp>(3);
        observerStockcodeSparseArrayKey = new ArrayList<Integer>();
        observerStockPriceSparseArrayKey = new ArrayList<Integer>();

        loadSharedPrefToMemory();

    }

    public void addOneStock(String stockcode, String stockname) {

        stockcodeList.add(stockcode);
        stocknameList.add(stockname);
        stockPriceList.add("0.00(0.00%)");
        stockCurrentUpdatedDateList.add(StockSetting.getInstance().getStartDateForStock());

        stockcodeListChangeNotify();

        loadMemoryToSharedPre();

    }

    // getter
    // pre : loadSharedPrefToMemory()
    // ----------------------------------------------------

    /**
     *
     *  return a String array copy of stockcodeList,
     *  this copy should not be empty,
     *  and at least should include sh000001 and sz399001 (default).
     *
     *  @return String[]
     *
     */
    public String[] getStockcodeArray() {

        int size = stockcodeList.size();
        String[] stockcodeArray = new String[size];
        for (int i=0; i<size; i++) {
            stockcodeArray[i] = stockcodeList.get(i);
        }

        return stockcodeArray;

    }

    public String[] getStockDateArray() {

        int size = stockCurrentUpdatedDateList.size();

        String[] stockCurrentUpdatedDateArray = new String[size - 2];
        for (int i=2; i<size; i++) {
            stockCurrentUpdatedDateArray[i-2] = stockCurrentUpdatedDateList.get(i);
        }

        return stockCurrentUpdatedDateArray;

    }

    public String[] getStocknameArray() {

        int size = stocknameList.size();
        String[] stocknameArray = new String[size];
        for (int i=0; i<size; i++) {
            stocknameArray[i] = stocknameList.get(i);
        }

        return stocknameArray;

    }

    public String[] getStockPriceArray() {

        int size = stockPriceList.size();
        String[] stockPriceArray = new String[size];
        for (int i=0; i<size; i++) {
            stockPriceArray[i] = stockPriceList.get(i);
        }

        return stockPriceArray;

    }

    // -------------------------------------------------------------------

    private void loadSharedPrefToMemory() {

        String stockcodeListStr = StockeyeApp.appSharedPref.getString("stockcode_list", "");
        if (!stockcodeListStr.isEmpty()) {

            String stocknameListStr = StockeyeApp.appSharedPref.getString("stockname_list", "");
            String stockPriceListStr =  StockeyeApp.appSharedPref.getString("stockprice_list", "");
            String stockCurrentUpdatedDateListStr = StockeyeApp.appSharedPref.getString("stock_current_updated_date_list", "");

            String[] stockcodes = stockcodeListStr.split("_");
            String[] stocknames = stocknameListStr.split("_");
            String[] stockPrices = stockPriceListStr.split("_");
            String[] stockCurrentUpdatedDates = stockCurrentUpdatedDateListStr.split("_");

            for ( int i=0, size=stockcodes.length; i<size; i++ ) {
                stockcodeList.add(stockcodes[i]);
                stocknameList.add(stocknames[i]);
                stockPriceList.add(stockPrices[i]);
                stockCurrentUpdatedDateList.add(stockCurrentUpdatedDates[i]);
            }

            return ;

        }

        for ( String stock : StockeyeApp.appContext.getResources().getStringArray(R.array.default_stock_array)) {

            String[] infosOfStock = stock.split("_", 3);
            stockcodeList.add(infosOfStock[0]);
            stocknameList.add(infosOfStock[1]);
            stockPriceList.add(infosOfStock[2]);
            stockCurrentUpdatedDateList.add("0");

        }

    }

    private void loadMemoryToSharedPre() {

        int size = stockcodeList.size();

        StringBuilder stockcodeBuilder = new StringBuilder();
        StringBuilder stocknameBuilder = new StringBuilder();
        StringBuilder stockPriceBuilder = new StringBuilder();
        StringBuilder stockCurrentUpdatedDateBuilder = new StringBuilder();

        for (int i=0; i<size; i++) {
            stockcodeBuilder.append(stockcodeList.get(i)).append("_");
            stocknameBuilder.append(stocknameList.get(i)).append("_");
            stockPriceBuilder.append(stockPriceList.get(i)).append("_");
            stockCurrentUpdatedDateBuilder.append(stockCurrentUpdatedDateList.get(i)).append("_");
        }

        stockcodeBuilder.deleteCharAt(stockcodeBuilder.length() - 1);
        stocknameBuilder.deleteCharAt(stocknameBuilder.length() - 1);
        stockPriceBuilder.deleteCharAt(stockPriceBuilder.length() - 1);
        stockCurrentUpdatedDateBuilder.deleteCharAt(stockCurrentUpdatedDateBuilder.length() - 1);

        saveSharePrefData(stockcodeBuilder,
                stocknameBuilder,
                stockPriceBuilder,
                stockCurrentUpdatedDateBuilder);

    }

    private void saveSharePrefData(StringBuilder stockcodeBuilder,
                                  StringBuilder stocknameBuilder,
                                  StringBuilder stockPriceBuilder,
                                  StringBuilder stockCurrentUpdatedDateBuilder) {

        SharedPreferences.Editor editor = StockeyeApp.appSharedPref.edit();

        editor.remove("stockcode_list");
        editor.remove("stockname_list");
        editor.remove("stockprice_list");
        editor.remove("stock_current_updated_date_list");

        editor.putString("stockcode_list", stockcodeBuilder.toString());
        editor.putString("stockname_list", stocknameBuilder.toString());
        editor.putString("stockprice_list", stockPriceBuilder.toString());
        editor.putString("stock_current_updated_date_list", stockCurrentUpdatedDateBuilder.toString());

        editor.commit();

    }

    public void removeOneFromStockList(String stockcode) {

        int idx = stockcodeList.indexOf(stockcode);
        removeOneFromStockListByIndex(idx);

    }

    protected void removeOneFromStockListByIndex(int idxStock) {

        stockcodeList.remove(idxStock);
        stocknameList.remove(idxStock);
        stockPriceList.remove(idxStock);
        stockCurrentUpdatedDateList.remove(idxStock);

        stockcodeListChangeNotify();

        loadMemoryToSharedPre();

    }

    /**
     * for stockcode list observe
     *
     * @param uniqueReg
     * @param observer
     */
    @Override
    public void attachObserveStockcode(int uniqueReg, StockcodeObserverImp observer) {
        observerStockcodSparseArray.put(uniqueReg, observer);
        observerStockcodeSparseArrayKey.add(uniqueReg);
    }

    @Override
    public void detachObserveStockcode(int uniqueReg) {
        observerStockcodSparseArray.remove(uniqueReg);
        observerStockcodeSparseArrayKey.remove(Integer.valueOf(uniqueReg));
    }

    @Override
    public void attachObserveStockPrice(int uniqueRegisterNumber, StockPriceObserverImp observer) {
        observerStockPriceSparseArray.put(uniqueRegisterNumber, observer);
        observerStockPriceSparseArrayKey.add(uniqueRegisterNumber);
    }

    @Override
    public void detachObserveStockPrice(int uniqueRegisterNumber) {
        observerStockPriceSparseArray.remove(uniqueRegisterNumber);
        observerStockPriceSparseArrayKey.remove(Integer.valueOf(uniqueRegisterNumber));
    }

    @Override
    public void update(String[] stockPriceArray) {

        for (int i=0, len=stockPriceArray.length; i<len; i++) {
            if (stockPriceArray[i].equals("0.00(0.00%)")) {
                stockPriceArray[i] = stockPriceList.get(i);
            }
        }

        String[] percentageStockPriceArray = FactoryStockQuoteViewData.getStockQuoteViewDataProduct(
                StockSetting.StockQuotationStyleProductType.Percentage, stockPriceArray);

        stockPriceList.clear();

        for (String stockPrice : percentageStockPriceArray) {
            stockPriceList.add(stockPrice);
        }

        for (int i=0, size=observerStockPriceSparseArrayKey.size(); i<size; i++) {
            int key = observerStockPriceSparseArrayKey.get(i);
            observerStockPriceSparseArray.get(key).update(percentageStockPriceArray);
        }

    }

    public void updateStockCurrentUpdatedDate(String stockcode, String date) {

        int idx = stockcodeList.indexOf(stockcode);
        stockCurrentUpdatedDateList.set(idx, date);

        loadMemoryToSharedPre();

    }


    private void stockcodeListChangeNotify() {

        for (int i=0, size=observerStockcodeSparseArrayKey.size(); i<size; i++) {

            observerStockcodSparseArray.get(observerStockcodeSparseArrayKey.get(i)).update(this.hashCode(), this.getStockcodeArray());
        }

    }

}
