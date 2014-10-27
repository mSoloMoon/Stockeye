package com.msolo.stockeye.service.userdata;

/**
 * Created by mSolo on 2014/8/17.
 */
public interface StockPriceSubjectImp {

    public void attachObserveStockPrice(int uniqueRegisterNumber, StockPriceObserverImp observer);
    public void detachObserveStockPrice(int uniqueRegisterNumber);

}
