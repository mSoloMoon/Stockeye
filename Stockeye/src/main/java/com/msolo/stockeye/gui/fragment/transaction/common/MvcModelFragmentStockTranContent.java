package com.msolo.stockeye.gui.fragment.transaction.common;

import com.msolo.stockeye.R;
import com.msolo.stockeye.business.stock.mine.DataMiningFacade;
import com.msolo.stockeye.service.common.CommonServiceFacade;
import com.msolo.stockeye.service.db.access.DatabaseQueryServiceFacade;
import com.msolo.stockeye.service.db.build.DatabaseObjConfigure;
import com.msolo.stockeye.service.db.build.ObjTranDailyRec;
import com.msolo.stockeye.service.db.build.ObjTranWMQYRec;

import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/22.
 */
public class MvcModelFragmentStockTranContent {

    private static String defaultStockcode = null;
    private static String defaultQueryId = null;

    private MvcModelFragmentStockTranContent() {}

    public static ArrayAdapterAbstractTranRec getDefaultMvcMode(String stockcode, String queryRecordId) {

        defaultStockcode = stockcode;
        defaultQueryId = queryRecordId;

        ObjTranDailyRec objTranDailyRec = (ObjTranDailyRec) DatabaseQueryServiceFacade.getInstance().setUpQueryer(0).queryOneTranRec(defaultStockcode, queryRecordId);
        return new ArrayAdapterTranDailyRec(objTranDailyRec);

    }

    public static ArrayAdapterAbstractTranRec getMvcModel(int typeId, String queryRecordId) {

        if (queryRecordId == null) {
            queryRecordId = defaultQueryId;
        }

        switch (typeId) {

            case R.id.type_day:
                return new ArrayAdapterTranDailyRec( getObjStockDailyRec(queryRecordId) );

            case R.id.type_week:
                return new ArrayAdapterTranWeeklyRec( getObjStockWeeklyRec(queryRecordId, defaultStockcode) );

            case R.id.type_month:
                return new ArrayAdapterTranMQYRec( getObjStockMonthlyRec(queryRecordId, defaultStockcode) );

            case R.id.type_quarter:
                return new ArrayAdapterTranMQYRec( getObjStockQuarterlyRec(queryRecordId, defaultStockcode) );

            case R.id.type_year:
                return new ArrayAdapterTranMQYRec( getObjStockYearlyRec(queryRecordId, defaultStockcode) );

            case 6:
                return new ArrayAdapterTranMQYRec( getObjStockDateRangeMonthlyRec(queryRecordId, defaultStockcode) );

            default:
                break;

        }

        // should not go here
        return null;

    }

    private static ObjTranDailyRec getObjStockDailyRec(String queryRecordId) {

        return (ObjTranDailyRec) DatabaseQueryServiceFacade.getInstance().setUpQueryer(0).queryOneTranRec(defaultStockcode, queryRecordId);

    }

    public static ObjTranWMQYRec getObjStockWeeklyRec(String queryRecordId, String table) {

        String[] dateArray = CommonServiceFacade.getInstance().getDateArrayOfWeek(queryRecordId);
        ArrayList<String[]> tranWeeklyRecItemsArrayList = DataMiningFacade.getInstance().setUpDataMiningAnalyzer(1)
                .analyze(table, new String[]{String.format("%s%s", dateArray[0], dateArray[dateArray.length - 1])}, null);

        if ( tranWeeklyRecItemsArrayList.isEmpty() ) {
            return null;
        }

        ObjTranWMQYRec objTranWeeklyRec = new ObjTranWMQYRec();
        DatabaseObjConfigure.setObjTranMQYRec( tranWeeklyRecItemsArrayList.get(0), objTranWeeklyRec );

        return objTranWeeklyRec;

    }

    public static ObjTranWMQYRec getObjStockDateRangeMonthlyRec(String queryRecordId, String table) {

        ArrayList<String[]> monthlyRecordItemsArrayList = DataMiningFacade.getInstance().setUpDataMiningAnalyzer(2)
                .analyze(table, new String[]{queryRecordId}, null);

        if ( monthlyRecordItemsArrayList.isEmpty() ) {
            return null;
        }

        ObjTranWMQYRec objTranMonthlyRec = new ObjTranWMQYRec();
        DatabaseObjConfigure.setObjTranMQYRec( monthlyRecordItemsArrayList.get(0), objTranMonthlyRec );

        return objTranMonthlyRec;

    }

