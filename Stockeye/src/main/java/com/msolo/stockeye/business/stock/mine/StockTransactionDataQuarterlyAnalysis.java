package com.msolo.stockeye.business.stock.mine;

import com.msolo.stockeye.service.common.CommonServiceFacade;
import com.msolo.stockeye.service.db.access.DatabaseQueryServiceFacade;
import com.msolo.stockeye.service.db.build.ObjTranWMQYRec;

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
public class StockTransactionDataQuarterlyAnalysis extends StockTransactionDataAnalysis implements StockTransactionDataAnalysisImp<ObjTranWMQYRec> {

    protected StockTransactionDataQuarterlyAnalysis() {}

    @Override
    public String[] analyzeToTransactionRecordItemArray(String stockcode, String queryRecordId, ArrayList<ObjTranWMQYRec> addedObjTranMonthlyRecArrayList) {

        ArrayList<ObjTranWMQYRec> objTranMonthlyRecArrayList = new ArrayList<ObjTranWMQYRec>();

        boolean hasSourceData = generateAnalyzeSourceData(stockcode, queryRecordId, objTranMonthlyRecArrayList, addedObjTranMonthlyRecArrayList);
        if ( !hasSourceData ) {
            return null;
        }

        initMemory();

        ObjTranWMQYRec firstObj = objTranMonthlyRecArrayList.get(0);
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
        classifyingEachPriceStat(String.valueOf(mqyIdx), firstObj.getPriceStat().split("="));

        int size = objTranMonthlyRecArrayList.size();
        int lastObjIdx = size - 1;
        for (int i=1; i<size; i++) {

            ObjTranWMQYRec obj = objTranMonthlyRecArrayList.get(i);
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

        ObjTranWMQYRec lastObj = objTranMonthlyRecArrayList.get(lastObjIdx);

        int yearQuarter = mqyIdx / 100 * 10 + CommonServiceFacade.getInstance().getQuarter(mqyIdx - mqyIdx / 100 * 100);

        String[] quarterlyRecordItemArray = new String[17];

        quarterlyRecordItemArray[0] = String.valueOf( yearQuarter );
        quarterlyRecordItemArray[1] = String.valueOf( firstObj.getStartDate() );
        quarterlyRecordItemArray[2] = String.valueOf( lastObj.getEndDate() );
        quarterlyRecordItemArray[3] = String.valueOf( dateCount );
        quarterlyRecordItemArray[4] = String.valueOf( firstObj.getLast() );
        quarterlyRecordItemArray[5] = String.valueOf( firstObj.getOpen() );
        quarterlyRecordItemArray[6] = String.valueOf( lastObj.getClose() );
        quarterlyRecordItemArray[7] = String.valueOf( highest );
        quarterlyRecordItemArray[8] = String.valueOf( lowest );
        quarterlyRecordItemArray[9] = String.valueOf( getPriceCount() );
        quarterlyRecordItemArray[10] = String.valueOf( totalTranCount );
        quarterlyRecordItemArray[11] = String.valueOf( totalVol );
        quarterlyRecordItemArray[12] = String.valueOf( totalMoney );
        quarterlyRecordItemArray[13] = String.valueOf( singleSumVol );
        quarterlyRecordItemArray[14] = new String( getSingleRec() );
        quarterlyRecordItemArray[15] = new String( getEachDayBuilder().toString() );
        quarterlyRecordItemArray[16] = new String( getPriceStat() );

        return quarterlyRecordItemArray;

    }

    private boolean generateAnalyzeSourceData(String stockcode, String queryRecordId,
                                              ArrayList<ObjTranWMQYRec> objTranMonthlyRecArrayList,
                                              ArrayList<ObjTranWMQYRec> addedObjTranMonthlyRecArrayList) {

        DatabaseQueryServiceFacade.getInstance().setUpQueryer(3)
                .queryTranRecRange( true, stockcode, queryRecordId, objTranMonthlyRecArrayList );

        if ( addedObjTranMonthlyRecArrayList != null && !addedObjTranMonthlyRecArrayList.isEmpty() ) {

            for (ObjTranWMQYRec obj : addedObjTranMonthlyRecArrayList) {
                objTranMonthlyRecArrayList.add(obj);
            }

        }

        if ( objTranMonthlyRecArrayList.isEmpty() ) {
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
