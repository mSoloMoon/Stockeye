package com.msolo.stockeye.gui.fragment.transaction.common;

import android.widget.ArrayAdapter;

import com.msolo.stockeye.gui.fragment.transaction.FragmentStockTranContentWebView;

/**
 * Created by mSolo on 2014/8/21.
 */
public abstract class ArrayAdapterAbstractTranRec {

    public abstract int getOpenFlag();

    public abstract int getCloseFlag();

    public abstract int getHighestFlag();

    public abstract int getLowestFlag();

    public abstract String getOpenPrice();

    public abstract String getClosePrice();

    public abstract String getPeriodDecorator();

    public abstract String getPeriod();

    public abstract String getLastPrice();

    public abstract String getHighestPrice();

    public abstract String getLowestPrice();

    public abstract String getTranCount();

    public abstract String getPriceCount();

    public abstract String getVolume();

    public abstract String getMoney();


    public abstract String getEachPriceHeaderTitle();

    public abstract String[] getSingleMaxVolRecords();

    public abstract String[] getGeneralRecords();

    public abstract String[] getEachPriceRecords();

    public abstract void setupDetailArrayAdapter(int newId);

    public abstract ArrayAdapter<String> getSingleMaxVolArrayAdapter(int resourceId);

    public abstract ArrayAdapter<String> getEachPriceRecordsArrayAdapter(int resourceId, String[] contentArray);

    public abstract ArrayAdapter<String> getGeneralRecordsArrayAdapter(int resourceId);

    public abstract void visulizeSingleMaxVolRecords(FragmentStockTranContentWebView webViewMaker);

    public abstract void visulizeGeneralRecords(FragmentStockTranContentWebView webViewMaker);

    public abstract void visulizeEachPriceRecords(FragmentStockTranContentWebView webViewMaker);
/*
    public abstract void setupKChartView(KChartView kChartView);
*/
}
