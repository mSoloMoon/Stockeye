package com.msolo.stockeye.service.db.access;

import com.msolo.stockeye.service.db.build.ObjAbstractTranRec;

/**
 * Created by mSolo on 2014/8/19.
 */
public interface DatabaseQueryServiceFacadeImp<T> {

    public boolean isTranRecExist(String table, String theQueryRecordId);
    public ObjAbstractTranRec queryOneTranRec(String table, String theQueryRecordId);

    public void queryTranDailyChartRecLimit(int limtCount, String table, long[][] recordArray);
    public void queryTranMQYChartRecRange(int adjustFactor, String recordIdRange, String table, long[][] recordArray);

    public void queryTranRecRange(boolean indicator, String table, String recordIdRange, T objTranRecArrayList);

    public DatabaseQueryServiceFacadeImp setUpQueryer(int queryerType);

}
