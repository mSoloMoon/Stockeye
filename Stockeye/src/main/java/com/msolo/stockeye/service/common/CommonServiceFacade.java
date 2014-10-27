package com.msolo.stockeye.service.common;

import com.msolo.stockeye.setting.StockSetting;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mSolo on 2014/8/12.
 */
public class CommonServiceFacade implements CommonServiceFacadeImp {

    private static final CommonServiceFacade INSTANCE = new CommonServiceFacade();
    private NetworkResourceFetching networkResourcefetcher = NetworkResourceFetching.getInstance();
    private UtilCalendar utilCalendar = UtilCalendar.getInstance();
    private ExecutorService executorService = null;

    private CommonServiceFacade() {}

    public static CommonServiceFacade getInstance() {
        return INSTANCE;
    }

    @Override
    public ExecutorService getExecutorServiceCachedThreadPool() {

        if (null == executorService) {
            executorService = Executors.newCachedThreadPool();
        }

        return executorService;

    }

    @Override
    public boolean isNetworkConnected() {
        return networkResourcefetcher.isNetworkConnected();
    }

    @Override
    public String getHistoryLastdayPrice(String stockcode, String formatDate) {
        return networkResourcefetcher.getLastdayPriceByHtml(stockcode, formatDate);
    }

    /**
     *
     * @param requestCode [ 0:stockname; 1:realPrice; 2:lastdayPrice By json; ]
     * @param key
     * @return String
     *
     */
    @Override
    public String getStockResourceString(int requestCode, String key) {

        switch (requestCode) {
            case 0:
                return networkResourcefetcher.getStocknameByOkHttp(key);
            case 1:
                String rawRecord = networkResourcefetcher.getContentWithRealUrlByOkHttp(key);
                if (null == rawRecord) {
                    return null;
                }
                return priceFilter(rawRecord);
            case 2:
                rawRecord = networkResourcefetcher.getContentWithRealUrlByOkHttp(key);
                return lastdayPriceFilter(rawRecord);
            case 3:
                return null;
            default:
                return null;
        }

    }

    @Override
    public String downloadXlsContent(String stockcode, String formatdate) {

        String xlsContent = networkResourcefetcher.getXlsContentByHttpClient(stockcode, formatdate);

        if ( null != xlsContent ) {
            if ( !checkIfXlsCompleted(xlsContent) ) {
                return null;
            }

        }

        return xlsContent;

    }

    // Util Calendar
    // ------------------------------------------------------------------------

    @Override
    public boolean checkIsInSameQuarter(int firstYyyyMm, int secondYyyyMm) {

        int thisYear = firstYyyyMm/100;
        int thisQuarter = utilCalendar.getQuarter(firstYyyyMm - thisYear * 100);

        int thatYear = secondYyyyMm/100;
        int thatQuarter = utilCalendar.getQuarter(secondYyyyMm - thatYear * 100);

        if (thisYear != thatYear || thisQuarter != thatQuarter) {
            return false;
        }

        return true;

    }

    @Override
    public String[] getDateArrayOfWeek(String date) {
        return utilCalendar.getDateArrayOfWeek(date);
    }

    @Override
    public String getDateString(int dateIntFmt) {
        return utilCalendar.getDateString(dateIntFmt);
    }

    @Override
    public String[] getDownloadTransactionDateArray(String stockcode, String startDate) {

        boolean isNeedAddStartDate = false;

        String openSaleDate = networkResourcefetcher.getStockcodeOpenSaleDate(stockcode);

        StringBuilder updatedStartDate = new StringBuilder(startDate);

        // did the stock download before? if not,
        // then should compare the open sale date and the default date.
        if ( startDate.equals( StockSetting.getInstance().getStartDateForStock() ) ) {

            if (Integer.parseInt(openSaleDate.substring(0, 4)) > 2006) {
                updatedStartDate.replace(0, 8, openSaleDate.replace("-", ""));
                isNeedAddStartDate = true;
            }

        }

        return utilCalendar.getDownloadTransactionDateArray(isNeedAddStartDate, updatedStartDate.toString());

    }

    @Override
    public String getLastWeekStartDate(String date) {
        return utilCalendar.getLastWeekStartDate(date);
    }

