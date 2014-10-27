package com.msolo.stockeye.service.db.build;

import java.util.HashMap;

/**
 * Created by mSolo on 14-6-23.
 */

public class SchemaTranDailyRec extends SchemaTranRec {

    public static final String DATABASE_NAME = "daily_tran_rec.db";
    public static final int DATABASE_VERSION = 1;

    public SchemaTranDailyRec() {}

    @Override
    protected String getCreationTableIndexTemplateSqlStatement() {

        return "CREATE UNIQUE INDEX IF NOT EXISTS "
               + DATABASE_TEMPLATE_TABLE_NAME
               + "_date_idx on "
               + DATABASE_TEMPLATE_TABLE_NAME
               + "("
               + Column.DATE
               + ");";

    }

    @Override
    protected String getCreationTableTemplateSqlStatement() {

        return  "CREATE TABLE IF NOT EXISTS "
                + DATABASE_TEMPLATE_TABLE_NAME + " ("
                + Column.ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Column.DATE + " INTEGER NOT NULL UNIQUE, "
                + Column.WEEKDAY + DATATYPE_INT
                + Column.LAST + DATATYPE_INT
                + Column.OPEN + DATATYPE_INT
                + Column.CLOSE + DATATYPE_INT
                + Column.HIGHEST + DATATYPE_INT
                + Column.LOWEST + DATATYPE_INT

                + Column.TOTAL_PRICE_COUNT + DATATYPE_INT
                + Column.TOTAL_TRAN_COUNT + DATATYPE_INT
                + Column.TOTAL_VOL + DATATYPE_LONG
                + Column.TOTAL_MONEY + DATATYPE_LONG

                + Column.PRICE_TIME_GAP + DATATYPE_INT
                + Column.SINGLE_SUM_VOL + DATATYPE_LONG

                + Column.SINGLE_01 + DATATYPE_TEXT
                + Column.SINGLE_02 + DATATYPE_TEXT
                + Column.SINGLE_03 + DATATYPE_TEXT
                + Column.SINGLE_04 + DATATYPE_TEXT
                + Column.SINGLE_05 + DATATYPE_TEXT
                + Column.SINGLE_06 + DATATYPE_TEXT
                + Column.SINGLE_07 + DATATYPE_TEXT
                + Column.SINGLE_08 + DATATYPE_TEXT
                + Column.SINGLE_09 + DATATYPE_TEXT
                + Column.SINGLE_10 + DATATYPE_TEXT
                + Column.SINGLE_11 + DATATYPE_TEXT
                + Column.SINGLE_12 + DATATYPE_TEXT
                + Column.SINGLE_13 + DATATYPE_TEXT
                + Column.SINGLE_14 + DATATYPE_TEXT
                + Column.SINGLE_15 + DATATYPE_TEXT
                + Column.SINGLE_16 + DATATYPE_TEXT
                + Column.SINGLE_17 + DATATYPE_TEXT
                + Column.SINGLE_18 + DATATYPE_TEXT
                + Column.SINGLE_19 + DATATYPE_TEXT
                + Column.SINGLE_20 + DATATYPE_TEXT
                + Column.SINGLE_21 + DATATYPE_TEXT
                + Column.SINGLE_22 + DATATYPE_TEXT
                + Column.SINGLE_23 + DATATYPE_TEXT
                + Column.SINGLE_24 + DATATYPE_TEXT
                + Column.SINGLE_25 + DATATYPE_TEXT
                + Column.SINGLE_26 + DATATYPE_TEXT
                + Column.SINGLE_27 + DATATYPE_TEXT
                + Column.SINGLE_28 + DATATYPE_TEXT
                + Column.SINGLE_29 + DATATYPE_TEXT
                + Column.SINGLE_30 + DATATYPE_TEXT
                + Column.SINGLE_31 + DATATYPE_TEXT
                + Column.SINGLE_32 + DATATYPE_TEXT
                + Column.SINGLE_33 + DATATYPE_TEXT
                + Column.SINGLE_34 + DATATYPE_TEXT

                + Column.FIFTEEN_MINUTE_01 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_02 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_03 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_04 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_05 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_06 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_07 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_08 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_09 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_10 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_11 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_12 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_13 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_14 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_15 + DATATYPE_TEXT
                + Column.FIFTEEN_MINUTE_16 + DATATYPE_TEXT

                + Column.PRICE_STAT + DATATYPE_TEXT_CREATE_TABLE_END;
    }

