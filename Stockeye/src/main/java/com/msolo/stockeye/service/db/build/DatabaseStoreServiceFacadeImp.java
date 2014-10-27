package com.msolo.stockeye.service.db.build;

/**
 * Created by mSolo on 2014/8/18.
 */
public interface DatabaseStoreServiceFacadeImp {

    public void cacheOneStockDailyRecord(String[] dailyRecordItemArray);
    public void cacheOneStockMQYRecord(String[] mqyRecordItemArray);

    public void prepareStockTranRecordCycleCache(int dailyCapacity, int mqyCapacity);

    public void submitCachesOfStockDailyRecord(String stockcode);
    public void submitStockMQYRecords(String stockcode);

    public void dropDbTable(String stockcode);

}
