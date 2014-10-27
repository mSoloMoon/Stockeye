package com.msolo.stockeye.gui.fragment.transaction.common;

import android.widget.ArrayAdapter;
import java.util.Arrays;
import java.util.List;

import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.gui.fragment.transaction.FragmentStockTranContentWebView;
import com.msolo.stockeye.service.db.build.ObjTranWMQYRec;

/**
 *
 * Adapter for view of stock transaction data used by MvcModelFragmentStockTranContent
 *
 * Created by mSolo on 2014/8/22.
 *
 */
public class ArrayAdapterTranMQYRec extends ArrayAdapterAbstractTranRec {

    private ObjTranWMQYRec objTranMQYRec;

    private String[] singleVolRecArray = null;

    public ArrayAdapterTranMQYRec(ObjTranWMQYRec obj) {
        objTranMQYRec = obj;
    }

    @Override
    public int getOpenFlag() {
        return checkPriceRiseOrFall(objTranMQYRec.getOpen(), objTranMQYRec.getLast());
    }

    @Override
    public int getCloseFlag() {
        return checkPriceRiseOrFall(objTranMQYRec.getClose(), objTranMQYRec.getLast());
    }

    @Override
    public int getHighestFlag() {
        return checkPriceRiseOrFall(objTranMQYRec.getHighest(), objTranMQYRec.getLast());
    }

    @Override
    public int getLowestFlag() {
        return checkPriceRiseOrFall(objTranMQYRec.getLowest(), objTranMQYRec.getLast());
    }

    @Override
    public String getOpenPrice() {
        return String.format("开盘: %.2f", (float) objTranMQYRec.getOpen() / 100);
    }

    @Override
    public String getClosePrice() {

        float closePrice = (float) objTranMQYRec.getClose() / 100;
        float lastPrice = (float) objTranMQYRec.getLast() / 100;
        return String.format("收盘: %.2f (%.2f%%)", closePrice, (closePrice - lastPrice) / lastPrice * 100);
    }

    @Override
    public String getPeriodDecorator() {
        return String.format("共%d天", objTranMQYRec.getDateCount());
    }

    @Override
    public String getPeriod() {

        int dateInInt = objTranMQYRec.getStartDate();
        int yearIntOfStart = dateInInt / 10000;
        int monthIntOfStart = dateInInt / 100 - yearIntOfStart * 100;
        int dayIntOfStart = dateInInt - dateInInt / 100 * 100;

        dateInInt = objTranMQYRec.getEndDate();
        int yearIntOfEnd = dateInInt / 10000;
        int monthIntOfEnd = dateInInt / 100 - yearIntOfEnd * 100;
        int dayIntOfEnd = dateInInt - dateInInt / 100 * 100;

        return String.format("开始: %d-%02d-%02d\n结束: %d-%02d-%02d",
                yearIntOfStart, monthIntOfStart, dayIntOfStart,
                yearIntOfEnd, monthIntOfEnd, dayIntOfEnd);

    }

    @Override
    public String getLastPrice() {
        return String.format("前收: %.2f", (float) objTranMQYRec.getLast() / 100);
    }

    @Override
    public String getHighestPrice() {
        return String.format("最高: %.2f", (float) objTranMQYRec.getHighest() / 100);
    }

    @Override
    public String getLowestPrice() {
        return String.format("最低: %.2f", (float) objTranMQYRec.getLowest() / 100);
    }

    @Override
    public String getTranCount() {
        return String.format("交易数: %d", objTranMQYRec.getTotalTranCount());
    }

    @Override
    public String getPriceCount() {
        return String.format("价格数: %d", objTranMQYRec.getTotalPriceCount());
    }

    @Override
    public String getVolume() {
        return String.format("成交量: %.4f亿", (float) objTranMQYRec.getTotalVol() / 100000000);
    }

    @Override
    public String getMoney() {
        return String.format("成交额: %.4f亿", (float) objTranMQYRec.getTotalMoney() / 100000000);
    }

