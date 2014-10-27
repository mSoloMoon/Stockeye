package com.msolo.stockeye.service.common;

import java.util.concurrent.ExecutorService;

/**
 * Created by mSolo on 2014/8/12.
 */
public interface CommonServiceFacadeImp {

    public ExecutorService getExecutorServiceCachedThreadPool();

    public boolean isNetworkConnected();

    // -----------------------------------------
    public String getHistoryLastdayPrice(String stockcode, String formatDate);
    public String getStockResourceString(int requestCode, String key);
    public String downloadXlsContent(String stockcode, String formatdate);

    // Util : Calendar
    // ----------------------------------------------------------------------------------

    public boolean checkIsInSameQuarter(int firstYyyyMm, int SecondYyyyMm);
    public String[] getDateArrayOfWeek(String date);
    public String getDateString(int dateIntFmt);
    public String[] getDownloadTransactionDateArray(String stockcode, String startDate);
    public String getLastWeekStartDate(String date);
    public String[] getMonthArrayForTheQuarter(int yyyMm);
    public int getNextMonth(int monthIntFmt);
    public String getNextWeekStartDate(String date);
    public int getQuarter(int month);
    public String getRefreshingDate();
    public String getToday();
    public int getTransactionRefreshCounterByTenSecond();
    public int getWeekday(String date);
    public int getWeekIndexOfYear(String date);

}