    @Override
    public String[] getMonthArrayForTheQuarter(int yyyyMm) {

        int year = yyyyMm / 100;
        int month = yyyyMm - yyyyMm / 100 * 100;
        int quarter = utilCalendar.getQuarter(month);

        int[] monthArray = utilCalendar.getMonthArrayByQuarter(quarter);

        return new String[] {

                String.valueOf(year * 100 + monthArray[0]),
                String.valueOf(year * 100 + monthArray[1]),
                String.valueOf(year * 100 + monthArray[2])

        };

    }

    @Override
    public int getNextMonth(int monthIntFmt) {

        monthIntFmt += 1;
        int monthInt = (monthIntFmt - monthIntFmt/100 * 100);
        if ( 13 == monthInt ) {
            monthIntFmt = ((monthIntFmt - monthInt) / 100 + 1) * 100 + 1;
        }

        return monthIntFmt;

    }

    @Override
    public String getNextWeekStartDate(String date) {
        return utilCalendar.getNextWeekStartDate(date);
    }

    @Override
    public int getQuarter(int month) {
        return utilCalendar.getQuarter(month);
    }

    @Override
    public String getRefreshingDate() {
        return utilCalendar.getRefreshingDate();
    }

    @Override
    public String getToday() {
        return UtilCalendar.getInstance().getToday();
    }

    @Override
    public int getTransactionRefreshCounterByTenSecond() {

        if (utilCalendar.checkIsWorkDay()) {

            int todayTimeSecondElapse = utilCalendar.getTodayTimeElapseBySecond();

            // 09:25 - 00:00 = 33900000
            // 15:05 - 00:00 = 54300000
            if ( todayTimeSecondElapse < 54300000 && todayTimeSecondElapse > 33900000 ) {
                return ( 54300000 -  todayTimeSecondElapse) / (1000 * 10);      // 1000 * 10 : 10 second each count
            }

        }

        return 1;

    }

    @Override
    public int getWeekday(String date) {
        return UtilCalendar.getInstance().getWeekday(date);
    }

    @Override
    public int getWeekIndexOfYear(String date) {
        return utilCalendar.getWeekIndexOfYear(date);
    }


    // helper func
    // ------------------------------------------------------------------------

    private boolean checkIfXlsCompleted(String xlsContent) {

        String[] splitContent = xlsContent.split("\n");
        int size = splitContent.length;

        return size > 100;
        /*
        return size > 100 &&
               (( xlsContent.contains("14:54") || xlsContent.contains("14:53") || xlsContent.contains("14:49") || xlsContent.contains("14:46") ) &&
                ( xlsContent.contains("14:42") || xlsContent.contains("14:36") || xlsContent.contains("14:32") ) &&
                ( xlsContent.contains("14:26") || xlsContent.contains("14:23") || xlsContent.contains("14:16") ) &&
                ( xlsContent.contains("14:13") || xlsContent.contains("14:07") || xlsContent.contains("14:03") ) &&
                ( xlsContent.contains("13:53") || xlsContent.contains("13:48") || xlsContent.contains("13:46") ) &&
                ( xlsContent.contains("13:42") || xlsContent.contains("13:36") || xlsContent.contains("13:32") ) &&
                ( xlsContent.contains("13:26") || xlsContent.contains("13:23") || xlsContent.contains("13:16") ) &&
                ( xlsContent.contains("13:13") || xlsContent.contains("13:11") || xlsContent.contains("13:04") ) &&
                ( splitContent[size - 1].contains("9:25") || splitContent[size - 1].contains("9:30") ) );
        */

    }

    private String lastdayPriceFilter(String record) {
        return record.split(",")[2];
    }

    // real url return : example
    // var hq_str_sz002024="苏宁云商,7.04,7.05,7.16,7.27,7.04,7.15,7.16,153649583,1105032109.60,1330080, ...;
    // filter to "currentPrice_lastPrice_highestPrice_lowestPrice_open_vol_money"
    // after split, the related index will be : 3, 2, 4, 5, 1, 8, 9
    private String priceFilter(String record) {

        StringBuilder result = new StringBuilder();

        String[] itemOfRecordArray = record.split(",");

        result.append(itemOfRecordArray[3]).append("_")
              .append(itemOfRecordArray[2]).append("_")
              .append(itemOfRecordArray[4]).append("_")
              .append(itemOfRecordArray[5]).append("_")
              .append(itemOfRecordArray[1]).append("_")
              .append(itemOfRecordArray[8]).append("_")
              .append(itemOfRecordArray[9]);

        return result.toString();

    }

}