    @Override
    public String[] getSingleMaxVolRecords() {

        List<String> singleRecList = Arrays.asList(objTranMQYRec.getSingleRecStat().split("="));

        int sizeOfSingleRecList = singleRecList.size();
        int sizeOfSingleVolRecs = sizeOfSingleRecList + 2;
        singleVolRecArray = new String[sizeOfSingleVolRecs];

        singleVolRecArray[0] = "时间\t价格\t \t成交(万)\t金额(百万)";
        singleVolRecArray[1] = String.format(" \t \t \t%d\t", objTranMQYRec.getSingleSumVol());
        for (int i = 2; i < sizeOfSingleVolRecs; i++) {
            singleVolRecArray[i] = singleRecList.get(i - 2);
        }

        return singleVolRecArray;
    }

    @Override
    public String[] getGeneralRecords() {

        StringBuilder eachDayRecords = new StringBuilder();

        eachDayRecords.append("日期\t开收高低\t价易数\t成交(万)\t金额(百万)=")
                .append(objTranMQYRec.getEachDayBasic());

        return eachDayRecords.toString().split("=");

    }

    @Override
    public String getEachPriceHeaderTitle() {
        return String.format("时间\t价格\t交易数\t成交(万)\t金额(百万)");
    }

    @Override
    public String[] getEachPriceRecords() {
        return objTranMQYRec.getPriceStat().split("=");
    }

    @Override
    public void setupDetailArrayAdapter(int newSpinnerId) {
        MQYDetailArrayAdapter.setSpinnerId( newSpinnerId );
        MQYDetailArrayAdapter.setLastPrice( objTranMQYRec.getLast() );
    }

    @Override
    public ArrayAdapter<String> getSingleMaxVolArrayAdapter(int resourceId) {
        return new MQYDetailArrayAdapter(StockeyeApp.appContext,
                resourceId,
                getSingleMaxVolRecords());
    }

    @Override
    public ArrayAdapter<String> getGeneralRecordsArrayAdapter(int resourceId) {
        return new MQYDetailArrayAdapter(StockeyeApp.appContext,
                resourceId,
                getGeneralRecords());
    }

    @Override
    public ArrayAdapter<String> getEachPriceRecordsArrayAdapter(int resourceId, String[] contentArray) {
        return new MQYDetailArrayAdapter(StockeyeApp.appContext,
                resourceId,
                contentArray);
    }

    @Override
    public void visulizeSingleMaxVolRecords(FragmentStockTranContentWebView webViewMaker) {

        webViewMaker.formatMaxVolSingleRecsForMQY(objTranMQYRec.getSingleSumVol(), getSingleMaxVolRecords());
        webViewMaker.loadOnlyPieChartForWebView();

    }

    @Override
    public void visulizeGeneralRecords(FragmentStockTranContentWebView webViewMaker) {

        int mqy = objTranMQYRec.getRecordId();
        if (mqy < 3000) {
            webViewMaker.formatEachdayRecsForYear(objTranMQYRec.getTotalVol(), objTranMQYRec.getEachDayBasic().split("="));
        } else if (mqy < 30000) {
            webViewMaker.formatEachdayRecsForQuarter(objTranMQYRec.getTotalVol(), objTranMQYRec.getEachDayBasic().split("="));
        } else {
            webViewMaker.formatEachdayRecs(objTranMQYRec.getTotalVol(), objTranMQYRec.getEachDayBasic().split("="));
        }
        webViewMaker.loadOnlyPieChartForWebView();

    }

    @Override
    public void visulizeEachPriceRecords(FragmentStockTranContentWebView webViewMaker) {

        webViewMaker.formatWMQYEachPriceRecs(objTranMQYRec.getHighest(), objTranMQYRec.getLowest(), objTranMQYRec.getTotalVol(), getEachPriceRecords());
        webViewMaker.loadOnlyPieChartForWebView();

    }

    private int checkPriceRiseOrFall(int price1, int price2) {

        if (price1 < price2) {
            return 2;
        } else if (price1 > price2) {
            return 1;
        } else {
            return 0;
        }

    }

}
