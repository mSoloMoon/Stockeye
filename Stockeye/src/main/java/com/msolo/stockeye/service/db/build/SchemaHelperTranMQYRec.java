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
 * Created by mSolo on 2014/8/17.
 */
public class SchemaHelperTranMQYRec extends SQLiteOpenHelper {

    private String tableName = null;
    private SchemaTranMQYRec schemaTranMQYRec = null;
    private String templateTableName = null;

    protected SchemaHelperTranMQYRec() {

        super(StockeyeApp.appContext, SchemaTranMQYRec.DATABASE_NAME, null, SchemaTranMQYRec.DATABASE_VERSION);
        schemaTranMQYRec = new SchemaTranMQYRec();
        templateTableName = schemaTranMQYRec.getDatabaseTemplateTableName();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {

        File dbFile = new File( getDatabaseFilePath() + "/" + schemaTranMQYRec.getDatabaseName() );

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

    public void addTranMQYRec(ObjTranWMQYRec objTranMQYRec) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        buildTranRecContentValues(objTranMQYRec, cv);

        db.insertWithOnConflict(tableName, "", cv, SQLiteDatabase.CONFLICT_IGNORE);

        db.close();

    }

    public boolean addTranMQYRecByBatch(ArrayList<ObjTranWMQYRec> objTranMQYRecArrayList) {

        SQLiteDatabase db = null;
        try {

            db = getWritableDatabase();

            String templateInsertSqlStatement = schemaTranMQYRec.getTemplateInsertSqlStatement();

            String insertSqlStatement = templateInsertSqlStatement.replace(templateTableName, tableName);

            db.beginTransaction();

            for (ObjTranWMQYRec obj : objTranMQYRecArrayList) {
                db.execSQL(insertSqlStatement, obj.toStringArray());
            }
            db.setTransactionSuccessful();

            db.endTransaction();

        } finally {
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
        db.execSQL(schemaTranMQYRec.getCreationTableTemplateSqlStatement()
                        .replace(templateTableName, tableName)
        );
        db.close();
        //

    }

    public void buildTranRecContentValues(ObjTranWMQYRec obj, ContentValues cv) {

        cv.put( SchemaTranMQYRec.Column.TABLE_MQY_IDX, obj.getRecordId() );
        cv.put( SchemaTranMQYRec.Column.START_DATE, obj.getStartDate() );
        cv.put( SchemaTranMQYRec.Column.END_DATE, obj.getEndDate() );
        cv.put( SchemaTranMQYRec.Column.DATE_COUNT, obj.getDateCount() );
        cv.put( SchemaTranMQYRec.Column.LAST, obj.getLast() );
        cv.put( SchemaTranMQYRec.Column.OPEN, obj.getOpen() );
        cv.put( SchemaTranMQYRec.Column.CLOSE, obj.getClose() );
        cv.put( SchemaTranMQYRec.Column.HIGHEST, obj.getHighest() );
        cv.put( SchemaTranMQYRec.Column.LOWEST, obj.getLowest() );

        cv.put( SchemaTranMQYRec.Column.TOTAL_VOL, obj.getTotalVol() );
        cv.put( SchemaTranMQYRec.Column.TOTAL_MONEY, obj.getTotalMoney() );
        cv.put( SchemaTranMQYRec.Column.TOTAL_TRAN_COUNT, obj.getTotalTranCount() );
        cv.put( SchemaTranMQYRec.Column.TOTAL_PRICE_COUNT, obj.getTotalPriceCount() );

        cv.put( SchemaTranMQYRec.Column.SINGLE_SUM_VOL, obj.getSingleSumVol() );
        cv.put( SchemaTranMQYRec.Column.SINGLE_REC_STAT, obj.getSingleRecStat() );
        cv.put( SchemaTranMQYRec.Column.EACH_DAY_BASIC, obj.getEachDayBasic() );

        cv.put( SchemaTranMQYRec.Column.PRICE_STAT, obj.getPriceStat() );

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
