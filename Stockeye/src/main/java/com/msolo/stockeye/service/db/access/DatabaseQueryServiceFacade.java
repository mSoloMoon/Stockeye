package com.msolo.stockeye.service.db.access;

import com.msolo.stockeye.service.db.build.ObjAbstractTranRec;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/19.
 */
public class DatabaseQueryServiceFacade implements DatabaseQueryServiceFacadeImp {

    private static final DatabaseQueryServiceFacade INSTANCE = new DatabaseQueryServiceFacade();

    private WeakReference<QueryTranRec> queryer = null;

    private DatabaseQueryServiceFacade() {}

    public static DatabaseQueryServiceFacade getInstance() {
        return INSTANCE;
    }

    @Override
    public DatabaseQueryServiceFacade setUpQueryer(int queryerType) {

        switch (queryerType) {

            case 0:         // daily
                queryer = new WeakReference<QueryTranRec>(new QueryTranDailyRec());
                break;

            case 1:         // weekly
                queryer = new WeakReference<QueryTranRec>(new QueryTranWeeklyRec());
                break;

            case 2:         // monthly
            case 3:         // quarterly
            case 4:         // yearly
                queryer = new WeakReference<QueryTranRec>(new QueryTranMQYRec());
                break;

            default:
                break;

        }

        return this;

    }

    @Override
    public boolean isTranRecExist(String table, String theQueryRecordId) {
        return queryer.get().isTranRecExist(table, theQueryRecordId);
    }

    @Override
    public ObjAbstractTranRec queryOneTranRec(String table, String theQueryRecordId) {
        return queryer.get().queryOneTranRec(table, theQueryRecordId);
    }

    @Override
    public void queryTranDailyChartRecLimit(int limtCount, String table, long[][] chartTranRecArray) {

        new QueryTranDailyRec().queryTranDailyChartRecLimit(limtCount, table, chartTranRecArray);

    }

    @Override
    public void queryTranMQYChartRecRange(int adjustFactor, String recordIdRange, String table, long[][] recordArray) {
        new QueryTranMQYRec().queryTranMQYChartRecRange(adjustFactor, recordIdRange, table, recordArray);
    }

    @Override
    public void queryTranRecRange(boolean indicator, String table, String recordIdRange, Object objTranRecArrayList) {
        queryer.get().queryTranRecRange(indicator, table, recordIdRange, (ArrayList) objTranRecArrayList);
    }



}
