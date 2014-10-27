package com.msolo.stockeye.business.stock;

import android.os.AsyncTask;

import com.msolo.stockeye.business.stock.mine.DataMiningFacade;
import com.msolo.stockeye.service.common.CommonServiceFacade;
import com.msolo.stockeye.service.db.build.DatabaseStoreServiceFacade;

import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/18.
 */
class BuildTransactionDataWork implements Runnable {

    private DownloadStockTransactionDataService downloadService = null;
    private String stockcode = null;
    private CommonServiceFacade commonServiceFacade = null;
    private DatabaseStoreServiceFacade storeService = null;

    private BuildTransactionDataWork() {}

    BuildTransactionDataWork(DownloadStockTransactionDataService s) {
        downloadService = s;
        commonServiceFacade = CommonServiceFacade.getInstance();
        storeService = new DatabaseStoreServiceFacade();
    }

    @Override
    public void run() {

        DownloadStockTransactionDataController controller = DownloadStockTransactionDataController.getInstance();

        while(true) {

            String[] stock = controller.getOneStockcodeToDownload();
            stockcode = stock[0];

            String[] updateDateArray = commonServiceFacade.getDownloadTransactionDateArray(stockcode, stock[2]);

            downloadService.getBuildTransactionDataWorkNotification().setupNotification(stock[1], stock[2]);
            if (updateDateArray.length == 1) {
                downloadService.getBuildTransactionDataWorkNotification().updateNotification(100, 100, stock[2]);
                return ;
            }

            download(updateDateArray);

            if ( !controller.isActivityExist() && controller.isDownloadStockcodeListEmpty() ) {
                break;
            }

        }

        downloadService.stopSelf();

    }

    private void download( String[] tranDateArray ) {

        int size = tranDateArray.length;

        XlsStockTransactionDataAnalysis xlsAnalyzer = new XlsStockTransactionDataAnalysis();

        if (size > 248) {
            storeService.prepareStockTranRecordCycleCache(248, size / 10);
        } else {
            storeService.prepareStockTranRecordCycleCache(size, 20);
        }

        StringBuilder cmpDate = new StringBuilder().append(tranDateArray[0]);
        int yearOfcmpDate = Integer.parseInt( tranDateArray[0].substring(0, 4) );

        for (int i=0; i<size - 1; i++) {

            int currentYearOfCmpDate = Integer.parseInt( tranDateArray[i].substring(0, 4) );
            if ( currentYearOfCmpDate > yearOfcmpDate ) {

                downloadService.getBuildTransactionDataWorkNotification().updateNotification(i - 1, size, tranDateArray[i]);

                storeService.submitCachesOfStockDailyRecord(stockcode);

                yearOfcmpDate = currentYearOfCmpDate;
                cmpDate.replace(0, 10, tranDateArray[i]);

            }

            handleAndCacheTranDailyRecord(tranDateArray[i], xlsAnalyzer);

        }

        storeService.submitCachesOfStockDailyRecord(stockcode);
        downloadService.getBuildTransactionDataWorkNotification().updateNotification(size, size, tranDateArray[size - 2]);

        String startDate = tranDateArray[0].replace("-", "").substring(0, 6);
        String nextDateOfRefreshDate = tranDateArray[size - 1].replace("-", "").substring(0, 6);
        new HandleMonthQuarterYearlyRecordRunnable()
                .executeOnExecutor(CommonServiceFacade.getInstance().getExecutorServiceCachedThreadPool(), startDate, nextDateOfRefreshDate);

    }

    private String getLastdayPrice(String stockcode, String formatDate) {

        String lastDayPrice = formatDate.equals(commonServiceFacade.getToday())
                ? commonServiceFacade.getStockResourceString(2, stockcode)
                : commonServiceFacade.getHistoryLastdayPrice(stockcode, formatDate);

        if (lastDayPrice == null) {
            lastDayPrice = "0.00";
        }

        return lastDayPrice.replace(".", "");

    }

    /**
     * analyze and store daily record
     *
     * @param tranDate
     * @param xlsAnalyzer
     */
    private void handleAndCacheTranDailyRecord(String tranDate, XlsStockTransactionDataAnalysis xlsAnalyzer) {

        String[] dailyRecordItemArray = new String[16];
        dailyRecordItemArray[0] = tranDate.replace("-", "");
        dailyRecordItemArray[1] = String.valueOf( commonServiceFacade.getWeekday(dailyRecordItemArray[0]) );
        dailyRecordItemArray[2] = getLastdayPrice(stockcode, tranDate);

        String xlsContent = commonServiceFacade.downloadXlsContent(stockcode, tranDate);
        if (xlsContent != null) {

            xlsAnalyzer.analyzeXlsContentToObjTranDailyRec(xlsContent, dailyRecordItemArray);
            storeService.cacheOneStockDailyRecord(dailyRecordItemArray);

        }

    }