    @Override
    protected String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Override
    protected int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    @Override
    protected String getTemplateInsertSqlStatement() {

        return  "INSERT INTO " + DATABASE_TEMPLATE_TABLE_NAME + "("
                + Column.DATE + ","
                + Column.WEEKDAY + ","
                + Column.LAST + ","
                + Column.OPEN + ","
                + Column.CLOSE + ","
                + Column.HIGHEST + ","
                + Column.LOWEST + ","

                + Column.TOTAL_PRICE_COUNT + ","
                + Column.TOTAL_TRAN_COUNT + ","
                + Column.TOTAL_VOL + ","
                + Column.TOTAL_MONEY + ","

                + Column.PRICE_TIME_GAP + ","
                + Column.SINGLE_SUM_VOL + ","

                + Column.SINGLE_01 + ","
                + Column.SINGLE_02 + ","
                + Column.SINGLE_03 + ","
                + Column.SINGLE_04 + ","
                + Column.SINGLE_05 + ","
                + Column.SINGLE_06 + ","
                + Column.SINGLE_07 + ","
                + Column.SINGLE_08 + ","
                + Column.SINGLE_09 + ","
                + Column.SINGLE_10 + ","
                + Column.SINGLE_11 + ","
                + Column.SINGLE_12 + ","
                + Column.SINGLE_13 + ","
                + Column.SINGLE_14 + ","
                + Column.SINGLE_15 + ","
                + Column.SINGLE_16 + ","
                + Column.SINGLE_17 + ","
                + Column.SINGLE_18 + ","
                + Column.SINGLE_19 + ","
                + Column.SINGLE_20 + ","
                + Column.SINGLE_21 + ","
                + Column.SINGLE_22 + ","
                + Column.SINGLE_23 + ","
                + Column.SINGLE_24 + ","
                + Column.SINGLE_25 + ","
                + Column.SINGLE_26 + ","
                + Column.SINGLE_27 + ","
                + Column.SINGLE_28 + ","
                + Column.SINGLE_29 + ","
                + Column.SINGLE_30 + ","
                + Column.SINGLE_31 + ","
                + Column.SINGLE_32 + ","
                + Column.SINGLE_33 + ","
                + Column.SINGLE_34 + ","

                + Column.FIFTEEN_MINUTE_01 + ","
                + Column.FIFTEEN_MINUTE_02 + ","
                + Column.FIFTEEN_MINUTE_03 + ","
                + Column.FIFTEEN_MINUTE_04 + ","
                + Column.FIFTEEN_MINUTE_05 + ","
                + Column.FIFTEEN_MINUTE_06 + ","
                + Column.FIFTEEN_MINUTE_07 + ","
                + Column.FIFTEEN_MINUTE_08 + ","
                + Column.FIFTEEN_MINUTE_09 + ","
                + Column.FIFTEEN_MINUTE_10 + ","
                + Column.FIFTEEN_MINUTE_11 + ","
                + Column.FIFTEEN_MINUTE_12 + ","
                + Column.FIFTEEN_MINUTE_13 + ","
                + Column.FIFTEEN_MINUTE_14 + ","
                + Column.FIFTEEN_MINUTE_15 + ","
                + Column.FIFTEEN_MINUTE_16 + ","

                + Column.PRICE_STAT

                + ") " + "VALUES("
                + "?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?);";

    }

