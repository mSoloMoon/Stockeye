package com.msolo.stockeye.business.stock.mine;

import com.msolo.stockeye.service.common.CommonServiceFacade;
import com.msolo.stockeye.service.db.build.ObjTranDailyRec;
import com.msolo.stockeye.service.db.access.DatabaseQueryServiceFacade;

import java.util.ArrayList;

/**
 *
 * apply : Abstract Factory Pattern - ConcreteProduct
 * apply : Strategy Pattern - ConcreteStrategy
 *
 * Created by mSolo on 2014/8/20.
 */
public class StockTransactionDataWeeklyAnalysis extends StockTransactionDataAnalysis implements StockTransactionDataAnalysisImp<ObjTranDailyRec> {

    protected StockTransactionDataWeeklyAnalysis() {}

    @Override
    public String[] analyzeToTransactionRecordItemArray(String stockcode, String queryRecordId, ArrayList<ObjTranDailyRec> addedObjTranDailyRecArrayList) {

        ArrayList<ObjTranDailyRec> objTranDailyRecArrayList = new ArrayList<ObjTranDailyRec>();

        boolean hasSourceData = generateAnalyzeSourceData(stockcode, queryRecordId, objTranDailyRecArrayList, addedObjTranDailyRecArrayList);
        if ( !hasSourceData ) {
            return null;
        }

        initMemory();

        ObjTranDailyRec firstObj = objTranDailyRecArrayList.get(0);

        int startDateIntFmt = firstObj.getRecordId();
        int weekIndexOfYear = CommonServiceFacade.getInstance().getWeekIndexOfYear(String.valueOf(startDateIntFmt));
        int yyyyWeekOfYear = Integer.parseInt(String.format("%d%02d", (startDateIntFmt - (startDateIntFmt - startDateIntFmt/10000 * 10000))/10000, weekIndexOfYear));

        sumTranCountAndVolAndMoneyAndSingleVol( firstObj.getTotalTranCount(), firstObj.getTotalVol(), firstObj.getTotalMoney(), firstObj.getSingleSumVol() );

        highest = firstObj.getHighest();
        lowest = firstObj.getLowest();

        formatEachDayRecord(startDateIntFmt, firstObj.getOpen(), firstObj.getClose(), highest, lowest,
                firstObj.getTotalPriceCount(), totalTranCount, totalVol, totalMoney);
        formatSingleRecStat(CommonServiceFacade.getInstance().getDateString(startDateIntFmt), firstObj.getSingleRecQueue());
        classifyingEachPriceStat(CommonServiceFacade.getInstance().getDateString(startDateIntFmt), firstObj.getPriceStat().split("="));

        int size = objTranDailyRecArrayList.size();
        int lastObjIdx = size - 1;
        for (int i=1; i<size; i++) {

            ObjTranDailyRec obj = objTranDailyRecArrayList.get(i);
            if (null == obj) {
                lastObjIdx = i - 1;
                break;
            }

            sumTranCountAndVolAndMoneyAndSingleVol( obj.getTotalTranCount(), obj.getTotalVol(), obj.getTotalMoney(), obj.getSingleSumVol() );

            int tmpHighest = obj.getHighest();
            int tmpLowest = obj.getLowest();

            if (tmpHighest > highest) { highest = tmpHighest; }
            if (lowest > tmpLowest) { lowest = tmpLowest; }

            formatEachDayRecord(obj.getRecordId(), obj.getOpen(), obj.getClose(), tmpHighest, tmpLowest,
                    obj.getTotalPriceCount(), obj.getTotalTranCount(), obj.getTotalVol(), obj.getTotalMoney());
            formatSingleRecStat(CommonServiceFacade.getInstance().getDateString(obj.getRecordId()), obj.getSingleRecQueue());
            classifyingEachPriceStat(CommonServiceFacade.getInstance().getDateString(obj.getRecordId()), obj.getPriceStat().split("="));

        }

        ObjTranDailyRec lastObj = objTranDailyRecArrayList.get(lastObjIdx);

        String[] weeklyRecordItemArray = new String[17];

        weeklyRecordItemArray[0] = String.valueOf( yyyyWeekOfYear );
        weeklyRecordItemArray[1] = String.valueOf( startDateIntFmt );
        weeklyRecordItemArray[2] = String.valueOf( lastObj.getRecordId() );
        weeklyRecordItemArray[3] = String.valueOf( weekIndexOfYear );
        weeklyRecordItemArray[4] = String.valueOf( firstObj.getLast() );
        weeklyRecordItemArray[5] = String.valueOf( firstObj.getOpen() );
        weeklyRecordItemArray[6] = String.valueOf( lastObj.getClose() );
        weeklyRecordItemArray[7] = String.valueOf( highest );
        weeklyRecordItemArray[8] = String.valueOf( lowest );
        weeklyRecordItemArray[9] = String.valueOf( getPriceCount() );
        weeklyRecordItemArray[10] = String.valueOf( totalTranCount );
        weeklyRecordItemArray[11] = String.valueOf( totalVol );
        weeklyRecordItemArray[12] = String.valueOf( totalMoney );
        weeklyRecordItemArray[13] = String.valueOf( singleSumVol );
        weeklyRecordItemArray[14] = new String( getSingleRec() );
        weeklyRecordItemArray[15] = new String( getEachDayRecord() );
        weeklyRecordItemArray[16] = new String( getPriceStat() );

        return weeklyRecordItemArray;

    }

    private boolean generateAnalyzeSourceData(String stockcode, String queryRecordId,
                                              ArrayList<ObjTranDailyRec> objTranDailyRecArrayList,
                                              ArrayList<ObjTranDailyRec> addedObjTranDailyRecArrayList) {

        DatabaseQueryServiceFacade.getInstance().setUpQueryer(1)
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
