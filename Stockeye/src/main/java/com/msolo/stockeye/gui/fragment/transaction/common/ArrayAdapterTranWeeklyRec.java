package com.msolo.stockeye.gui.fragment.transaction.common;

import android.widget.ArrayAdapter;

import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.gui.fragment.transaction.FragmentStockTranContentWebView;
import com.msolo.stockeye.service.db.build.ObjTranWMQYRec;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mSolo on 2014/8/22.
 */
public class ArrayAdapterTranWeeklyRec extends ArrayAdapterAbstractTranRec {

    private ObjTranWMQYRec objTranWeeklyRec;

    private String[] singleVolRecArray = null;

    public ArrayAdapterTranWeeklyRec(ObjTranWMQYRec obj) {
        objTranWeeklyRec = obj;
    }

    @Override
    public int getOpenFlag() {
        return checkPriceRiseOrFall( objTranWeeklyRec.getOpen(), objTranWeeklyRec.getLast() );
    }

    @Override
    public int getCloseFlag() {
        return checkPriceRiseOrFall( objTranWeeklyRec.getClose(), objTranWeeklyRec.getLast() );
    }

    @Override
    public int getHighestFlag() {
        return checkPriceRiseOrFall( objTranWeeklyRec.getHighest(), objTranWeeklyRec.getLast() );
    }

    @Override
    public int getLowestFlag() {
        return checkPriceRiseOrFall( objTranWeeklyRec.getLowest(), objTranWeeklyRec.getLast() );
    }

    @Override
    public String getOpenPrice() {
        return String.format( "开盘: %.2f", (float)objTranWeeklyRec.getOpen()/100 );
    }

    @Override
    public String getClosePrice() {

        float closePrice = (float)objTranWeeklyRec.getClose()/100;
        float lastPrice = (float)objTranWeeklyRec.getLast()/100;
        return String.format( "收盘: %.2f (%.2f%%)", closePrice, (closePrice - lastPrice) / lastPrice * 100 );
    }

    @Override
    public String getPeriodDecorator() {
        int weekOfYear = objTranWeeklyRec.getRecordId();
        return String.format( "第%02d周", weekOfYear - weekOfYear/100 * 100 );
    }

    @Override
    public String getPeriod() {

        int dateInInt = objTranWeeklyRec.getStartDate();
        int yearInIntOfStart = dateInInt / 10000;
        int monthInIntOfStart = dateInInt / 100 - yearInIntOfStart * 100;
        int dayInIntOfStart = dateInInt - dateInInt / 100 * 100;

        dateInInt = objTranWeeklyRec.getEndDate();
        int yearInIntOfEnd = dateInInt / 10000;
        int monthInIntOfEnd = dateInInt / 100 - yearInIntOfStart * 100;
        int dayInIntOfEnd = dateInInt - dateInInt / 100 * 100;

        return String.format("开始: %d-%02d-%02d\n结束: %d-%02d-%02d",
                yearInIntOfStart, monthInIntOfStart, dayInIntOfStart,
                yearInIntOfEnd, monthInIntOfEnd, dayInIntOfEnd);

    }

    @Override
    public String getLastPrice() {
        return String.format( "前收: %.2f", (float)objTranWeeklyRec.getLast()/100 );
    }

    @Override
    public String getHighestPrice() {
        return String.format( "最高: %.2f", (float)objTranWeeklyRec.getHighest()/100 );
    }

    @Override
    public String getLowestPrice() {
        return String.format( "最低: %.2f", (float)objTranWeeklyRec.getLowest()/100 );
    }

    @Override
    public String getTranCount() {
        return String.format( "交易数: %d", objTranWeeklyRec.getTotalTranCount() );
    }

    @Override
    public String getPriceCount() {
        return String.format( "价格数: %d", objTranWeeklyRec.getTotalPriceCount() );
    }

    @Override
    public String getVolume() {
        return String.format( "成交量: %.4f亿", (float)objTranWeeklyRec.getTotalVol()/100000000 );
    }

