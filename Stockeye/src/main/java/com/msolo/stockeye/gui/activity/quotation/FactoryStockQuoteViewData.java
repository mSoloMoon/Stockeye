package com.msolo.stockeye.gui.activity.quotation;

/**
 * Created by mSolo on 2014/8/9.
 */

import static com.msolo.stockeye.setting.StockSetting.StockQuotationStyleProductType;

public class FactoryStockQuoteViewData {

    private FactoryStockQuoteViewData() {}

    public static String[] getStockQuoteViewDataProduct(StockQuotationStyleProductType productType, String[] dataArray) {

        switch (productType) {
            case Percentage:
                return new ProductPercentageFormatData(dataArray).getProduct();
            case Offset:
                break;
            case Highest:
                break;
            case Lowest:
                break;
            case Open:
                break;
            case Last:
                break;
            case Voluem:
                break;
            case Moeny:
                break;
            default:
                break;

        }

        return null;

    }

}
