package com.msolo.stockeye.setting;

import com.msolo.stockeye.StockeyeApp;

/**
 * Created by mSolo on 2014/8/8.
 */
public final class StockSetting {

    private StockQuotationStyleProductType defaultStockQuotationStyleProductType = null;

    public enum StockQuotationStyleProductType{Percentage, Offset, Highest, Lowest, Open, Last, Voluem, Moeny};

    private static final StockSetting INSTANCE = new StockSetting();

    private StockSetting() {
        defaultStockQuotationStyleProductType = StockQuotationStyleProductType.Percentage;
    }

    public static StockSetting getInstance() {
        return INSTANCE;
    }

    public String getStartDateForStock() {
        return StockeyeApp.appSharedPref.getString("stock_startdate", "20060101");
    }

    public StockQuotationStyleProductType getDefaultStockQuotationStyle() {
        return defaultStockQuotationStyleProductType;
    }

    public boolean getWakefulFlag() {
        return StockeyeApp.appSharedPref.getBoolean("is_wakeful", true);
    }

}
