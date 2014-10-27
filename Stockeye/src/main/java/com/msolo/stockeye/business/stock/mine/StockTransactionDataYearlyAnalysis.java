package com.msolo.stockeye.business.stock.mine;

import com.msolo.stockeye.service.db.build.ObjTranWMQYRec;
import com.msolo.stockeye.service.db.access.DatabaseQueryServiceFacade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * apply : Abstract Factory Pattern - ConcreteProduct
 * apply : Strategy Pattern - ConcreteStrategy
 *
 * Created by mSolo on 2014/8/20.
 */
public class StockTransactionDataYearlyAnalysis extends StockTransactionDataAnalysis implements StockTransactionDataAnalysisImp<ObjTranWMQYRec> {

    protected StockTransactionDataYearlyAnalysis() {}

    @Override
    public String[] analyzeToTransactionRecordItemArray(String stockcode, String queryRecordId, ArrayList<ObjTranWMQYRec> addedObjTranQuarterlyRecArrayList) {

        ArrayList<ObjTranWMQYRec> objTranQuarterlyRecArrayList = new ArrayList<ObjTranWMQYRec>();

        boolean hasSourceData = generateAnalyzeSourceData(stockcode, queryRecordId, objTranQuarterlyRecArrayList, addedObjTranQuarterlyRecArrayList);
        if ( !hasSourceData ) {
            return null;
        }

        initMemory();

        ObjTranWMQYRec firstObj = objTranQuarterlyRecArrayList.get(0);
        if (null == firstObj) {
            return null;
        }

        sumTranCountAndVolAndMoneyAndSingleVol( firstObj.getTotalTranCount(), firstObj.getTotalVol(), firstObj.getTotalMoney(), firstObj.getSingleSumVol() );

        dateCount += firstObj.getDateCount();
        highest = firstObj.getHighest();
        lowest = firstObj.getLowest();

        getEachDayBuilder().append(firstObj.getEachDayBasic());

        int mqyIdx = firstObj.getRecordId();
        Queue<String> singleQueue = new LinkedList<String>();
        formatSingleRecordStat(mqyIdx, firstObj.getSingleRecStat().split("="), singleQueue);
        classifyingEachPriceStat(String.valueOf(firstObj.getRecordId()), firstObj.getPriceStat().split("="));

        int size = objTranQuarterlyRecArrayList.size();
        int lastObjIdx = size - 1;
        for (int i=1; i<size; i++) {

            ObjTranWMQYRec obj = objTranQuarterlyRecArrayList.get(i);
            if (null == obj) {
                lastObjIdx = i - 1;
                break;
            }

            sumTranCountAndVolAndMoneyAndSingleVol( obj.getTotalTranCount(), obj.getTotalVol(), obj.getTotalMoney(), obj.getSingleSumVol() );

            dateCount += obj.getDateCount();

            int tmpHighest = obj.getHighest();
            int tmpLowest = obj.getLowest();

            if (tmpHighest > highest) { highest = tmpHighest; }
            if (lowest > tmpLowest) { lowest = tmpLowest; }

            getEachDayBuilder().append("=").append(obj.getEachDayBasic());

            formatSingleRecordStat(obj.getRecordId(), obj.getSingleRecStat().split("="), singleQueue);
            classifyingEachPriceStat(String.valueOf(obj.getRecordId()), obj.getPriceStat().split("="));

        }

        ObjTranWMQYRec lastObj = objTranQuarterlyRecArrayList.get(lastObjIdx);

        String[] yearlyRecordItemArray = new String[17];

        yearlyRecordItemArray[0] = String.valueOf( mqyIdx / 10 );
        yearlyRecordItemArray[1] = String.valueOf( firstObj.getStartDate() );
        yearlyRecordItemArray[2] = String.valueOf( lastObj.getEndDate() );
        yearlyRecordItemArray[3] = String.valueOf( dateCount );
        yearlyRecordItemArray[4] = String.valueOf( firstObj.getLast() );
        yearlyRecordItemArray[5] = String.valueOf( firstObj.getOpen() );
        yearlyRecordItemArray[6] = String.valueOf( lastObj.getClose() );
        yearlyRecordItemArray[7] = String.valueOf( highest );
        yearlyRecordItemArray[8] = String.valueOf( lowest );
        yearlyRecordItemArray[9] = String.valueOf( getPriceCount() );
        yearlyRecordItemArray[10] = String.valueOf( totalTranCount );
        yearlyRecordItemArray[11] = String.valueOf( totalVol );
        yearlyRecordItemArray[12] = String.valueOf( totalMoney );
        yearlyRecordItemArray[13] = String.valueOf( singleSumVol );
        yearlyRecordItemArray[14] = new String( getSingleRec() );
        yearlyRecordItemArray[15] = new String( getEachDayBuilder().toString() );
        yearlyRecordItemArray[16] = new String( getPriceStat() );

        return yearlyRecordItemArray;

    }

    private boolean generateAnalyzeSourceData(String stockcode, String queryRecordId,
                                              ArrayList<ObjTranWMQYRec> objTranQuarterlyRecArrayList,
                                              ArrayList<ObjTranWMQYRec> addedObjTranQuarterlyRecArrayList) {

        DatabaseQueryServiceFacade.getInstance().setUpQueryer(4)
                .queryTranRecRange( true, stockcode, queryRecordId, objTranQuarterlyRecArrayList );

        if ( addedObjTranQuarterlyRecArrayList != null && !addedObjTranQuarterlyRecArrayList.isEmpty() ) {

            for (ObjTranWMQYRec obj : addedObjTranQuarterlyRecArrayList) {
                objTranQuarterlyRecArrayList.add(obj);
            }

        }

        if ( objTranQuarterlyRecArrayList.isEmpty() ) {
            return false;
        }

        return true;

    }

    private void formatSingleRecordStat(int recordIdx, String[] singleRecordArray, Queue<String> singleQueue) {

        for (String singleRec : singleRecordArray) {
            singleQueue.add(singleRec);
        }

        formatSingleRecStat(String.valueOf(recordIdx), singleQueue);
        singleQueue.clear();

    }

}
