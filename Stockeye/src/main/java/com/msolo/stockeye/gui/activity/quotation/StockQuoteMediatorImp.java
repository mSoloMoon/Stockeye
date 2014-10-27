package com.msolo.stockeye.gui.activity.quotation;

import com.msolo.stockeye.service.userdata.StockQuoteData;

/**
 * Created by mSolo on 2014/8/13.
 */
public interface StockQuoteMediatorImp {

    public String[] getStockcodeArray();
    public String[] getStockDateArray();
    public String[] getStocknameArray();
    public String[] getStockPriceArray();

    public void addOneStockToQuote(String stockcode, String stockname);
    public void removeOneStock(String stockcode);

    public void setUpStockQuoteData(StockQuoteData quoteData);
    public void setUpStockQuoteView(StockQuoteView quoteView);

    public void toastMessage(String message);

    public void updateStockCurrentUpdatedDate(String stockcode, String date);

}