    private void handleAndCacheTranMonthlyRecord(int yyyyMMOfStartDate, int yyyyMMOfNextDateOfRefreshDate, DataMiningFacade dataMiningFacade) {

        dataMiningFacade.setUpDataMiningAnalyzer(2);

        StringBuilder monthIdRangeArrayBuilder = new StringBuilder();
        while ( yyyyMMOfNextDateOfRefreshDate != yyyyMMOfStartDate ) {

            monthIdRangeArrayBuilder.append( String.format("%d%s%d%s=", yyyyMMOfStartDate, "00", yyyyMMOfStartDate, "32") );
            yyyyMMOfStartDate = commonServiceFacade.getNextMonth(yyyyMMOfStartDate);

        }

        monthIdRangeArrayBuilder.deleteCharAt( monthIdRangeArrayBuilder.length() - 1 );

        ArrayList<String[]> monthlyRecordItemsArrayList = dataMiningFacade.analyze(stockcode, monthIdRangeArrayBuilder.toString().split("="), null);
        if ( !monthlyRecordItemsArrayList.isEmpty() ) {
            for ( String[] itemArray : monthlyRecordItemsArrayList ) {
                storeService.cacheOneStockMQYRecord(itemArray);
            }
        }

        storeService.submitStockMQYRecords(stockcode);

    }

    private void handleAndCacheTranQuarterlyRecord(int yyyyMMOfStartDate, int yyyyMMOfNextDateOfRefreshDate, DataMiningFacade dataMiningFacade) {

        dataMiningFacade.setUpDataMiningAnalyzer(3);

        String[] yyyyMmArrayForQuarter = null;

        StringBuilder quarterIdRangeArrayBuilder = new StringBuilder();
        while ( !commonServiceFacade.checkIsInSameQuarter(yyyyMMOfStartDate, yyyyMMOfNextDateOfRefreshDate) ) {

            yyyyMmArrayForQuarter = commonServiceFacade.getMonthArrayForTheQuarter(yyyyMMOfStartDate);
            quarterIdRangeArrayBuilder.append( String.format("%s%s=", yyyyMmArrayForQuarter[0], yyyyMmArrayForQuarter[2]) );
            yyyyMMOfStartDate = commonServiceFacade.getNextMonth(Integer.parseInt(yyyyMmArrayForQuarter[2]));

        }

        quarterIdRangeArrayBuilder.deleteCharAt( quarterIdRangeArrayBuilder.length() - 1 );

        ArrayList<String[]> quarterlyRecordItemsArrayList = dataMiningFacade.analyze(stockcode, quarterIdRangeArrayBuilder.toString().split("="), null);
        if ( !quarterlyRecordItemsArrayList.isEmpty() ) {
            for ( String[] itemArray : quarterlyRecordItemsArrayList ) {
                storeService.cacheOneStockMQYRecord(itemArray);
            }
        }

        storeService.submitStockMQYRecords(stockcode);

    }

    private void handleAndCacheTranYearlyRecord(int startYear, int latestYear, DataMiningFacade dataMiningFacade) {

        dataMiningFacade.setUpDataMiningAnalyzer(4);

        StringBuilder yearIdRangeArrayBuilder = new StringBuilder();
        while (startYear != latestYear) {

            int yearPre = startYear * 10;
            int[] quarterArrayForYear = new int[]{yearPre + 1, yearPre + 2, yearPre + 3, yearPre + 4};
            String yearIdRange = String.format("%d%d", quarterArrayForYear[0], quarterArrayForYear[3]);

            yearIdRangeArrayBuilder.append(yearIdRange).append("=");

            startYear++;

        }

        yearIdRangeArrayBuilder.deleteCharAt(yearIdRangeArrayBuilder.length() - 1);

        ArrayList<String[]> yearlyRecordItemsArrayList = dataMiningFacade.analyze(stockcode, yearIdRangeArrayBuilder.toString().split("="), null);
        if ( !yearlyRecordItemsArrayList.isEmpty() ) {
            for ( String[] itemArray : yearlyRecordItemsArrayList ) {
                storeService.cacheOneStockMQYRecord(itemArray);
            }
        }


        storeService.submitStockMQYRecords(stockcode);

    }

    class HandleMonthQuarterYearlyRecordRunnable extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            DataMiningFacade dataMiningFacade = DataMiningFacade.getInstance();

            int yyyyMMOfStartDate = Integer.parseInt( params[0] );
            int yyyyMMOfNextDateOfRefreshDate = Integer.parseInt( params[1] );

            if ( yyyyMMOfNextDateOfRefreshDate == yyyyMMOfStartDate ) {
                return null;
            }

            handleAndCacheTranMonthlyRecord( yyyyMMOfStartDate, yyyyMMOfNextDateOfRefreshDate, dataMiningFacade );
            handleAndCacheTranQuarterlyRecord( yyyyMMOfStartDate, yyyyMMOfNextDateOfRefreshDate, dataMiningFacade );
            handleAndCacheTranYearlyRecord( Integer.parseInt( params[0].substring(0, 4) ), Integer.parseInt( params[1].substring(0, 4) ), dataMiningFacade );

            return null;

        }

    }

}