    protected static HashMap<String, String> getStockTranRecProjectionMap() {

        HashMap<String, String> stockTranRecProjectionMap = new HashMap<String, String>();

        stockTranRecProjectionMap.put(Column.DATE, Column.DATE);

        stockTranRecProjectionMap.put(Column.WEEKDAY, Column.WEEKDAY);
        stockTranRecProjectionMap.put(Column.LAST, Column.LAST);
        stockTranRecProjectionMap.put(Column.OPEN, Column.OPEN);
        stockTranRecProjectionMap.put(Column.CLOSE, Column.CLOSE);
        stockTranRecProjectionMap.put(Column.HIGHEST, Column.HIGHEST);
        stockTranRecProjectionMap.put(Column.LOWEST, Column.LOWEST);

        stockTranRecProjectionMap.put(Column.TOTAL_PRICE_COUNT, Column.TOTAL_PRICE_COUNT);
        stockTranRecProjectionMap.put(Column.TOTAL_TRAN_COUNT, Column.TOTAL_TRAN_COUNT);
        stockTranRecProjectionMap.put(Column.TOTAL_VOL, Column.TOTAL_VOL);
        stockTranRecProjectionMap.put(Column.TOTAL_MONEY, Column.TOTAL_MONEY);

        stockTranRecProjectionMap.put(Column.PRICE_TIME_GAP, Column.PRICE_TIME_GAP);

        stockTranRecProjectionMap.put(Column.SINGLE_SUM_VOL, Column.SINGLE_SUM_VOL);
        stockTranRecProjectionMap.put(Column.SINGLE_01, Column.SINGLE_01);
        stockTranRecProjectionMap.put(Column.SINGLE_02, Column.SINGLE_02);
        stockTranRecProjectionMap.put(Column.SINGLE_03, Column.SINGLE_03);
        stockTranRecProjectionMap.put(Column.SINGLE_04, Column.SINGLE_04);
        stockTranRecProjectionMap.put(Column.SINGLE_05, Column.SINGLE_05);
        stockTranRecProjectionMap.put(Column.SINGLE_06, Column.SINGLE_06);
        stockTranRecProjectionMap.put(Column.SINGLE_07, Column.SINGLE_07);
        stockTranRecProjectionMap.put(Column.SINGLE_08, Column.SINGLE_08);
        stockTranRecProjectionMap.put(Column.SINGLE_09, Column.SINGLE_09);
        stockTranRecProjectionMap.put(Column.SINGLE_10, Column.SINGLE_10);
        stockTranRecProjectionMap.put(Column.SINGLE_11, Column.SINGLE_11);
        stockTranRecProjectionMap.put(Column.SINGLE_12, Column.SINGLE_12);
        stockTranRecProjectionMap.put(Column.SINGLE_13, Column.SINGLE_13);
        stockTranRecProjectionMap.put(Column.SINGLE_14, Column.SINGLE_14);
        stockTranRecProjectionMap.put(Column.SINGLE_15, Column.SINGLE_15);
        stockTranRecProjectionMap.put(Column.SINGLE_16, Column.SINGLE_16);
        stockTranRecProjectionMap.put(Column.SINGLE_17, Column.SINGLE_17);
        stockTranRecProjectionMap.put(Column.SINGLE_18, Column.SINGLE_18);
        stockTranRecProjectionMap.put(Column.SINGLE_19, Column.SINGLE_19);
        stockTranRecProjectionMap.put(Column.SINGLE_20, Column.SINGLE_20);
        stockTranRecProjectionMap.put(Column.SINGLE_21, Column.SINGLE_21);
        stockTranRecProjectionMap.put(Column.SINGLE_22, Column.SINGLE_22);
        stockTranRecProjectionMap.put(Column.SINGLE_23, Column.SINGLE_23);
        stockTranRecProjectionMap.put(Column.SINGLE_24, Column.SINGLE_24);
        stockTranRecProjectionMap.put(Column.SINGLE_25, Column.SINGLE_25);
        stockTranRecProjectionMap.put(Column.SINGLE_26, Column.SINGLE_26);
        stockTranRecProjectionMap.put(Column.SINGLE_27, Column.SINGLE_27);
        stockTranRecProjectionMap.put(Column.SINGLE_28, Column.SINGLE_28);
        stockTranRecProjectionMap.put(Column.SINGLE_29, Column.SINGLE_29);
        stockTranRecProjectionMap.put(Column.SINGLE_30, Column.SINGLE_30);
        stockTranRecProjectionMap.put(Column.SINGLE_31, Column.SINGLE_31);
        stockTranRecProjectionMap.put(Column.SINGLE_32, Column.SINGLE_32);
        stockTranRecProjectionMap.put(Column.SINGLE_33, Column.SINGLE_33);
        stockTranRecProjectionMap.put(Column.SINGLE_34, Column.SINGLE_34);

        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_01, Column.FIFTEEN_MINUTE_01);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_02, Column.FIFTEEN_MINUTE_02);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_03, Column.FIFTEEN_MINUTE_03);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_04, Column.FIFTEEN_MINUTE_04);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_05, Column.FIFTEEN_MINUTE_05);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_06, Column.FIFTEEN_MINUTE_06);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_07, Column.FIFTEEN_MINUTE_07);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_08, Column.FIFTEEN_MINUTE_08);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_09, Column.FIFTEEN_MINUTE_09);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_10, Column.FIFTEEN_MINUTE_10);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_11, Column.FIFTEEN_MINUTE_11);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_12, Column.FIFTEEN_MINUTE_12);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_13, Column.FIFTEEN_MINUTE_13);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_14, Column.FIFTEEN_MINUTE_14);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_15, Column.FIFTEEN_MINUTE_15);
        stockTranRecProjectionMap.put(Column.FIFTEEN_MINUTE_16, Column.FIFTEEN_MINUTE_16);

        stockTranRecProjectionMap.put(Column.PRICE_STAT, Column.PRICE_STAT);

        return stockTranRecProjectionMap;

    }

    // Database Columns -- START
    class Column {
        
        public static final String ROWID = "row_id";
        public static final String DATE = "date";
        public static final String WEEKDAY = "weekday";
        public static final String LAST = "last_price";
        public static final String OPEN = "open";
        public static final String CLOSE = "close";
        public static final String HIGHEST = "highest";
        public static final String LOWEST = "lowest";

        public static final String TOTAL_VOL = "total_vol";
        public static final String TOTAL_MONEY = "total_money";
        public static final String TOTAL_TRAN_COUNT = "total_tran_count";
        public static final String TOTAL_PRICE_COUNT = "total_price_count";

        public static final String PRICE_TIME_GAP = "price_time_gap";
        public static final String SINGLE_SUM_VOL = "single_sum_vol";

        public static final String SINGLE_01 = "single_max_vol_01";
        public static final String SINGLE_02 = "single_max_vol_02";
        public static final String SINGLE_03 = "single_max_vol_03";
        public static final String SINGLE_04 = "single_max_vol_04";
        public static final String SINGLE_05 = "single_max_vol_05";
        public static final String SINGLE_06 = "single_max_vol_06";
        public static final String SINGLE_07 = "single_max_vol_07";
        public static final String SINGLE_08 = "single_max_vol_08";
        public static final String SINGLE_09 = "single_max_vol_09";
        public static final String SINGLE_10 = "single_max_vol_10";
        public static final String SINGLE_11 = "single_max_vol_11";
        public static final String SINGLE_12 = "single_max_vol_12";
        public static final String SINGLE_13 = "single_max_vol_13";
        public static final String SINGLE_14 = "single_max_vol_14";
        public static final String SINGLE_15 = "single_max_vol_15";
        public static final String SINGLE_16 = "single_max_vol_16";
        public static final String SINGLE_17 = "single_max_vol_17";
        public static final String SINGLE_18 = "single_max_vol_18";
        public static final String SINGLE_19 = "single_max_vol_19";
        public static final String SINGLE_20 = "single_max_vol_20";
        public static final String SINGLE_21 = "single_max_vol_21";
        public static final String SINGLE_22 = "single_max_vol_22";
        public static final String SINGLE_23 = "single_max_vol_23";
        public static final String SINGLE_24 = "single_max_vol_24";
        public static final String SINGLE_25 = "single_max_vol_25";
        public static final String SINGLE_26 = "single_max_vol_26";
        public static final String SINGLE_27 = "single_max_vol_27";
        public static final String SINGLE_28 = "single_max_vol_28";
        public static final String SINGLE_29 = "single_max_vol_29";
        public static final String SINGLE_30 = "single_max_vol_30";
        public static final String SINGLE_31 = "single_max_vol_31";
        public static final String SINGLE_32 = "single_max_vol_32";
        public static final String SINGLE_33 = "single_max_vol_33";
        public static final String SINGLE_34 = "single_max_vol_34";

        public static final String FIFTEEN_MINUTE_01 = "fifteen_minute_01";
        public static final String FIFTEEN_MINUTE_02 = "fifteen_minute_02";
        public static final String FIFTEEN_MINUTE_03 = "fifteen_minute_03";
        public static final String FIFTEEN_MINUTE_04 = "fifteen_minute_04";
        public static final String FIFTEEN_MINUTE_05 = "fifteen_minute_05";
        public static final String FIFTEEN_MINUTE_06 = "fifteen_minute_06";
        public static final String FIFTEEN_MINUTE_07 = "fifteen_minute_07";
        public static final String FIFTEEN_MINUTE_08 = "fifteen_minute_08";
        public static final String FIFTEEN_MINUTE_09 = "fifteen_minute_09";
        public static final String FIFTEEN_MINUTE_10 = "fifteen_minute_10";
        public static final String FIFTEEN_MINUTE_11 = "fifteen_minute_11";
        public static final String FIFTEEN_MINUTE_12 = "fifteen_minute_12";
        public static final String FIFTEEN_MINUTE_13 = "fifteen_minute_13";
        public static final String FIFTEEN_MINUTE_14 = "fifteen_minute_14";
        public static final String FIFTEEN_MINUTE_15 = "fifteen_minute_15";
        public static final String FIFTEEN_MINUTE_16 = "fifteen_minute_16";

        public static final String PRICE_STAT = "price_stat";
    }
    // Database Columns -- END

}
