package com.msolo.stockeye.service.db.access;

import android.database.Cursor;
import android.net.Uri;

import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.service.db.build.DatabaseObjConfigure;
import com.msolo.stockeye.service.db.build.ObjTranDailyRec;
import com.msolo.stockeye.service.db.build.ObjTranWMQYRec;

import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/22.
 */
public class QueryTranWeeklyRec extends QueryTranRec<ObjTranDailyRec> {

    private static final String URI_QUERY_DAILY_RECORD_RANGE = "content://com.msolo.stockeye.db.TranRecContentProvider/records/daily/range/";

    private Cursor recordCursor = null;
    private ObjTranWMQYRec objTranWeeklyRec;

    public QueryTranWeeklyRec() {}

    /**
     *
     * query weekly transaction records by range date and store it to objTranDailyRecArrayList
     * set all fields - daily transaction record
     *
     * @param indicator boolean
     * @param table String
     * @param dailyRange String date1date2(YYYYMMDDYYYYMMDD)
     * @return void
     *
     */
    @Override
    protected void queryTranRecRange(boolean indicator, String table, String dailyRange, ArrayList<ObjTranDailyRec> objTranDailyRecArrayList) {

        Uri uri = Uri.parse( URI_QUERY_DAILY_RECORD_RANGE + dailyRange );

        recordCursor = StockeyeApp.appContentResolver.query(uri, null, table, null, null);
        if (recordCursor != null) {

            while ( recordCursor.moveToNext() ) {

                ObjTranDailyRec obj = new ObjTranDailyRec();
                DatabaseObjConfigure.setObjTranDailyRec(indicator, recordCursor, obj);
                objTranDailyRecArrayList.add(obj);

            }

        }

        recordCursor.close();

    }

    private ObjTranWMQYRec getObjTranWeeklyRec(String[] weeklyRecordItemArray) {

        objTranWeeklyRec = new ObjTranWMQYRec();
        DatabaseObjConfigure.setObjTranMQYRec( weeklyRecordItemArray, objTranWeeklyRec );

        return objTranWeeklyRec;

    }

}
