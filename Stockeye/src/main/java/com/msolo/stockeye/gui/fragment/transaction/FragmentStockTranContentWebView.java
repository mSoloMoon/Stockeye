package com.msolo.stockeye.gui.fragment.transaction;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.msolo.stockeye.StockeyeApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mSolo on 2014/8/22.
 */
public class FragmentStockTranContentWebView {

    private boolean isOnload;

    private String chartPieTitle;
    private String chartLineTitle;
    private ArrayList<String> xPieRecs;
    private ArrayList<String> yPieRecs;
    private ArrayList<String> xLineRecs;
    private ArrayList<String> yLineRecs;

    private WebView webView;

    public FragmentStockTranContentWebView(WebView wv) {

        webView = wv;

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });

        if (StockeyeApp.isLargeScreen) {
            webView.loadUrl("file:///android_asset/chart_large.html");
        } else {
            webView.loadUrl("file:///android_asset/chart.html");
        }

        isOnload = false;
        xPieRecs = new ArrayList<String>();
        yPieRecs = new ArrayList<String>();
        xLineRecs = new ArrayList<String>();
        yLineRecs = new ArrayList<String>();

    }

    public WebView getWebView() {
        return webView;
    }

    public void formatMaxVolSingleRecs(long sumVol, String[] singleRecs) {

        xPieRecs.clear();
        yPieRecs.clear();

        Map<String, Long> mapPriceVol = new LinkedHashMap<String, Long>();
        String[] firstSingleRec = singleRecs[2].split("\t");
        mapPriceVol.put(firstSingleRec[1], Long.valueOf(firstSingleRec[2]));

        int size = singleRecs.length;
        for (int i=3; i<size; i++) {
            String[] singleRecComb = singleRecs[i].split("\t");
            if ( mapPriceVol.containsKey(singleRecComb[1]) ) {
                mapPriceVol.put( singleRecComb[1], mapPriceVol.get(singleRecComb[1]) + Long.valueOf(singleRecComb[2]) );
            } else {
                mapPriceVol.put( singleRecComb[1], Long.valueOf(singleRecComb[2]) );
            }
        }

        Iterator<String> it = mapPriceVol.keySet().iterator();
        while ( it.hasNext() ) {
            String key = it.next();
            String ratio = String.format("%.1f", mapPriceVol.get(key).floatValue() / sumVol * 100);
            xPieRecs.add(key);
            yPieRecs.add(ratio);
        }

        chartPieTitle = "34笔最大交易占比图";

    }

    public void formatMaxVolSingleRecsForMQY(long sumVol, String[] singleRecs) {

        xPieRecs.clear();
        yPieRecs.clear();

        Map<String, Long> mapPriceVol = new LinkedHashMap<String, Long>();
        String[] firstSingleRec = singleRecs[2].split("\t");
        mapPriceVol.put(firstSingleRec[1], Long.valueOf(firstSingleRec[2]));

        int highestPrice = 0;
        int lowestPrice = 10000;
        int size = singleRecs.length;
        for (int i=3; i<size; i++) {
            String[] singleRecComb = singleRecs[i].split("\t");
            if ( mapPriceVol.containsKey(singleRecComb[1]) ) {
                mapPriceVol.put( singleRecComb[1], mapPriceVol.get(singleRecComb[1]) + Long.valueOf(singleRecComb[2]) );
            } else {

                int price = Integer.parseInt(singleRecComb[1].replace(".", ""));
                if (price > highestPrice) {
                    highestPrice = price;
                }
                if (price < lowestPrice) {
                    lowestPrice = price;
                }

                mapPriceVol.put( singleRecComb[1], Long.valueOf(singleRecComb[2]) );
            }
        }

        int[] priceSplitForPie;
        long[] volsForPie;

        int divider = 5;
        int splitPriceVal = (highestPrice - lowestPrice + 1) / divider;
        int remainder = ((highestPrice - lowestPrice + 1)) % divider;
        if (remainder >= divider - 2) {
            priceSplitForPie = new int[7];
            priceSplitForPie[0] = lowestPrice;
            priceSplitForPie[1] = priceSplitForPie[0] + splitPriceVal;
            priceSplitForPie[2] = priceSplitForPie[1] + splitPriceVal;
            priceSplitForPie[3] = priceSplitForPie[2] + splitPriceVal;
            priceSplitForPie[4] = priceSplitForPie[3] + splitPriceVal;
            priceSplitForPie[5] = priceSplitForPie[4] + remainder;
            priceSplitForPie[6] = highestPrice;

            volsForPie = new long[6];

        } else {
            priceSplitForPie = new int[6];
            priceSplitForPie[0] = lowestPrice;
            priceSplitForPie[1] = priceSplitForPie[0] + splitPriceVal;
            priceSplitForPie[2] = priceSplitForPie[1] + splitPriceVal;
            priceSplitForPie[3] = priceSplitForPie[2] + splitPriceVal;
            priceSplitForPie[4] = priceSplitForPie[3] + splitPriceVal;
            priceSplitForPie[5] = highestPrice;

            volsForPie = new long[5];
        }

        Iterator<String> it = mapPriceVol.keySet().iterator();
        while ( it.hasNext() ) {
            String price = it.next();

            int theCmpPrice = Integer.parseInt(price.replace(".", ""));

            if ( theCmpPrice >= priceSplitForPie[0] && theCmpPrice < priceSplitForPie[1] ) {
                volsForPie[0] += mapPriceVol.get(price);
                continue;
            }

            if ( theCmpPrice >= priceSplitForPie[1] && theCmpPrice < priceSplitForPie[2] ) {
                volsForPie[1] += mapPriceVol.get(price);
                continue;
            }

            if ( theCmpPrice >= priceSplitForPie[2] && theCmpPrice < priceSplitForPie[3] ) {
                volsForPie[2] += mapPriceVol.get(price);
                continue;
            }

            if ( theCmpPrice >= priceSplitForPie[3] && theCmpPrice < priceSplitForPie[4] ) {
                volsForPie[3] += mapPriceVol.get(price);
                continue;
            }

            if ( theCmpPrice >= priceSplitForPie[4] && theCmpPrice <= priceSplitForPie[5] ) {
                volsForPie[4] += mapPriceVol.get(price);
                continue;
            }

            if ( theCmpPrice > priceSplitForPie[5] && theCmpPrice <= priceSplitForPie[6] ) {
                volsForPie[5] += mapPriceVol.get(price);
            }

        }

        xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[0])/100, ((float)priceSplitForPie[1] - 1)/100 ) );
        xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[1])/100, ((float)priceSplitForPie[2] - 1)/100 ) );
        xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[2])/100, ((float)priceSplitForPie[3] - 1)/100 ) );
        xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[3])/100, ((float)priceSplitForPie[4] - 1)/100 ) );

        yPieRecs.add( String.format( "%.1f", ((float)volsForPie[0]) / sumVol * 100) );
        yPieRecs.add( String.format( "%.1f", ((float)volsForPie[1]) / sumVol * 100) );
        yPieRecs.add( String.format( "%.1f", ((float)volsForPie[2]) / sumVol * 100) );
        yPieRecs.add( String.format( "%.1f", ((float)volsForPie[3]) / sumVol * 100) );

        if (volsForPie.length > 5) {
            xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[4])/100, ((float)priceSplitForPie[5] - 1)/100 ) );
            xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[5])/100, ((float)priceSplitForPie[6])/100 ) );
            yPieRecs.add( String.format( "%.1f", ((float)volsForPie[4]) / sumVol * 100) );
            yPieRecs.add( String.format( "%.1f", ((float)volsForPie[5]) / sumVol * 100) );

        } else {
            xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[4])/100, ((float)priceSplitForPie[5])/100 ) );
            yPieRecs.add( String.format( "%.1f", ((float)volsForPie[4]) / sumVol * 100) );
        }

        chartPieTitle = "34笔最大交易占比图";

    }

    public void formatFifteenRecsForPieChart(long totalVol, String[] fifteenRecs) {

        xPieRecs.clear();
        yPieRecs.clear();

        String[] fifteenTimesName = new String[]{"9:30, ", "9:45, ", "10:00, ", "10:15, ", "10:30, ", "10:45, ", "11:00, ", "11:15, ",
                "1:00, ", "1:15, ", "1:30, ", "1:45, ", "2:00, ", "2:15, ", "2:30, ", "2:45, "};

        int size = fifteenRecs.length;
        for(int i=1; i<size; i++) {

            String[] partOfRec = fifteenRecs[i].split("\t");
            xPieRecs.add(fifteenTimesName[i-1] + partOfRec[2]);
            yPieRecs.add(String.format("%.1f", ((float) Long.parseLong(partOfRec[3])) / totalVol * 100));

        }

        chartPieTitle = "15分钟内交易占比图";

    }

    public void formatFifteenRecsForLineChart(int openPrice, int closePrice, int highestPrice, int lowestPrice, String[] fifteenRecs) {

        xLineRecs.clear();
        yLineRecs.clear();

        xLineRecs.add("0");
        yLineRecs.add(String.format("%.2f", ((float)openPrice) / 100 ));

        int size = fifteenRecs.length;
        int counter = 1;
        for(int i=1; i<size; i++) {

            String[] partOfRec = fifteenRecs[i].split("\t");
            xLineRecs.add(String.valueOf(counter));
            yLineRecs.add(partOfRec[0].split("\n")[1]);
            counter++;
            xLineRecs.add(String.valueOf(counter));
            yLineRecs.add(partOfRec[1].split("\n")[1]);

        }

        xLineRecs.add(String.valueOf(counter));
        yLineRecs.add(String.format("%.2f", ((float)closePrice) / 100 ));

        chartLineTitle = "15分钟内交易价格趋势图=仿分时图="+ String.format("%.2f=%.2f", ((float)(highestPrice + 10)) / 100, ((float)lowestPrice) / 100);

    }

    public void formatEachdayRecs(long totalVol, String[] eachdayRecs) {

        xPieRecs.clear();
        yPieRecs.clear();

        int size = eachdayRecs.length;
        for(int i=0; i<size; i++) {

            String[] partOfRec = eachdayRecs[i].split("\t");
            xPieRecs.add(partOfRec[0]);
            yPieRecs.add( String.format("%.1f", Float.parseFloat(partOfRec[3]) / totalVol * 100) );

        }

        chartPieTitle = "价格成交量占比图";

    }

    public void formatEachdayRecsForQuarter(long totalVol, String[] eachdayRecs) {

        xPieRecs.clear();
        yPieRecs.clear();

        StringBuilder dateStrBuilder = new StringBuilder();

        int size = eachdayRecs.length;
        int counter = 4;
        float sumVol = 0.00f;
        for(int i=0; i<size; i++) {

            counter--;

            String[] partOfRec = eachdayRecs[i].split("\t");

            if ( dateStrBuilder.length() == 0 ) {
                dateStrBuilder.append(partOfRec[0]);
            }

            if (counter == 0) {
                xPieRecs.add( new StringBuilder().append(dateStrBuilder.substring(6, 10)).append("~").append(partOfRec[0].substring(6, 10)).toString() );
                yPieRecs.add( String.format("%.1f", sumVol / totalVol * 100) );
                sumVol = 0.00f;
                dateStrBuilder.delete(0, 10);
                counter = 4;
                continue;
            }

            sumVol += Float.parseFloat(partOfRec[3]);

        }

        if (counter != 4) {
            xPieRecs.add( new StringBuilder().append(dateStrBuilder.substring(6, 10)).append("~").append(eachdayRecs[size - 1].split("\t")[0].substring(6, 10)).toString() );
            yPieRecs.add( String.format("%.1f", sumVol / totalVol * 100) );
        }

        chartPieTitle = "价格成交量占比图";

    }

    public void formatEachdayRecsForYear(long totalVol, String[] eachdayRecs) {

        xPieRecs.clear();
        yPieRecs.clear();

        StringBuilder dateStrBuilder = new StringBuilder(eachdayRecs[0].split("\t")[0].substring(0, 7));

        int size = eachdayRecs.length;
        float volOfMonth = 0.00f;
        for(int i=0; i<size; i++) {

            String[] partOfRec = eachdayRecs[i].split("\t");

            if (!dateStrBuilder.toString().equals(partOfRec[0].substring(0, 7))) {
                xPieRecs.add( dateStrBuilder.toString() );
                yPieRecs.add( String.format("%.1f", volOfMonth / totalVol * 100) );

                dateStrBuilder.replace(0, 7, partOfRec[0].substring(0, 7));
                volOfMonth = 0.00f;
            }

            volOfMonth += Float.parseFloat(partOfRec[3]);

        }

        xPieRecs.add( dateStrBuilder.toString() );
        yPieRecs.add( String.format("%.1f", volOfMonth / totalVol * 100) );

        chartPieTitle = "价格成交量占比图";

    }

    public void formatEachPriceRecs(long totalVol, String[] eachPriceRecs) {

        xPieRecs.clear();
        yPieRecs.clear();

        for(String eachPriceRec : eachPriceRecs) {

            String[] partOfRec = eachPriceRec.split("\t");
            xPieRecs.add(partOfRec[1] + ", " + partOfRec[2]);
            yPieRecs.add( String.format("%.1f", Float.parseFloat(partOfRec[3].split("\n")[0]) / totalVol * 100) );

        }

        chartPieTitle = "价格成交量占比图";
    }

    public void formatWMQYEachPriceRecs(int highestPrice, int lowestPrice, long totalVol, String[] eachPriceRecs) {

        int[] priceSplitForPie;
        long[] volsForPie;

        int divider = 5;
        int splitPriceVal = (highestPrice - lowestPrice + 1) / divider;
        int remainder = ((highestPrice - lowestPrice + 1)) % divider;
        if (remainder >= divider - 2) {
            priceSplitForPie = new int[7];
            priceSplitForPie[0] = lowestPrice;
            priceSplitForPie[1] = priceSplitForPie[0] + splitPriceVal;
            priceSplitForPie[2] = priceSplitForPie[1] + splitPriceVal;
            priceSplitForPie[3] = priceSplitForPie[2] + splitPriceVal;
            priceSplitForPie[4] = priceSplitForPie[3] + splitPriceVal;
            priceSplitForPie[5] = priceSplitForPie[4] + remainder;
            priceSplitForPie[6] = highestPrice;

            volsForPie = new long[6];

        } else {
            priceSplitForPie = new int[6];
            priceSplitForPie[0] = lowestPrice;
            priceSplitForPie[1] = priceSplitForPie[0] + splitPriceVal;
            priceSplitForPie[2] = priceSplitForPie[1] + splitPriceVal;
            priceSplitForPie[3] = priceSplitForPie[2] + splitPriceVal;
            priceSplitForPie[4] = priceSplitForPie[3] + splitPriceVal;
            priceSplitForPie[5] = highestPrice;

            volsForPie = new long[5];
        }

        for(String eachPriceRec : eachPriceRecs) {

            String[] partOfRec = eachPriceRec.split("\t");

            int theCmpPrice = 0;
            try {
                theCmpPrice = Integer.parseInt(partOfRec[1].replace(".", ""));
            } catch (NumberFormatException e) {
                StringBuilder theCmpPriceBuilder = new StringBuilder();
                for(int i=0; i<partOfRec[1].length(); i++) {
                    if (partOfRec[1].charAt(i) >= 48 && partOfRec[1].charAt(i) <= 57) {
                        theCmpPriceBuilder.append(partOfRec[1].charAt(i));
                    }
                }
                theCmpPrice = Integer.parseInt(theCmpPriceBuilder.toString());
            }

            if ( theCmpPrice >= priceSplitForPie[0] && theCmpPrice < priceSplitForPie[1] ) {
                volsForPie[0] += Long.parseLong(partOfRec[3]);
                continue;
            }

            if ( theCmpPrice >= priceSplitForPie[1] && theCmpPrice < priceSplitForPie[2] ) {
                volsForPie[1] += Long.parseLong(partOfRec[3]);
                continue;
            }

            if ( theCmpPrice >= priceSplitForPie[2] && theCmpPrice < priceSplitForPie[3] ) {
                volsForPie[2] += Long.parseLong(partOfRec[3]);
                continue;
            }

            if ( theCmpPrice >= priceSplitForPie[3] && theCmpPrice < priceSplitForPie[4] ) {
                volsForPie[3] += Long.parseLong(partOfRec[3]);
                continue;
            }

            if ( theCmpPrice >= priceSplitForPie[4] && theCmpPrice <= priceSplitForPie[5] ) {
                volsForPie[4] += Long.parseLong(partOfRec[3]);
                continue;
            }

            if ( theCmpPrice > priceSplitForPie[5] && theCmpPrice <= priceSplitForPie[6] ) {
                volsForPie[5] += Long.parseLong(partOfRec[3]);
            }

        }

        xPieRecs.clear();
        yPieRecs.clear();

        xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[0])/100, ((float)priceSplitForPie[1] - 1)/100 ) );
        xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[1])/100, ((float)priceSplitForPie[2] - 1)/100 ) );
        xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[2])/100, ((float)priceSplitForPie[3] - 1)/100 ) );
        xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[3])/100, ((float)priceSplitForPie[4] - 1)/100 ) );

        yPieRecs.add( String.format( "%.1f", ((float)volsForPie[0]) / totalVol * 100) );
        yPieRecs.add( String.format( "%.1f", ((float)volsForPie[1]) / totalVol * 100) );
        yPieRecs.add( String.format( "%.1f", ((float)volsForPie[2]) / totalVol * 100) );
        yPieRecs.add( String.format( "%.1f", ((float)volsForPie[3]) / totalVol * 100) );

        if (volsForPie.length > 5) {
            xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[4])/100, ((float)priceSplitForPie[5] - 1)/100 ) );
            xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[5])/100, ((float)priceSplitForPie[6])/100 ) );
            yPieRecs.add( String.format( "%.1f", ((float)volsForPie[4]) / totalVol * 100) );
            yPieRecs.add( String.format( "%.1f", ((float)volsForPie[5]) / totalVol * 100) );

        } else {
            xPieRecs.add( String.format( "%.2f~%.2f", ((float)priceSplitForPie[4])/100, ((float)priceSplitForPie[5])/100 ) );
            yPieRecs.add( String.format( "%.1f", ((float)volsForPie[4]) / totalVol * 100) );
        }

        chartPieTitle = "价格成交量占比图";

    }

    // jsData : title=price1=ratio1=...priceN=ratioN
    public void loadChartForWebView() {

        StringBuilder jsPieData = new StringBuilder();
        jsPieData.append(chartPieTitle);

        int size = xPieRecs.size();
        for (int i=0; i<size; i++) {
            jsPieData.append("=").append(xPieRecs.get(i)).append("=").append(yPieRecs.get(i));
        }

        StringBuilder jsLineData = new StringBuilder();
        jsLineData.append(chartLineTitle);

        size = xLineRecs.size();
        for (int i=0; i<size; i++) {
            jsLineData.append("=").append(xLineRecs.get(i)).append("=").append(yLineRecs.get(i));
        }

        webView.loadUrl("javascript: genPieChartFunc('" + jsPieData.toString() + "'); " +
                "genLineChartFunc('" + jsLineData.toString() + "'); ");
    }

    public void loadOnlyPieChartForWebView() {

        StringBuilder jsData = new StringBuilder();
        jsData.append(chartPieTitle);

        int size = xPieRecs.size();
        for (int i=0; i<size; i++) {
            jsData.append("=").append(xPieRecs.get(i)).append("=").append(yPieRecs.get(i));
        }

        if (!isOnload) {
            webView.loadUrl("javascript:window.onload=function(){ genPieChartFunc('" + jsData.toString() + "'); genNullLineChartFunc(); }");
            isOnload = true;
        } else {
            webView.loadUrl("javascript: genPieChartFunc('" + jsData.toString() + "'); genNullLineChartFunc(); ");
        }
    }

}
