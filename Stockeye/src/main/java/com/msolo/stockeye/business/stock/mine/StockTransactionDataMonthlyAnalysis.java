package com.msolo.stockeye.business.stock.mine;

import com.msolo.stockeye.service.common.CommonServiceFacade;
import com.msolo.stockeye.service.db.access.DatabaseQueryServiceFacade;
import com.msolo.stockeye.service.db.build.ObjTranDailyRec;

import java.util.ArrayList;

/**
 *
 * apply : Abstract Factory Pattern - ConcreteProduct
 * apply : Strategy Pattern - ConcreteStrategy
 *
 * Created by mSolo on 2014/8/19.
 *
 */
public class StockTransactionDataMonthlyAnalysis extends StockTransactionDataAnalysis implements StockTransactionDataAnalysisImp<ObjTranDailyRec> {

    protected StockTransactionDataMonthlyAnalysis() {}

    @Override
    public String[] analyzeToTransactionRecordItemArray(String stockcode, String queryRecordId, ArrayList<ObjTranDailyRec> addedObjTranDailyRecArrayList) {

        CommonServiceFacade commonServiceFacade = CommonServiceFacade.getInstance();

        ArrayList<ObjTranDailyRec> objTranDailyRecArrayList = new ArrayList<ObjTranDailyRec>();

        boolean hasSourceData = generateAnalyzeSourceData(stockcode, queryRecordId, objTranDailyRecArrayList, addedObjTranDailyRecArrayList);
        if ( !hasSourceData ) {
            return null;
        }

        initMemory();

        ObjTranDailyRec firstObj = objTranDailyRecArrayList.get(0);

        sumTranCountAndVolAndMoneyAndSingleVol( firstObj.getTotalTranCount(), firstObj.getTotalVol(), firstObj.getTotalMoney(), firstObj.getSingleSumVol() );

        highest = firstObj.getHighest();
        lowest = firstObj.getLowest();

        int startDateIntFmt = firstObj.getRecordId();

        formatEachDayRecord(startDateIntFmt, firstObj.getOpen(), firstObj.getClose(), highest, lowest,
                firstObj.getTotalPriceCount(), totalTranCount, totalVol, totalMoney);
        formatSingleRecStat( commonServiceFacade.getDateString(startDateIntFmt), firstObj.getSingleRecQueue() );
        classifyingEachPriceStat(commonServiceFacade.getDateString(startDateIntFmt), firstObj.getPriceStat().split("="));

        int size = objTranDailyRecArrayList.size();
        int lastObjIdx = size - 1;
        for (int i=1; i<size; i++) {

            ObjTranDailyRec obj = objTranDailyRecArrayList.get(i);
            if (null == obj) {
                lastObjIdx = i -1;
                break;
            }

            sumTranCountAndVolAndMoneyAndSingleVol( obj.getTotalTranCount(), obj.getTotalVol(), obj.getTotalMoney(), obj.getSingleSumVol() );

            int tmpHighest = obj.getHighest();
            int tmpLowest = obj.getLowest();

            if (tmpHighest > highest) { highest = tmpHighest; }
            if (lowest > tmpLowest) { lowest = tmpLowest; }

            formatEachDayRecord(obj.getRecordId(), obj.getOpen(), obj.getClose(), tmpHighest, tmpLowest,
                    obj.getTotalPriceCount(), obj.getTotalTranCount(), obj.getTotalVol(), obj.getTotalMoney());
            formatSingleRecStat( commonServiceFacade.getDateString(obj.getRecordId()), obj.getSingleRecQueue() );
            classifyingEachPriceStat(commonServiceFacade.getDateString(obj.getRecordId()), obj.getPriceStat().split("="));

        }

        ObjTranDailyRec lastObj = objTranDailyRecArrayList.get(lastObjIdx);

        String[] monthlyRecordItemArray = new String[17];

        monthlyRecordItemArray[0] = String.valueOf( (startDateIntFmt - (startDateIntFmt - startDateIntFmt/100 * 100)) / 100 );
        monthlyRecordItemArray[1] = String.valueOf( startDateIntFmt );
        monthlyRecordItemArray[2] = String.valueOf( lastObj.getRecordId() );
        monthlyRecordItemArray[3] = String.valueOf( size );
        monthlyRecordItemArray[4] = String.valueOf( firstObj.getLast() );
        monthlyRecordItemArray[5] = String.valueOf( firstObj.getOpen() );
        monthlyRecordItemArray[6] = String.valueOf( lastObj.getClose() );
        monthlyRecordItemArray[7] = String.valueOf( highest);
        monthlyRecordItemArray[8] = String.valueOf( lowest);
        monthlyRecordItemArray[9] = String.valueOf( getPriceCount() );
        monthlyRecordItemArray[10] = String.valueOf( totalTranCount );
        monthlyRecordItemArray[11] = String.valueOf( totalVol );
        monthlyRecordItemArray[12] = String.valueOf( totalMoney );
        monthlyRecordItemArray[13] = String.valueOf( singleSumVol );
        monthlyRecordItemArray[14] = new String( getSingleRec() );
        monthlyRecordItemArray[15] = new String( getEachDayRecord() );
        monthlyRecordItemArray[16] = new String( getPriceStat() );

        return monthlyRecordItemArray;

    }

    private boolean generateAnalyzeSourceData(String stockcode, String queryRecordId,
                                              ArrayList<ObjTranDailyRec> objTranDailyRecArrayList,
                                              ArrayList<ObjTranDailyRec> addedObjTranDailyRecArrayList) {

        DatabaseQueryServiceFacade.getInstance().setUpQueryer(0)
                .queryTranRecRange( true, stockcode, queryRecordId, objTranDailyRecArrayList );

        if ( addedObjTranDailyRecArrayList != null && !addedObjTranDailyRecArrayList.isEmpty() ) {

            for (ObjTranDailyRec obj : addedObjTranDailyRecArrayList) {
                objTranDailyRecArrayList.add(obj);
            }

        }

        if ( objTranDailyRecArrayList.isEmpty() ) {
            return false;
        }

        return true;

    }

}
