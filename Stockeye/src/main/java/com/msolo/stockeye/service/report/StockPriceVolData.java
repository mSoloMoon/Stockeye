package com.msolo.stockeye.service.report;

import com.msolo.stockeye.gui.fragment.transaction.common.MvcModelFragmentStockTranContent;
import com.msolo.stockeye.service.common.CommonServiceFacade;
import com.msolo.stockeye.service.db.access.DatabaseQueryServiceFacade;
import com.msolo.stockeye.service.db.build.DatabaseObjConfigure;
import com.msolo.stockeye.service.db.build.ObjTranWMQYRec;

/**
 * Created by mSolo on 2014/8/28.
 */
public class StockPriceVolData {

    private static boolean isLatestMonthHappenedEnded = false;

    private StockPriceVolData() {}

    public static void getStockPriceVoDailyData(int recordArrayCapacity, String table, long[][] chartDailyRecordArray)  {

        DatabaseQueryServiceFacade.getInstance().queryTranDailyChartRecLimit(recordArrayCapacity, table, chartDailyRecordArray);

    }

    public static void getStockPriceVolWeeklyData(int recordArrayCapacity, String table, long[][] chartWeeklyRecordArray) {

        CommonServiceFacade commonServiceFacade = CommonServiceFacade.getInstance();

        StringBuilder currentRefreshingDateBuilder = new StringBuilder().append( commonServiceFacade.getRefreshingDate() );

        int counter = 1;
        while (true) {

            ObjTranWMQYRec objTranWeeklyRec = MvcModelFragmentStockTranContent.getObjStockWeeklyRec(currentRefreshingDateBuilder.toString(), table);
            if (objTranWeeklyRec != null) {

                DatabaseObjConfigure.setArrayTranMQYChartRec(counter, objTranWeeklyRec, chartWeeklyRecordArray);
                counter++;

            }

            currentRefreshingDateBuilder.replace( 0, 8, commonServiceFacade.getLastWeekStartDate(currentRefreshingDateBuilder.toString()) );
            if ( Integer.parseInt(currentRefreshingDateBuilder.toString()) < 20051232 || counter > recordArrayCapacity ) {
                break;
            }

        }

        chartWeeklyRecordArray[0][0] = counter + 1;

    }
    
    public static void getStockPriceVolMonthlyData(int recordArrayCapacity, String table, long[][] chartMonthlyRecordArray) {

        int adjustFactor = 0;

        String today = CommonServiceFacade.getInstance().getToday().replace("-", "");

        ObjTranWMQYRec objTranMonthlyRec = (ObjTranWMQYRec) DatabaseQueryServiceFacade.getInstance().setUpQueryer(2).queryOneTranRec(table, today.substring(0, 6));
        if (null == objTranMonthlyRec) {

            adjustFactor = 1;
            objTranMonthlyRec = MvcModelFragmentStockTranContent.getObjStockMonthlyRec(today, table);
            if (null != objTranMonthlyRec) {
                DatabaseObjConfigure.setArrayTranMQYChartRec(adjustFactor, objTranMonthlyRec, chartMonthlyRecordArray);
            } else {
                adjustFactor = 0;
                isLatestMonthHappenedEnded = false;
            }

        } else {
            isLatestMonthHappenedEnded = true;
        }

        DatabaseQueryServiceFacade.getInstance().queryTranMQYChartRecRange(adjustFactor, "200601205001", table, chartMonthlyRecordArray);

    }

    public static void getStockPriceVolQuarterlyData(int recordArrayCapacity, String table, long[][] chartQuarterlyRecordArray) {

        int adjustFactor = 0;

        if ( !isLatestMonthHappenedEnded ) {

            adjustFactor = 1;

            String today = CommonServiceFacade.getInstance().getToday().replace("-", "");
            ObjTranWMQYRec objTranQuarterlyRec = MvcModelFragmentStockTranContent.getObjStockQuarterlyRec(today, table);

            if (null != objTranQuarterlyRec) {
                DatabaseObjConfigure.setArrayTranMQYChartRec(adjustFactor, objTranQuarterlyRec, chartQuarterlyRecordArray);
            } else {
                adjustFactor = 0;
            }

        }

        DatabaseQueryServiceFacade.getInstance().queryTranMQYChartRecRange(adjustFactor, "2006120501", table, chartQuarterlyRecordArray);

    }

    public static void getStockPriceVolYearlyData(int recordArrayCapacity, String table, long[][] chartYearlyRecordArray) {

        int adjustFactor = 0;

        if ( !isLatestMonthHappenedEnded ) {

            adjustFactor = 1;

            String today = CommonServiceFacade.getInstance().getToday().replace("-", "");
            ObjTranWMQYRec objTranYearlyRec = MvcModelFragmentStockTranContent.getObjStockYearlyRec(today, table);

            if (null != objTranYearlyRec) {
                DatabaseObjConfigure.setArrayTranMQYChartRec(adjustFactor, objTranYearlyRec, chartYearlyRecordArray);
            } else {
                adjustFactor = 0;
            }

        }

        DatabaseQueryServiceFacade.getInstance().queryTranMQYChartRecRange(adjustFactor, "20062050", table, chartYearlyRecordArray);

    }

}
