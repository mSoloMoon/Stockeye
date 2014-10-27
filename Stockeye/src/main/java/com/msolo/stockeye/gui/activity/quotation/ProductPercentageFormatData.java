package com.msolo.stockeye.gui.activity.quotation;

/**
 * Created by mSolo on 2014/8/9.
 */
public class ProductPercentageFormatData implements ProductStockQuoteDataFormatImp {

    private String[] product = null;

    private ProductPercentageFormatData() {}

    public ProductPercentageFormatData(String[] sourceDataArray) {

        int sourceDataArraySize = sourceDataArray.length;

        product = new String[sourceDataArraySize];

        String[] firstRecordDataArray = sourceDataArray[0].split("_");
        String[] secondRecordDataArray = sourceDataArray[1].split("_");

        product[0] = String.format("%s", formatData(firstRecordDataArray));
        product[1] = String.format("%s", formatData(secondRecordDataArray));

        for (int i=2; i<sourceDataArraySize; i++) {

            String[] recordDataAraay = sourceDataArray[i].split("_");
            product[i] = formatData(recordDataAraay);

        }

    }

    @Override
    public String[] getProduct() {
        return product;
    }

    private String formatData(String[] itemrray) {

        int currentPrice = Integer.parseInt(itemrray[0].replace(".", ""));
        int lastPrice = Integer.parseInt(itemrray[1].replace(".", ""));

        float percentage = 0.00f;
        char sign = '+';

        if (currentPrice > lastPrice) {
            percentage = (float)(currentPrice - lastPrice) / lastPrice * 100;
        } else if (currentPrice < lastPrice) {
            percentage = (float)(lastPrice - currentPrice) / lastPrice * 100;
            sign = '-';
        } else {
            sign = '#';
        }

        return String.format("%c%5.2f (%2.2f%%)", sign, Float.parseFloat(itemrray[0]), percentage);

    }

}
