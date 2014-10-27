package com.msolo.stockeye.gui.fragment.transaction.common;

import android.widget.ArrayAdapter;

import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.gui.fragment.transaction.FragmentStockTranContentWebView;
import com.msolo.stockeye.service.db.build.ObjTranDailyRec;

import java.util.Queue;

class ArrayAdapterTranDailyRec extends ArrayAdapterAbstractTranRec {

    private ObjTranDailyRec objTranDailyRec;

    private String[] singleVolRecArray = new String[36];
    private String[] fifteenRecArray = new String[17];

    ArrayAdapterTranDailyRec(ObjTranDailyRec obj) {
        objTranDailyRec = obj;
    }

    @Override
    public int getOpenFlag() {
        return checkPriceRiseOrFall(objTranDailyRec.getOpen(), objTranDailyRec.getLast());
    }

    @Override
    public int getCloseFlag() {
        return checkPriceRiseOrFall(objTranDailyRec.getClose(), objTranDailyRec.getLast());
    }

    @Override
    public int getHighestFlag() {
        return checkPriceRiseOrFall(objTranDailyRec.getHighest(), objTranDailyRec.getLast());
    }

    @Override
    public int getLowestFlag() {
        return checkPriceRiseOrFall(objTranDailyRec.getLowest(), objTranDailyRec.getLast());
    }

    @Override
    public String getOpenPrice() {
        return String.format("开盘: %.2f", (float) objTranDailyRec.getOpen() / 100);
    }

    @Override
    public String getClosePrice() {

        float closePrice = (float) objTranDailyRec.getClose() / 100;
        float lastPrice = (float) objTranDailyRec.getLast() / 100;
        return String.format("收盘: %.2f (%.2f%%)", closePrice, (closePrice - lastPrice) / lastPrice * 100);

    }

    @Override
    public String getPeriodDecorator() {

        String priceTimeGap = null;
        String timeGap = String.format("%d", objTranDailyRec.getPriceTimeGap() );
        if (timeGap.length() > 2) {
            priceTimeGap =  String.format("%s:%s", timeGap.substring(0, 1), timeGap.substring(1, 3));
        } else {
            priceTimeGap = String.format("0:%s", timeGap);
        }

        String weekDay = null;
        switch (objTranDailyRec.getWeekday()) {
            case 1: weekDay = "一"; break;
            case 2: weekDay = "二"; break;
            case 3: weekDay = "三"; break;
            case 4: weekDay = "四"; break;
            case 5: weekDay = "五"; break;
        }

        return String.format("周%s, 时差: %s", weekDay, priceTimeGap);
    }

    @Override
    public String getPeriod() {

        int dateInInt = objTranDailyRec.getRecordId();
        int yearInInt = dateInInt / 10000;
        int monthInInt = dateInInt / 100 - yearInInt * 100;
        int dayInInt = dateInInt - dateInInt / 100 * 100;

        return String.format("日期: %d-%02d-%02d", yearInInt, monthInInt, dayInInt);

    }

    @Override
    public String getLastPrice() {
        return String.format("昨收: %.2f", (float) objTranDailyRec.getLast() / 100);
    }

    @Override
    public String getHighestPrice() {
        return String.format("最高: %.2f", (float) objTranDailyRec.getHighest() / 100);
    }

    @Override
    public String getLowestPrice() {
        return String.format("最低: %.2f", (float) objTranDailyRec.getLowest() / 100);
    }

    @Override
    public String getTranCount() {
        return String.format("交易数: %d", objTranDailyRec.getTotalTranCount());
    }

    @Override
    public String getPriceCount() {
        return String.format("价格数: %d", objTranDailyRec.getTotalPriceCount());
    }

    @Override
    public String getVolume() {
        return String.format("成交量: %.3f万", (float) objTranDailyRec.getTotalVol() / 10000);
    }

