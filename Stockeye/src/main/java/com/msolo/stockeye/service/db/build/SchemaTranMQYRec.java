package com.msolo.stockeye.service.db.build;

import java.util.HashMap;

/**
 * Created by mSolo on 2014/8/16.
 */
public class SchemaTranMQYRec extends SchemaTranRec {

    public static final String DATABASE_NAME = "mqy_tran_rec.db";
    public static final int DATABASE_VERSION = 1;

    @Override
    protected String getCreationTableTemplateSqlStatement() {

        return "CREATE TABLE IF NOT EXISTS "
                + DATABASE_TEMPLATE_TABLE_NAME + " ("
                + Column.TABLE_MQY_IDX + " INTEGER PRIMARY KEY, "
                + Column.START_DATE + " INTEGER NOT NULL, "
                + Column.END_DATE + " INTEGER NOT NULL, "
                + Column.DATE_COUNT + DATATYPE_INT
                + Column.LAST + DATATYPE_INT
                + Column.OPEN + DATATYPE_INT
                + Column.CLOSE + DATATYPE_INT
                + Column.HIGHEST + DATATYPE_INT
                + Column.LOWEST  + DATATYPE_INT

                + Column.TOTAL_PRICE_COUNT  + DATATYPE_INT
                + Column.TOTAL_TRAN_COUNT  + DATATYPE_INT
                + Column.TOTAL_VOL  + DATATYPE_LONG
                + Column.TOTAL_MONEY  + DATATYPE_LONG

                + Column.SINGLE_SUM_VOL  + DATATYPE_LONG
                + Column.SINGLE_REC_STAT  + DATATYPE_TEXT

                + Column.EACH_DAY_BASIC  + DATATYPE_TEXT
                + Column.PRICE_STAT  + DATATYPE_TEXT_CREATE_TABLE_END;

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

        return  "insert into " + DATABASE_TEMPLATE_TABLE_NAME + "("
                + Column.TABLE_MQY_IDX + ","
                + Column.START_DATE + ","
                + Column.END_DATE + ","
                + Column.DATE_COUNT + ","
                + Column.LAST + ","
                + Column.OPEN + ","
                + Column.CLOSE + ","
                + Column.HIGHEST + ","
                + Column.LOWEST + ","
                + Column.TOTAL_PRICE_COUNT + ","
                + Column.TOTAL_TRAN_COUNT + ","
                + Column.TOTAL_VOL + ","
                + Column.TOTAL_MONEY + ","

                + Column.SINGLE_SUM_VOL + ","
                + Column.SINGLE_REC_STAT + ","

                + Column.EACH_DAY_BASIC + ","
                + Column.PRICE_STAT

                + ") " + "values("
                + "?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,"
                + "?);";

    }

    protected static HashMap<String, String> getStockTranRecProjectionMap() {

        HashMap<String, String> stockTranRecProjectionMap = new HashMap<String, String>();

        stockTranRecProjectionMap.put(Column.TABLE_MQY_IDX, Column.TABLE_MQY_IDX);
        stockTranRecProjectionMap.put(Column.START_DATE, Column.START_DATE);
        stockTranRecProjectionMap.put(Column.END_DATE, Column.END_DATE);
        stockTranRecProjectionMap.put(Column.DATE_COUNT, Column.DATE_COUNT);
        stockTranRecProjectionMap.put(Column.LAST, Column.LAST);
        stockTranRecProjectionMap.put(Column.OPEN, Column.OPEN);
        stockTranRecProjectionMap.put(Column.CLOSE, Column.CLOSE);
        stockTranRecProjectionMap.put(Column.HIGHEST, Column.HIGHEST);
        stockTranRecProjectionMap.put(Column.LOWEST, Column.LOWEST);
        stockTranRecProjectionMap.put(Column.TOTAL_PRICE_COUNT, Column.TOTAL_PRICE_COUNT);
        stockTranRecProjectionMap.put(Column.TOTAL_TRAN_COUNT, Column.TOTAL_TRAN_COUNT);
        stockTranRecProjectionMap.put(Column.TOTAL_VOL, Column.TOTAL_VOL);
        stockTranRecProjectionMap.put(Column.TOTAL_MONEY, Column.TOTAL_MONEY);

        stockTranRecProjectionMap.put(Column.SINGLE_SUM_VOL, Column.SINGLE_SUM_VOL);
        stockTranRecProjectionMap.put(Column.SINGLE_REC_STAT, Column.SINGLE_REC_STAT);

        stockTranRecProjectionMap.put(Column.EACH_DAY_BASIC, Column.EACH_DAY_BASIC);
        stockTranRecProjectionMap.put(Column.PRICE_STAT, Column.PRICE_STAT);

        return stockTranRecProjectionMap;

    }

    // Database Columns -- START
    public class Column {
        public static final String TABLE_MQY_IDX = "table_mqy_idx";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";
        public static final String DATE_COUNT = "date_count";
        public static final String LAST = "last_price";
        public static final String OPEN = "open";
        public static final String CLOSE = "close";
        public static final String HIGHEST = "highest";
        public static final String LOWEST = "lowest";

        public static final String TOTAL_VOL = "total_vol";
        public static final String TOTAL_MONEY = "total_money";
        public static final String TOTAL_TRAN_COUNT = "total_tran_count";
        public static final String TOTAL_PRICE_COUNT = "total_price_count";

        public static final String SINGLE_SUM_VOL = "single_sum_vol";
        public static final String SINGLE_REC_STAT = "single_rec_stat";

        public static final String EACH_DAY_BASIC = "each_day_basic";

        public static final String PRICE_STAT = "price_stat";
    }
    // Database Columns -- END

}
