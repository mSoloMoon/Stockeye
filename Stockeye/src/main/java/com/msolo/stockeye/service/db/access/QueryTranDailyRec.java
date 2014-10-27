package com.msolo.stockeye.service.db.access;

import android.database.Cursor;
import android.net.Uri;

import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.service.db.build.DatabaseObjConfigure;
import com.msolo.stockeye.service.db.build.ObjTranDailyRec;

import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/19.
 */
public class QueryTranDailyRec extends QueryTranRec<ObjTranDailyRec> {

    private static final String URI_QUERY_DAILY_RECORD = "content://com.msolo.stockeye.db.TranRecContentProvider/records/daily/";
    private static final String URI_QUERY_DAILY_RECORD_RANGE = "content://com.msolo.stockeye.db.TranRecContentProvider/records/daily/range/";
    private static final String URI_QUERY_DAILY_RECORD_LIMIT = "content://com.msolo.stockeye.db.TranRecContentProvider/records/daily/limit/";

    private Cursor recordCursor = null;
    private ObjTranDailyRec objTranDailyRec;

    public QueryTranDailyRec() {}

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

        Uri uri = Uri.parse( URI_QUERY_DAILY_RECORD + theQueryDate );
        recordCursor = StockeyeApp.appContentResolver.query(uri, null, table, null, null);

        boolean existFlag = recordCursor.getCount() > 0 ? true : false;

        recordCursor.close();

        return existFlag;

    }

    /**
     *
     * query one daily transaction record
     *
     * @param table
     * @param theQueryDate daily transaction record's id: date(YYYYMMDD);
     * @return void
     *
     */
    @Override
    protected ObjTranDailyRec queryOneTranRec(String table, String theQueryDate) {

        objTranDailyRec = new ObjTranDailyRec();

        Uri uri = Uri.parse( URI_QUERY_DAILY_RECORD + theQueryDate );
        recordCursor = StockeyeApp.appContentResolver.query(uri, null, table, null, null);

        if ( recordCursor.moveToNext() ) {
            DatabaseObjConfigure.setObjTranDailyRec(true, recordCursor, objTranDailyRec);
        } else {
            return null;
        }

        recordCursor.close();

        return objTranDailyRec;

    }

    /**
     *
     * query daily transaction records by range date and store it to tranDailyRecObjList
     * set all fields - daily transaction record
     *
     * @param indicatorFullySet boolean
     * @param table String
     * @param dailyRange String date1date2(YYYYMMDDYYYYMMDD)
     * @param objTranDailyRecArrayList store the daily transaction records
     * @return void
     *
     */
    @Override
    protected void queryTranRecRange(boolean indicatorFullySet, String table, String dailyRange, ArrayList<ObjTranDailyRec> objTranDailyRecArrayList) {

        Uri uri = Uri.parse( URI_QUERY_DAILY_RECORD_RANGE + dailyRange );

        recordCursor = StockeyeApp.appContentResolver.query(uri, null, table, null, null);
        if (recordCursor != null) {

            while ( recordCursor.moveToNext() ) {

                ObjTranDailyRec obj = new ObjTranDailyRec();
                DatabaseObjConfigure.setObjTranDailyRec(indicatorFullySet, recordCursor, obj);
                objTranDailyRecArrayList.add(obj);

            }

        }

        recordCursor.close();

    }
    
    protected void queryTranDailyChartRecLimit(int limtCount, String table, long[][] chartTranDailyRecArray) {

        Uri uri = Uri.parse( URI_QUERY_DAILY_RECORD_LIMIT + limtCount );

        recordCursor = StockeyeApp.appContentResolver.query(uri, null, table, null, null);
        if ( recordCursor != null ) {

            int lineIdx = 1;
            int count = recordCursor.getCount();
            while ( lineIdx <= count ) {

                recordCursor.moveToNext();

                DatabaseObjConfigure.setArrayTranDailyChartRec(lineIdx, recordCursor, chartTranDailyRecArray);
                lineIdx++;

            }

            chartTranDailyRecArray[0][0] = lineIdx;

        }

        recordCursor.close();

    }

}
