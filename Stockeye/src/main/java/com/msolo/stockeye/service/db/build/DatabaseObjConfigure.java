package com.msolo.stockeye.service.db.build;

import android.database.Cursor;

import com.msolo.stockeye.gui.UtilViewTool;

/**
 * Created by mSolo on 2014/8/23.
 */
public class DatabaseObjConfigure {

    private DatabaseObjConfigure() {}

    public static void setArrayTranDailyChartRec(int line, Cursor cur, long[][] chartRecordArray) {

        int columnId = 0;

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.DATE);
        chartRecordArray[line][0] = cur.getInt(columnId);

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.WEEKDAY);
        chartRecordArray[line][1] = cur.getInt(columnId);

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.LAST);
        chartRecordArray[line][2] = cur.getInt(columnId);
        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.OPEN);
        chartRecordArray[line][3] = cur.getInt(columnId);
        chartRecordArray[line][4] = comparePrice( (int)chartRecordArray[line][3], (int)chartRecordArray[line][2] );

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.CLOSE);
        chartRecordArray[line][5] = cur.getInt(columnId);
        chartRecordArray[line][6] = comparePrice( (int)chartRecordArray[line][5], (int)chartRecordArray[line][2] );

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.HIGHEST);
        chartRecordArray[line][7] = cur.getInt(columnId);
        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.LOWEST);
        chartRecordArray[line][8] = cur.getInt(columnId);

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.TOTAL_PRICE_COUNT);
        chartRecordArray[line][9] = cur.getInt(columnId);
        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.TOTAL_TRAN_COUNT);
        chartRecordArray[line][10] = cur.getInt(columnId);

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.TOTAL_VOL);
        chartRecordArray[line][11] = cur.getLong(columnId);
        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.TOTAL_MONEY);
        chartRecordArray[line][12] = cur.getLong(columnId);

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.PRICE_TIME_GAP);
        chartRecordArray[line][13] = cur.getInt(columnId);

    }

    public static void setArrayTranMQYChartRec(int line, Cursor cur, long[][] chartRecordArray) {

        int columnId = 0;

        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.TABLE_MQY_IDX);
        chartRecordArray[line][0] = cur.getInt(columnId);

        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.DATE_COUNT);
        chartRecordArray[line][1] = cur.getInt(columnId);

        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.LAST);
        chartRecordArray[line][2] = cur.getInt(columnId);
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.OPEN);
        chartRecordArray[line][3] = cur.getInt(columnId);
        chartRecordArray[line][4] = comparePrice( (int)chartRecordArray[line][3], (int)chartRecordArray[line][2] );

        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.CLOSE);
        chartRecordArray[line][5] = cur.getInt(columnId);
        chartRecordArray[line][6] = comparePrice( (int)chartRecordArray[line][5], (int)chartRecordArray[line][2] );

        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.HIGHEST);
        chartRecordArray[line][7] = cur.getInt(columnId);
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.LOWEST);
        chartRecordArray[line][8] = cur.getInt(columnId);

        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.TOTAL_PRICE_COUNT);
        chartRecordArray[line][9] = cur.getInt(columnId);
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.TOTAL_TRAN_COUNT);
        chartRecordArray[line][10] = cur.getInt(columnId);

        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.TOTAL_VOL);
        chartRecordArray[line][11] = cur.getLong(columnId);
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.TOTAL_MONEY);
        chartRecordArray[line][12] = cur.getLong(columnId);

        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.SINGLE_SUM_VOL);
        chartRecordArray[line][13] = cur.getLong(columnId);

    }

    public static void setArrayTranMQYChartRec(int line, ObjTranWMQYRec obj, long[][] chartRecordArray) {

        chartRecordArray[line][0] = obj.getRecordId();
        chartRecordArray[line][1] = obj.getDateCount();

        chartRecordArray[line][2] = obj.getLast();
        chartRecordArray[line][3] = obj.getOpen();
        chartRecordArray[line][4] = comparePrice( (int)chartRecordArray[line][3], (int)chartRecordArray[line][2] );
        chartRecordArray[line][5] = obj.getClose();
        chartRecordArray[line][6] = comparePrice( (int)chartRecordArray[line][5], (int)chartRecordArray[line][2] );

        chartRecordArray[line][7] = obj.getHighest();
        chartRecordArray[line][8] = obj.getLowest();

        chartRecordArray[line][9] = obj.getTotalPriceCount();
        chartRecordArray[line][10] = obj.getTotalTranCount();

        chartRecordArray[line][11] = obj.getTotalVol();
        chartRecordArray[line][12] = obj.getTotalMoney();

        chartRecordArray[line][13] = obj.getSingleSumVol();

    }

    public static void setObjTranDailyRec(boolean isFullySet, Cursor cur, ObjTranDailyRec objTranDailyRec) {

        int columnId = 0;

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.DATE);
        objTranDailyRec.setRecordId(cur.getInt(columnId));

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.WEEKDAY);
        objTranDailyRec.setWeekday(cur.getInt(columnId));

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.LAST);
        objTranDailyRec.setLast( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.OPEN);
        objTranDailyRec.setOpen( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.CLOSE);
        objTranDailyRec.setClose( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.HIGHEST);
        objTranDailyRec.setHighest( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.LOWEST);
        objTranDailyRec.setLowest( cur.getInt(columnId) );

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.TOTAL_VOL);
        objTranDailyRec.setTotalVol( cur.getLong(columnId) );
        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.TOTAL_MONEY);
        objTranDailyRec.setTotalMoney( cur.getLong(columnId) );
        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.TOTAL_TRAN_COUNT);
        objTranDailyRec.setTotalTranCount( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.TOTAL_PRICE_COUNT);
        objTranDailyRec.setTotalPriceCount( cur.getInt(columnId) );

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.PRICE_TIME_GAP);
        objTranDailyRec.setPriceTimeGap( cur.getInt(columnId) );

        columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_SUM_VOL);
        objTranDailyRec.setSingleSumVol( cur.getLong(columnId) );

        if (isFullySet) {

            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_01);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_02);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_03);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_04);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_05);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_06);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_07);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_08);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_09);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_10);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_11);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_12);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_13);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_14);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_15);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_16);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_17);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_18);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_19);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_20);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_21);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_22);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_23);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_24);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_25);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_26);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_27);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_28);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_29);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_30);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_31);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_32);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_33);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.SINGLE_34);
            objTranDailyRec.pushSingleRecQueue( cur.getString(columnId) );

            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_01);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_02);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_03);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_04);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_05);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_06);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_07);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_08);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_09);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_10);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_11);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_12);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_13);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_14);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_15);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.FIFTEEN_MINUTE_16);
            objTranDailyRec.pushFifteenMinuteRecQueue( cur.getString(columnId) );

            columnId = cur.getColumnIndex(SchemaTranDailyRec.Column.PRICE_STAT);
            objTranDailyRec.setPriceStat( cur.getString(columnId) );

        }

    }

    public static void setObjTranMQYRec(boolean isFullySet, Cursor cur, ObjTranWMQYRec objTranMQYRec) {

        int columnId = 0;

        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.TABLE_MQY_IDX);
        objTranMQYRec.setRecordId( cur.getInt(columnId) );

        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.DATE_COUNT );
        objTranMQYRec.setDateCount( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.LAST);
        objTranMQYRec.setLast( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.OPEN);
        objTranMQYRec.setOpen( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.CLOSE);
        objTranMQYRec.setClose( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.HIGHEST);
        objTranMQYRec.setHighest( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.LOWEST);
        objTranMQYRec.setLowest( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.TOTAL_PRICE_COUNT);
        objTranMQYRec.setTotalPriceCount( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.TOTAL_TRAN_COUNT);
        objTranMQYRec.setTotalTranCount( cur.getInt(columnId) );
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.TOTAL_VOL);
        objTranMQYRec.setTotalVol( cur.getLong(columnId) );
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.TOTAL_MONEY);
        objTranMQYRec.setTotalMoney( cur.getLong(columnId) );
        columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.SINGLE_SUM_VOL);
        objTranMQYRec.setSingleSumVol( cur.getLong(columnId) );

        if (isFullySet) {

            columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.START_DATE);
            objTranMQYRec.setStartDate( cur.getInt(columnId) );
            columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.END_DATE);
            objTranMQYRec.setEndDate( cur.getInt(columnId) );

            columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.SINGLE_REC_STAT);
            objTranMQYRec.setSingleRecStat( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.EACH_DAY_BASIC);
            objTranMQYRec.setEachDayBasic( cur.getString(columnId) );
            columnId = cur.getColumnIndex(SchemaTranMQYRec.Column.PRICE_STAT);
            objTranMQYRec.setPriceStat( cur.getString(columnId) );

        }

    }

    public static void setObjTranDailyRec(String[] dailyRecordItemArray, ObjTranDailyRec objTranDailyRec) {

        objTranDailyRec.setRecordId( Integer.parseInt(dailyRecordItemArray[0]) );
        objTranDailyRec.setWeekday( Integer.parseInt(dailyRecordItemArray[1]) );
        objTranDailyRec.setLast( Integer.parseInt(dailyRecordItemArray[2]) );
        objTranDailyRec.setOpen( Integer.parseInt(dailyRecordItemArray[3]) );
        objTranDailyRec.setClose( Integer.parseInt(dailyRecordItemArray[4]) );
        objTranDailyRec.setHighest( Integer.parseInt(dailyRecordItemArray[5]) );
        objTranDailyRec.setLowest( Integer.parseInt(dailyRecordItemArray[6]) );

        objTranDailyRec.setTotalPriceCount( Integer.parseInt(dailyRecordItemArray[7]) );
        objTranDailyRec.setTotalTranCount( Integer.parseInt(dailyRecordItemArray[8]) );
        objTranDailyRec.setTotalVol( Long.parseLong(dailyRecordItemArray[9]) );
        objTranDailyRec.setTotalMoney( Long.parseLong(dailyRecordItemArray[10]) );

        objTranDailyRec.setPriceTimeGap( Integer.parseInt(dailyRecordItemArray[11]) );
        objTranDailyRec.setSingleSumVol( Long.parseLong(dailyRecordItemArray[12]) );

        String[] thirtyFourMaxSingleVolRecs = dailyRecordItemArray[13].split("_");
        for (int i=0; i<34; i++) {
            objTranDailyRec.pushSingleRecQueue(thirtyFourMaxSingleVolRecs[i]);
        }

        String[] fifteenMinuteRecs = dailyRecordItemArray[14].split("_");
        for (int i=0; i<16; i++) {
            objTranDailyRec.pushFifteenMinuteRecQueue(fifteenMinuteRecs[i]);
        }

        objTranDailyRec.setPriceStat( dailyRecordItemArray[15]) ;

    }

    public static void setObjTranMQYRec(String[] mqyRecordItemArray, ObjTranWMQYRec objTranMQYRec) {

        objTranMQYRec.setRecordId( Integer.parseInt(mqyRecordItemArray[0]) );
        objTranMQYRec.setStartDate( Integer.parseInt(mqyRecordItemArray[1]) );
        objTranMQYRec.setEndDate( Integer.parseInt(mqyRecordItemArray[2]) );
        objTranMQYRec.setDateCount( Integer.parseInt(mqyRecordItemArray[3]) );
        objTranMQYRec.setLast( Integer.parseInt(mqyRecordItemArray[4]) );
        objTranMQYRec.setOpen( Integer.parseInt(mqyRecordItemArray[5]) );
        objTranMQYRec.setClose( Integer.parseInt(mqyRecordItemArray[6]) );
        objTranMQYRec.setHighest( Integer.parseInt(mqyRecordItemArray[7]) );
        objTranMQYRec.setLowest( Integer.parseInt(mqyRecordItemArray[8]) );
        objTranMQYRec.setTotalPriceCount( Integer.parseInt(mqyRecordItemArray[9]) );
        objTranMQYRec.setTotalTranCount( Integer.parseInt(mqyRecordItemArray[10]) );
        objTranMQYRec.setTotalVol( Long.parseLong(mqyRecordItemArray[11]) );
        objTranMQYRec.setTotalMoney( Long.parseLong(mqyRecordItemArray[12]) );
        objTranMQYRec.setSingleSumVol( Long.parseLong(mqyRecordItemArray[13]) );
        objTranMQYRec.setSingleRecStat( mqyRecordItemArray[14] );
        objTranMQYRec.setEachDayBasic( mqyRecordItemArray[15] );
        objTranMQYRec.setPriceStat( mqyRecordItemArray[16] );

    }

    private static int comparePrice(int price1, int price2) {

        if ( price1 < price2 ) {
            return UtilViewTool.COLOR_GREEN;
        } else {
            return price1 > price2 ? UtilViewTool.COLOR_RED : UtilViewTool.COLOR_WHITE;
        }

    }

}
