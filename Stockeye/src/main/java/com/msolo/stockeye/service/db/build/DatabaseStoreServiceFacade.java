package com.msolo.stockeye.service.db.build;

import com.msolo.stockeye.gui.activity.quotation.StockQuoteMediator;

import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/18.
 */
public class DatabaseStoreServiceFacade implements DatabaseStoreServiceFacadeImp {

    private int dailyCapacity = 0;

    private ArrayList<ObjTranDailyRec> objTranDailyRecArrayList = new ArrayList<ObjTranDailyRec>();
    private ArrayList<ObjTranWMQYRec> objTranMQYRecArrayList = new ArrayList<ObjTranWMQYRec>();

    public DatabaseStoreServiceFacade() {}

    @Override
    public void cacheOneStockDailyRecord(String[] dailyRecordItemArray) {

        ObjTranDailyRec objTranDailyRec = new ObjTranDailyRec();

        DatabaseObjConfigure.setObjTranDailyRec(dailyRecordItemArray, objTranDailyRec);

        objTranDailyRecArrayList.add(objTranDailyRec);

    }

    @Override
    public void cacheOneStockMQYRecord(String[] monthlyRecordItemArray) {

        ObjTranWMQYRec objTranMQYRec = new ObjTranWMQYRec();

        DatabaseObjConfigure.setObjTranMQYRec(monthlyRecordItemArray, objTranMQYRec);

        objTranMQYRecArrayList.add(objTranMQYRec);

    }

    @Override
    public void prepareStockTranRecordCycleCache(int dailyCapacity, int mqyCapacity) {

        this.dailyCapacity = dailyCapacity;

        objTranDailyRecArrayList.ensureCapacity(dailyCapacity);
        objTranMQYRecArrayList.ensureCapacity(mqyCapacity);
    }

    @Override
    public void submitCachesOfStockDailyRecord(String stockcode) {

        SchemaHelperTranDailyRec schemaHelperTranDailyRec = new SchemaHelperTranDailyRec();
        schemaHelperTranDailyRec.setTableName(stockcode);
        schemaHelperTranDailyRec.addTranRecByBatch(objTranDailyRecArrayList);

        String updatedDate = String.valueOf( objTranDailyRecArrayList.get(objTranDailyRecArrayList.size() - 1).getRecordId() );
        StockQuoteMediator.getInstance().updateStockCurrentUpdatedDate(stockcode, updatedDate);

        objTranDailyRecArrayList.clear();
        objTranDailyRecArrayList.ensureCapacity(dailyCapacity);

    }

    @Override
    public void submitStockMQYRecords(String stockcode) {

        SchemaHelperTranMQYRec schemaHelperTranMQYRec = new SchemaHelperTranMQYRec();
        schemaHelperTranMQYRec.setTableName(stockcode);
        schemaHelperTranMQYRec.addTranMQYRecByBatch(objTranMQYRecArrayList);
        objTranMQYRecArrayList.clear();

    }

    @Override
    public void dropDbTable(String stockcode) {

        SchemaHelperTranDailyRec schemaHelperTranDailyRec = new SchemaHelperTranDailyRec();
        schemaHelperTranDailyRec.setTableName(stockcode);
        schemaHelperTranDailyRec.dropDbTable();

        SchemaHelperTranMQYRec schemaHelperTranMQYRec = new SchemaHelperTranMQYRec();
        schemaHelperTranMQYRec.setTableName(stockcode);
        schemaHelperTranMQYRec.dropDbTable();

    }

}
