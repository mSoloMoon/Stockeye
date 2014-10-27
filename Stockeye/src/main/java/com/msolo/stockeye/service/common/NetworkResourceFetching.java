package com.msolo.stockeye.service.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.msolo.stockeye.StockeyeApp;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by mSolo on 2014/8/9.
 */

public class NetworkResourceFetching {

    // real url return : example
    // var hq_str_sz002024="苏宁云商,7.04,7.05,7.16,7.27,7.04,7.15,7.16,153649583,1105032109.60,1330080, ...;
    // filter to "currentPrice_lastPrice_highestPrice_lowestPrice_open_vol_money"
    // after split, the related index will be : 3, 2, 4, 5, 1, 8, 9
    private static final String PREFIX_REAL_URL_STOCK_RECORD = "http://hq.sinajs.cn/?_=0.461174342315644&list=";
    private static final String TEMPLATE_XLS_URL = "http://market.finance.sina.com.cn/downxls.php?date=2000-01-01&symbol=sx000000";
    private static final String TEMPLATE_HISTORY_URL = "http://vip.stock.finance.sina.com.cn/quotes_service" +
            "/view/vMS_tradehistory.php?symbol=sx000000&date=2000-01-01";
    private static final String PREFIX_OPENSALE_DATE_URL = "http://stockdata.stock.hexun.com/gszl/s";

    private static final NetworkResourceFetching INSTANCE = new NetworkResourceFetching();
    private static final OkHttpClient okHttpClient = new OkHttpClient();

    private NetworkResourceFetching() {}

    protected boolean isNetworkConnected() {

        ConnectivityManager mConnectivityManager = (ConnectivityManager) StockeyeApp.appContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }

        return false;
    }

    protected static NetworkResourceFetching getInstance() {
        return INSTANCE;
    }

    protected String getContentWithRealUrlByOkHttp(String stockcode) {

        int counter = 10;
        String content = null;

        Request request = new Request.Builder().url(PREFIX_REAL_URL_STOCK_RECORD + stockcode).build();
        Response response = null;
        while (counter > 0) {

            try {
                response = okHttpClient.newCall(request).execute();
                content = response.body().string();
            } catch (IOException e) {
                //
            }

            if (content != null) {
                return content;
            }
            counter--;

        }

        return null;

    }

    protected String getLastdayPriceByHtml(String stockcode, String formatDate) {

        String webUrl = new StringBuilder(TEMPLATE_HISTORY_URL).replace(85, 93, stockcode)
                .replace(99, 109, formatDate)
                .toString();

        try {
            Document doc = Jsoup.connect(webUrl).timeout(5000).get();

            if (doc == null) {
                return null;
            }

            Element divMain = doc.getElementsByClass("main").first();
            if (divMain != null && divMain.text().equals("输入的日期为非交易日期")) {
                return null;
            }

            Element divsTranInfo = doc.getElementById("quote_area");
            if (divsTranInfo == null) {
                return null;
            }

            Elements tds = divsTranInfo.getElementsByTag("td");
            if (tds.get(2).text().equals("0.00") && tds.get(5).text().equals("0.00")) {
                return null;
            }

            return tds.get(5).text();

        } catch (IOException e) {
            // stub
        }

        return null;

    }

    protected String getStockcodeOpenSaleDate(String stockcode) {

        String openSaleDateUrl = new StringBuilder(PREFIX_OPENSALE_DATE_URL).append(stockcode.substring(2)).append(".shtml").toString();

        try {
            Document doc = Jsoup.connect(openSaleDateUrl).timeout(5000).get();
            Elements tds = doc.getElementsByClass("tab_xtable")
                    .get(2)
                    .getElementsByTag("td");

            return tds.get(3).text();

        } catch (IOException ioe) {

        }

        return null;

    }

    protected String getStocknameByOkHttp(String stockcode) {

        String rawStockRealRecord = getContentWithRealUrlByOkHttp(stockcode);

        if ( rawStockRealRecord != null ) {

            String stockname = rawStockRealRecord.split("\"")[1].split(",")[0].replace(" ", "");
            if ( !stockname.isEmpty() && !stockname.endsWith("\";") ) {
                return stockname;
            }

        }

        return null;

    }

    protected String getXlsContentByHttpClient(String stockcode, String formatDate) {

        String xlsUrl = new StringBuilder(TEMPLATE_XLS_URL).replace(51, 61, formatDate)
                .replace(69, 77, stockcode)
                .toString();

        BufferedReader in = null;
        HttpURLConnection conn = null;
        String content = null;

        int counter = 10;
        while (counter > 0) {
            try {

                conn = (HttpURLConnection) new URL(xlsUrl).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                conn.connect();

                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                if (in == null) {
                    counter--;
                    continue;
                }

                StringBuilder sb = new StringBuilder();
                String line = "";
                String NL = System.getProperty("line.separator");

                while ((line = in.readLine()) != null) {
                    sb.append(line).append(NL);
                }

                in.close();
                content = sb.toString();

                return content;

            } catch (MalformedURLException me) {
            } catch (SocketTimeoutException te) {
            } catch (IOException ioe) {
            } finally {
                conn.disconnect();
            }
        }

        return content;

    }

}