    @Override
    public String getMoney() {
        return String.format( "成交额: %.4f亿", (float)objTranWeeklyRec.getTotalMoney()/100000000 );
    }

    @Override
    public String[] getSingleMaxVolRecords() {

        List<String> singleRecList = Arrays.asList(objTranWeeklyRec.getSingleRecStat().split("="));

        int sizeOfSingleRecList = singleRecList.size();
        int sizeOfSingleVolRecs = sizeOfSingleRecList + 2;
        singleVolRecArray = new String[sizeOfSingleVolRecs];

        singleVolRecArray[0] = "时间\t价格\t \t成交(百手)\t金额(万)";
        singleVolRecArray[1] = String.format(" \t \t \t%d\t", objTranWeeklyRec.getSingleSumVol());
        for (int i=2; i<sizeOfSingleVolRecs; i++) {
            singleVolRecArray[i] = singleRecList.get(i-2);
        }

        return singleVolRecArray;
    }

    @Override
    public String[] getGeneralRecords() {

        StringBuilder eachDayRecords = new StringBuilder();

        eachDayRecords.append("日期\t开收高低\t价易数\t成交(万)\t金额(百万)=")
                .append(objTranWeeklyRec.getEachDayBasic());

        return eachDayRecords.toString().split("=");

    }

    @Override
    public String getEachPriceHeaderTitle() {
        return String.format("时间\t价格\t交易数\t成交(万)\t金额(百万)");
    }

    @Override
    public String[] getEachPriceRecords() {
        return objTranWeeklyRec.getPriceStat().split("=");
    }

    @Override
    public void setupDetailArrayAdapter(int newSpinnerId) {
        WeeklyDetailArrayAdapter.setSpinnerId( newSpinnerId );
        WeeklyDetailArrayAdapter.setLastPrice( objTranWeeklyRec.getLast() );
    }

    @Override
    public ArrayAdapter<String> getSingleMaxVolArrayAdapter(int resourceId) {
        return new WeeklyDetailArrayAdapter(StockeyeApp.appContext,
                resourceId,
                getSingleMaxVolRecords());
    }

    @Override
    public ArrayAdapter<String> getGeneralRecordsArrayAdapter(int resourceId) {
        return new WeeklyDetailArrayAdapter(StockeyeApp.appContext,
                resourceId,
                getGeneralRecords());
    }

    @Override
    public ArrayAdapter<String> getEachPriceRecordsArrayAdapter(int resourceId, String[] contentArray) {
        return new WeeklyDetailArrayAdapter(StockeyeApp.appContext,
                resourceId,
                contentArray);
    }

    @Override
    public void visulizeSingleMaxVolRecords(FragmentStockTranContentWebView webViewMaker) {

        webViewMaker.formatMaxVolSingleRecs(objTranWeeklyRec.getSingleSumVol(), singleVolRecArray);
        webViewMaker.loadOnlyPieChartForWebView();

    }

    @Override
    public void visulizeGeneralRecords(FragmentStockTranContentWebView webViewMaker) {

        webViewMaker.formatEachdayRecs(objTranWeeklyRec.getTotalVol(), objTranWeeklyRec.getEachDayBasic().split("="));
        webViewMaker.loadOnlyPieChartForWebView();

    }

    @Override
    public void visulizeEachPriceRecords(FragmentStockTranContentWebView webViewMaker) {

        webViewMaker.formatWMQYEachPriceRecs(objTranWeeklyRec.getHighest(), objTranWeeklyRec.getLowest(), objTranWeeklyRec.getTotalVol(), getEachPriceRecords());
        webViewMaker.loadOnlyPieChartForWebView();

    }

    /*
    @Override
    public void setupKChartView(KChartView kChartView) {

    }
    */
    private int checkPriceRiseOrFall(int price1, int price2) {

        if (price1 < price2) {
            return 2;
        } else if (price1 > price2){
            return 1;
        } else {
            return 0;
        }

    }

}
