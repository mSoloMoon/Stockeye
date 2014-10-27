package com.msolo.stockeye.service.db.build;

/**
 * Created by mSolo on 2014/8/16.
 */

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import android.net.Uri;

public class ContentProviderTranRec extends ContentProvider {

    // Content Provider Uri and Authority
    public static String AUTHORITY = "com.msolo.stockeye.db.TranRecContentProvider";

    protected static final String TRAN_REC_DIR_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.com.msolo.stockeye.db.provider.tranrec";

    protected static final String TRAN_REC_ITEM_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.com.msolo.stockeye.db.provider.tranrec";

    protected static final UriMatcher tranRecUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    
    private static final int TRAN_DAILY_REC_DIR = 1;
    private static final int TRAN_DAILY_REC_ITEM = 2;
    private static final int TRAN_DAILY_REC_ITEM_LIMIT = 3;
    private static final int TRAN_DAILY_REC_ITEM_RANGE = 4;

    private static final int TRAN_MQY_REC_DIR = 11;
    private static final int TRAN_MQY_REC_ITEM = 12;
    private static final int TRAN_MQY_REC_ITEM_LIMIT = 13;
    private static final int TRAN_MQY_REC_ITEM_RANGE = 14;

    static {
        tranRecUriMatcher.addURI(AUTHORITY, "records/daily", TRAN_DAILY_REC_DIR);
        tranRecUriMatcher.addURI(AUTHORITY, "records/daily/#", TRAN_DAILY_REC_ITEM);
        tranRecUriMatcher.addURI(AUTHORITY, "records/daily/limit/#", TRAN_DAILY_REC_ITEM_LIMIT);
        tranRecUriMatcher.addURI(AUTHORITY, "records/daily/range/#", TRAN_DAILY_REC_ITEM_RANGE);

        tranRecUriMatcher.addURI(AUTHORITY, "records/mqy", TRAN_MQY_REC_DIR);
        tranRecUriMatcher.addURI(AUTHORITY, "records/mqy/#", TRAN_MQY_REC_ITEM);
        tranRecUriMatcher.addURI(AUTHORITY, "records/mqy/limit/#", TRAN_MQY_REC_ITEM_LIMIT);
        tranRecUriMatcher.addURI(AUTHORITY, "records/mqy/range/#", TRAN_MQY_REC_ITEM_RANGE);
    }

    private SQLiteDatabase dailyDb = null;
    private SQLiteDatabase mqyDb = null;

    @Override
    public String getType(Uri uri) {

        switch (tranRecUriMatcher.match(uri)) {

            case TRAN_DAILY_REC_DIR:
            case TRAN_DAILY_REC_ITEM_LIMIT:
            case TRAN_DAILY_REC_ITEM_RANGE:
            case TRAN_MQY_REC_DIR:
            case TRAN_MQY_REC_ITEM_LIMIT:
            case TRAN_MQY_REC_ITEM_RANGE:
                return TRAN_REC_DIR_MIME_TYPE;

            case TRAN_DAILY_REC_ITEM:
            case TRAN_MQY_REC_ITEM:
                return TRAN_REC_ITEM_MIME_TYPE;

            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);

        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public boolean onCreate() {

        dailyDb = new SchemaHelperTranDailyRec().getWritableDatabase();
        mqyDb = new SchemaHelperTranMQYRec().getWritableDatabase();

        return true;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if (tranRecUriMatcher.match(uri) != TRAN_DAILY_REC_ITEM &&
            tranRecUriMatcher.match(uri) != TRAN_DAILY_REC_ITEM_RANGE &&
            tranRecUriMatcher.match(uri) != TRAN_DAILY_REC_ITEM_LIMIT &&
            tranRecUriMatcher.match(uri) != TRAN_MQY_REC_ITEM &&
            tranRecUriMatcher.match(uri) != TRAN_MQY_REC_ITEM_RANGE    ) {

            throw new IllegalArgumentException("Illegal uri: " + uri);

        }

        String conditionCmb = null;
        String[] conditionArgs = new String[2];
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables( selection );
        switch (tranRecUriMatcher.match(uri)) {

            // 0: records, 1: daily or mqy, 2: range, 3: key;
            case TRAN_DAILY_REC_ITEM_RANGE:

                conditionCmb = uri.getPathSegments().get(3);
                conditionArgs[0] = conditionCmb.substring(0, 8);
                conditionArgs[1] = conditionCmb.substring(8, 16);

                String condition = SchemaTranDailyRec.Column.DATE + " BETWEEN " + conditionArgs[0] + " AND " + conditionArgs[1];

                qb.setProjectionMap(SchemaTranDailyRec.getStockTranRecProjectionMap());

                return qb.query(dailyDb, projection, condition, selectionArgs, null, null, null);

            case TRAN_MQY_REC_ITEM_RANGE:

                conditionCmb = uri.getPathSegments().get(3);
                int len = conditionCmb.length();
                if ( len == 12) {
                    conditionArgs[0] = conditionCmb.substring(0, 6);
                    conditionArgs[1] = conditionCmb.substring(6, 12);
                } else if (len == 10) {
                    conditionArgs[0] = conditionCmb.substring(0, 5);
                    conditionArgs[1] = conditionCmb.substring(5, 10);
                } else {
                    conditionArgs[0] = conditionCmb.substring(0, 4);
                    conditionArgs[1] = conditionCmb.substring(4, 8);
                }

                condition = new StringBuilder().append( SchemaTranMQYRec.Column.TABLE_MQY_IDX )
                        .append(" BETWEEN ").append( conditionArgs[0] ).append(" AND ").append( conditionArgs[1] ).toString();

                qb.setProjectionMap(SchemaTranMQYRec.getStockTranRecProjectionMap());

                return qb.query(mqyDb, projection, condition, selectionArgs, null, null, null);

            case TRAN_DAILY_REC_ITEM_LIMIT:

                String rawSql = new StringBuilder().append("SELECT * FROM ").append(selection).append(" ORDER BY ").append( SchemaTranDailyRec.Column.DATE )
                        .append(" DESC LIMIT ").append( uri.getPathSegments().get(3) ).toString();

                return dailyDb.rawQuery(rawSql, null);

            case TRAN_MQY_REC_ITEM_LIMIT:

                // use TRAN_MQY_REC_ITEM_RANGE
                return null;

            case TRAN_DAILY_REC_ITEM:

                qb.setProjectionMap(SchemaTranDailyRec.getStockTranRecProjectionMap());

                qb.appendWhere(SchemaTranDailyRec.Column.DATE + "=" + uri.getPathSegments().get(2));

                return qb.query(dailyDb, projection, null, selectionArgs, null, null, null);

            case TRAN_MQY_REC_ITEM:

                qb.appendWhere(SchemaTranMQYRec.Column.TABLE_MQY_IDX + "=" + uri.getPathSegments().get(2));

                qb.setProjectionMap(SchemaTranMQYRec.getStockTranRecProjectionMap());

                return qb.query(mqyDb, projection, null, selectionArgs, null, null, null);

            default:
                return null;

        }

    }

}