    @Override
    public String getMoney() {

        float money = (float) objTranDailyRec.getTotalMoney() / 100000000;
        if (money < 1) {
            return String.format("成交额: %.4f百万", (float) objTranDailyRec.getTotalMoney() / 1000000);
        } else {
            return String.format("成交额: %.4f亿", money);
        }

    }

    @Override
    public String[] getSingleMaxVolRecords() {

        singleVolRecArray[0] = "时间\t价格\t \t成交(手)\t金额(元)";
        singleVolRecArray[1] = String.format(" \t \t \t%d\t", objTranDailyRec.getSingleSumVol());
        Queue<String> singleRecQueue = objTranDailyRec.getSingleRecQueue();
        for (int i = 2; i < 36; i++) {
            singleVolRecArray[i] = singleRecQueue.poll();
        }

        return singleVolRecArray;
    }

    @Override
    public String[] getGeneralRecords() {

        Queue<String> fifteenQueue = objTranDailyRec.getFifteenMinuteRecQueue();

        fifteenRecArray[0] = "价格1\t价格2\t价易数\t成交(万)\t金额(万)";
        for (int i = 1; i < 17; i++) {
            fifteenRecArray[i] = fifteenQueue.poll();
        }

        return fifteenRecArray;
    }

    @Override
    public String getEachPriceHeaderTitle() {
        return String.format("时间\t价格\t交易数\t成交(手)\t金额(万)");
    }

    @Override
    public String[] getEachPriceRecords() {
        return objTranDailyRec.getPriceStat().split("=");
    }

    @Override
    public void setupDetailArrayAdapter(int newSpinnerId) {
        DailyDetailArrayAdapter.setSpinnerId(newSpinnerId);
        DailyDetailArrayAdapter.setLastPrice(objTranDailyRec.getLast());
    }

    @Override
    public ArrayAdapter<String> getSingleMaxVolArrayAdapter(int resourceId) {
        return new DailyDetailArrayAdapter(StockeyeApp.appContext,
                resourceId,
                getSingleMaxVolRecords());
    }

    @Override
    public ArrayAdapter<String> getGeneralRecordsArrayAdapter(int resourceId) {
        return new DailyDetailArrayAdapter(StockeyeApp.appContext,
                resourceId,
                getGeneralRecords());
    }

    @Override
    public ArrayAdapter<String> getEachPriceRecordsArrayAdapter(int resourceId, String[] contentArray) {
        return new DailyDetailArrayAdapter(StockeyeApp.appContext,
                resourceId,
                contentArray);
    }

    @Override
    public void visulizeSingleMaxVolRecords(FragmentStockTranContentWebView webViewMaker) {

        webViewMaker.formatMaxVolSingleRecs(objTranDailyRec.getSingleSumVol(), singleVolRecArray);
        webViewMaker.loadOnlyPieChartForWebView();

    }

    @Override
    public void visulizeGeneralRecords(FragmentStockTranContentWebView webViewMaker) {

        webViewMaker.formatFifteenRecsForPieChart(objTranDailyRec.getTotalVol(), fifteenRecArray);
        webViewMaker.formatFifteenRecsForLineChart(objTranDailyRec.getOpen(),
                objTranDailyRec.getClose(),
                objTranDailyRec.getHighest(),
                objTranDailyRec.getLowest(),
                fifteenRecArray);
        webViewMaker.loadChartForWebView();

    }

    @Override
    public void visulizeEachPriceRecords(FragmentStockTranContentWebView webViewMaker) {

        webViewMaker.formatEachPriceRecs(objTranDailyRec.getTotalVol(), getEachPriceRecords());
        webViewMaker.loadOnlyPieChartForWebView();

    }
/*
    @Override
    public void setupKChartView(KChartView kChartView) {

        new UpdateDailyChartViewAsyncTask(kChartView).execute();

    }
*/
    private int checkPriceRiseOrFall(int price1, int price2) {

        if (price1 < price2) {              // fall, green
            return 2;
        } else if (price1 > price2) {       // rise, red
            return 1;
        } else {                            // even, white
            return 0;
        }

    }

}