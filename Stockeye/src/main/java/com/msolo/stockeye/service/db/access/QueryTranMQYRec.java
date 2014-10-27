package com.msolo.stockeye.service.db.access;

import android.database.Cursor;
import android.net.Uri;

import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.service.db.build.DatabaseObjConfigure;
import com.msolo.stockeye.service.db.build.ObjTranWMQYRec;

import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/20.
 */
public class QueryTranMQYRec extends QueryTranRec<ObjTranWMQYRec> {

    private static final String URI_QUERY_MQY_RECORD = "content://com.msolo.stockeye.db.TranRecContentProvider/records/mqy/";
    private static final String URI_QUERY_MQY_RECORD_RANGE = "content://com.msolo.stockeye.db.TranRecContentProvider/records/mqy/range/";

    private Cursor recordCursor = null;
    private ObjTranWMQYRec objTranMQYRec;

    public QueryTranMQYRec() {}

    /**
     *
     * Check weather the database's transaction record is exist or not
     *
     * @param table
     * @param theQueryDate transaction record's id: daily [ date(YYYYMMDD) ]
     * @return boolean
     *
     */
    @Override
    protected boolean isTranRecExist(String table, String theQueryDate) {

        Uri uri = Uri.parse( URI_QUERY_MQY_RECORD + theQueryDate );
        recordCursor = StockeyeApp.appContentResolver.query(uri, null, table, null, null);

        boolean existFlag = recordCursor.moveToFirst() ? true : false;

        recordCursor.close();

        return existFlag;

    }

    /**
     *
     * query one mqy transaction record
     *
     * @param table
     * @param theQueryDate daily transaction record's id: month(YYYYMM), quarter(YYYYQ), year(YYYY);
     * @return void
     *
     */
    @Override
    protected ObjTranWMQYRec queryOneTranRec(String table, String theQueryDate) {

        objTranMQYRec = new ObjTranWMQYRec();

        Uri uri = Uri.parse( URI_QUERY_MQY_RECORD + theQueryDate );
        recordCursor = StockeyeApp.appContentResolver.query(uri, null, table, null, null);

        if ( recordCursor.moveToFirst() ) {
            DatabaseObjConfigure.setObjTranMQYRec(true, recordCursor, objTranMQYRec);
        } else {
            return null;
        }

        recordCursor.close();

        return objTranMQYRec;

    }

    /**
     *
     * query daily transaction records by range date and store it to tranDailyRecObjList
     * set all fields - daily transaction record
     *
     * @param indicator
     * @param table
     * @param mqyRange : mqy1mqy2(YYYYMMYYYYMM or YYYYQYYYYQ or YYYYYYYY)
     * @param objTranMQYRecArrayList : store the daily transaction records
     * @return void
     *
     */
    @Override
    protected void queryTranRecRange(boolean indicator, String table, String mqyRange, ArrayList<ObjTranWMQYRec> objTranMQYRecArrayList) {

        Uri uri = Uri.parse( URI_QUERY_MQY_RECORD_RANGE + mqyRange );

        recordCursor = StockeyeApp.appContentResolver.query(uri, null, table, null, null);
        if (recordCursor != null) {

            while ( recordCursor.moveToNext() ) {

                ObjTranWMQYRec obj = new ObjTranWMQYRec();
                DatabaseObjConfigure.setObjTranMQYRec(indicator, recordCursor, obj);
                objTranMQYRecArrayList.add(obj);

            }

        }

        recordCursor.close();

    }

    protected void queryTranMQYChartRecRange(int adjustFactor, String mqyRange, String table, long[][] chartTranMQYRecArray) {

        Uri uri = Uri.parse( URI_QUERY_MQY_RECORD_RANGE + mqyRange );

        recordCursor = StockeyeApp.appContentResolver.query(uri, null, table, null, null);
        if ( recordCursor != null ) {

            int count = recordCursor.getCount();
            int lineIdx = count + adjustFactor;
            while ( lineIdx > adjustFactor ) {

                recordCursor.moveToNext();
                DatabaseObjConfigure.setArrayTranMQYChartRec(lineIdx, recordCursor, chartTranMQYRecArray);
                lineIdx--;

            }

            chartTranMQYRecArray[0][0] = count + 1 + adjustFactor;

        }

        recordCursor.close();

    }

}
