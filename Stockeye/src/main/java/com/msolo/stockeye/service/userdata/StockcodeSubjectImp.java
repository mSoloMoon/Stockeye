package com.msolo.stockeye.service.userdata;

/**
 * Created by mSolo on 2014/8/8.
 */
public interface StockcodeSubjectImp {

    public void attachObserveStockcode(int uniqueRegisterNumber, StockcodeObserverImp observer);
    public void detachObserveStockcode(int uniqueRegisterNumber);

}
