package com.msolo.stockeye.service.db.build;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.msolo.stockeye.StockeyeApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/16.
 */
public class SchemaHelperTranDailyRec extends SQLiteOpenHelper {

    private String tableName = null;
    private SchemaTranDailyRec schemaTranDailyRec = null;
    private String templateTableName = null;

    protected SchemaHelperTranDailyRec() {

        super(StockeyeApp.appContext, SchemaTranDailyRec.DATABASE_NAME, null, SchemaTranDailyRec.DATABASE_VERSION);
        schemaTranDailyRec = new SchemaTranDailyRec();
        templateTableName = schemaTranDailyRec.getDatabaseTemplateTableName();

    }

    @Override
    public SQLiteDatabase getWritableDatabase() {

        File dbFile = new File( getDatabaseFilePath() + "/" + schemaTranDailyRec.getDatabaseName() );

        if (!dbFile.exists()) {

            try {
                dbFile.createNewFile();
            } catch (IOException ioe) {
                return null;
            }

        }

        return SQLiteDatabase.openOrCreateDatabase(dbFile, null);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // stub
    }

    protected void addTranRec(ObjTranDailyRec objTranDailyRec) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        buildTranRecContentValues(objTranDailyRec, cv);

        db.insertWithOnConflict(tableName, "", cv, SQLiteDatabase.CONFLICT_IGNORE);

        db.close();

    }

    protected boolean addTranRecByBatch(ArrayList<ObjTranDailyRec> objTranDailyRecArrayList) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {

            String templateInsertSqlStatement = schemaTranDailyRec.getTemplateInsertSqlStatement();

            String insertSqlStatement = templateInsertSqlStatement.replace(templateTableName, tableName);

            for (ObjTranDailyRec obj : objTranDailyRecArrayList) {
                db.execSQL(insertSqlStatement, obj.toStringArray());
            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
            if (db != null) { db.close(); }
        }

        return true;

    }

    protected void dropDbTable() {

        SQLiteDatabase db = getWritableDatabase();

        db.delete(tableName, null, null);

    }

    public void setTableName(String stockcode) {

        tableName = stockcode;

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(schemaTranDailyRec.getCreationTableTemplateSqlStatement()
                        .replace(templateTableName, tableName)
        );

        db.execSQL(schemaTranDailyRec.getCreationTableIndexTemplateSqlStatement()
                        .replace(templateTableName, tableName)
        );

        db.close();

    }

    private void buildTranRecContentValues(ObjTranDailyRec tranDailyRecObj, ContentValues cv) {

        cv.put( SchemaTranDailyRec.Column.DATE, tranDailyRecObj.getRecordId() );
        cv.put( SchemaTranDailyRec.Column.WEEKDAY, tranDailyRecObj.getWeekday() );
        cv.put( SchemaTranDailyRec.Column.LAST, tranDailyRecObj.getLast() );
        cv.put( SchemaTranDailyRec.Column.OPEN, tranDailyRecObj.getOpen() );
        cv.put( SchemaTranDailyRec.Column.CLOSE, tranDailyRecObj.getClose() );
        cv.put( SchemaTranDailyRec.Column.HIGHEST, tranDailyRecObj.getHighest() );
        cv.put( SchemaTranDailyRec.Column.LOWEST, tranDailyRecObj.getLowest() );

        cv.put( SchemaTranDailyRec.Column.TOTAL_VOL, tranDailyRecObj.getTotalVol() );
        cv.put( SchemaTranDailyRec.Column.TOTAL_MONEY, tranDailyRecObj.getTotalMoney() );
        cv.put( SchemaTranDailyRec.Column.TOTAL_TRAN_COUNT, tranDailyRecObj.getTotalTranCount() );
        cv.put( SchemaTranDailyRec.Column.TOTAL_PRICE_COUNT, tranDailyRecObj.getTotalPriceCount() );

        cv.put( SchemaTranDailyRec.Column.PRICE_TIME_GAP, tranDailyRecObj.getPriceTimeGap() );

        cv.put( SchemaTranDailyRec.Column.SINGLE_SUM_VOL, tranDailyRecObj.getSingleSumVol() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_01, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_02, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_03, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_04, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_05, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_06, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_07, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_08, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_09, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_10, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_11, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_12, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_13, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_14, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_15, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_16, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_17, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_18, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_19, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_20, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_21, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_22, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_23, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_24, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_25, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_26, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_27, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_28, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_29, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_30, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_31, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_32, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_33, tranDailyRecObj.getSingleRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.SINGLE_34, tranDailyRecObj.getSingleRecQueue().poll() );

        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_01, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_02, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_03, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_04, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_05, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_06, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_07, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_08, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_09, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_10, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_11, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_12, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_13, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_14, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_15, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );
        cv.put( SchemaTranDailyRec.Column.FIFTEEN_MINUTE_16, tranDailyRecObj.getFifteenMinuteRecQueue().poll() );

        cv.put( SchemaTranDailyRec.Column.PRICE_STAT, tranDailyRecObj.getPriceStat() );

    }

    private String getDatabaseFilePath() {

        String dbPath = Environment.getExternalStorageDirectory().getParentFile().getAbsolutePath() + "/sdcard1/stockeye/database";

        File dbDir = new File(dbPath);
        if (!dbDir.exists() && !dbDir.mkdirs()) {

            dbPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/stockeye/database";
            dbDir = new File(dbPath);
            if (!dbDir.exists() && !dbDir.mkdirs()) {
                return null;
            }

        }

        return dbPath;

    }

}