    public static ObjTranWMQYRec getObjStockMonthlyRec(String queryRecordId, String table) {

        String monthId = queryRecordId.substring(0, 6);
        ObjTranWMQYRec objTranMonthlyRec = (ObjTranWMQYRec) DatabaseQueryServiceFacade.getInstance().setUpQueryer(2).queryOneTranRec(table, monthId);
        if (objTranMonthlyRec == null) {

            ArrayList<String[]> monthlyRecordItemsArrayList = DataMiningFacade.getInstance().setUpDataMiningAnalyzer(2)
                    .analyze(table, new String[]{String.format("%s%02d%s", monthId, 0, defaultQueryId)}, null);

            if ( monthlyRecordItemsArrayList.isEmpty() ) {
                return null;
            }

            objTranMonthlyRec = new ObjTranWMQYRec();
            DatabaseObjConfigure.setObjTranMQYRec( monthlyRecordItemsArrayList.get(0), objTranMonthlyRec );

        }

        return objTranMonthlyRec;

    }

    public static ObjTranWMQYRec getObjStockQuarterlyRec(String queryRecordId, String table) {

        CommonServiceFacade commonServiceFacade = CommonServiceFacade.getInstance();
        int quarter = commonServiceFacade.getQuarter(Integer.parseInt(queryRecordId.substring(4, 6)));

        String queryQuarterId = String.format("%s%d", queryRecordId.substring(0, 4), quarter);
        ObjTranWMQYRec objTranQuarterlyRec = (ObjTranWMQYRec) DatabaseQueryServiceFacade.getInstance().setUpQueryer(3).queryOneTranRec(table, queryQuarterId);

        if (objTranQuarterlyRec == null) {

            ArrayList<ObjTranWMQYRec> objTranMonthlyRecArrayList = new ArrayList<ObjTranWMQYRec>();
            objTranMonthlyRecArrayList.add( getObjStockMonthlyRec(queryRecordId, table) );

            String[] monthArrayForQuarter = commonServiceFacade.getMonthArrayForTheQuarter(Integer.parseInt(queryRecordId.substring(0, 6)));
            ArrayList<String[]> quarterlyRecordItemsArrayList =  DataMiningFacade.getInstance().setUpDataMiningAnalyzer(3)
                    .analyze(table, new String[]{String.format("%s%s", monthArrayForQuarter[0], monthArrayForQuarter[2])}, objTranMonthlyRecArrayList);

            if ( quarterlyRecordItemsArrayList.isEmpty() ) {
                return null;
            }

            objTranQuarterlyRec = new ObjTranWMQYRec();
            DatabaseObjConfigure.setObjTranMQYRec(quarterlyRecordItemsArrayList.get(0), objTranQuarterlyRec);

        }

        return objTranQuarterlyRec;

    }

    public static ObjTranWMQYRec getObjStockYearlyRec(String queryRecordId, String table) {

        ObjTranWMQYRec objTranYearlyRec = (ObjTranWMQYRec) DatabaseQueryServiceFacade.getInstance().setUpQueryer(4).queryOneTranRec(table, queryRecordId.substring(0, 4));
        if ( objTranYearlyRec == null ) {

            ArrayList<ObjTranWMQYRec> objTranQuarterlyRecArrayList = new ArrayList<ObjTranWMQYRec>();
            objTranQuarterlyRecArrayList.add( getObjStockQuarterlyRec(queryRecordId, table) );

            int yearPre = Integer.parseInt(queryRecordId.substring(0, 4)) * 10;
            int[] quarterArrayForYear = new int[]{yearPre + 1, yearPre + 2, yearPre + 3, yearPre + 4};

            ArrayList<String[]> yearlyRecordItemsArrayList = DataMiningFacade.getInstance().setUpDataMiningAnalyzer(4)
                    .analyze(table, new String[]{String.format("%d%d", quarterArrayForYear[0], quarterArrayForYear[3])}, objTranQuarterlyRecArrayList);

            if ( yearlyRecordItemsArrayList.isEmpty() ) {
                return null;
            }

            objTranYearlyRec = new ObjTranWMQYRec();
            DatabaseObjConfigure.setObjTranMQYRec(yearlyRecordItemsArrayList.get(0), objTranYearlyRec);

        }

        return objTranYearlyRec;

    }

}
