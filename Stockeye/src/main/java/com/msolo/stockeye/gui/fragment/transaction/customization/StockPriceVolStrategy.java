package com.msolo.stockeye.gui.fragment.transaction.customization;

import android.graphics.Canvas;
import android.os.AsyncTask;

import com.msolo.stockeye.service.common.CommonServiceFacade;
import com.msolo.stockeye.service.report.StockPriceVolData;

/**
 * Setting transaction data record for stock Pirce-Vol chart with
 * daily, weekly, monthly, quarterly, yearly
 *
 * Created by mSolo on 2014/8/26.
 *
 */
public class StockPriceVolStrategy extends AsyncTask<String, Void, Void> implements StockPriceVolDataStrategyImp {

    private final int chartDailyRecordLength = 14;
    private final int chartDailyRecordCapacity = 500;
    private final int chartWeeklyRecordCapacity = 500;
    private final int chartMonthlyRecordCapacity = 360;
    private final int chartQuarterlyRecordCapacity = 120;
    private final int chartYearlyRecordCapacity = 30;

    private long[][] chartDailyRecordArray;
    private long[][] chartWeeklyRecordArray;

    private String table;

    private StockPriceVolStrategyDrawCandleStick strategyDrawCandleStick;

    public StockPriceVolStrategy() {
        strategyDrawCandleStick = new StockPriceVolStrategyDrawCandleStick();
    }

    @Override
    protected Void doInBackground(String... params) {

        table = params[0];

        chartDailyRecordArray = new long[1 + chartDailyRecordCapacity][chartDailyRecordLength];
        chartWeeklyRecordArray = null;
        StockPriceVolData.getStockPriceVoDailyData(chartDailyRecordCapacity, table, chartDailyRecordArray);

        return null;

    }

    @Override
    public void applyStrategy(StockPriceVolChartView view) {

        int[] viewAttrArray = view.getXYAttrInfoArray();

        strategyDrawCandleStick.init();
        strategyDrawCandleStick.setXYAttrInfo(viewAttrArray);
        Canvas canvas = view.getCanvas();

        switch (viewAttrArray[10]) {
            case 0:
                strategyDrawCandleStick.setSizeOfChartRecordArray((int) chartDailyRecordArray[0][0]);
                strategyDrawCandleStick.initRecordSparseArray(chartDailyRecordCapacity);
                strategyDrawCandleStick.setChartRecordArray(chartDailyRecordArray);

                if ( chartWeeklyRecordArray == null ) {
                    setChartWeeklyRecordArray();
                }
                break;

            case 1:
                strategyDrawCandleStick.setSizeOfChartRecordArray((int) chartWeeklyRecordArray[0][0]);
                strategyDrawCandleStick.initRecordSparseArray(chartWeeklyRecordCapacity);
                strategyDrawCandleStick.setChartRecordArray(chartWeeklyRecordArray);
                break;

            case 2:
                long[][] chartMonthlyRecordArray = new long[1 + chartMonthlyRecordCapacity][chartDailyRecordLength];
                StockPriceVolData.getStockPriceVolMonthlyData(chartMonthlyRecordCapacity, table, chartMonthlyRecordArray);

                strategyDrawCandleStick.setSizeOfChartRecordArray((int) chartMonthlyRecordArray[0][0]);
                strategyDrawCandleStick.initRecordSparseArray(chartMonthlyRecordCapacity);
                strategyDrawCandleStick.setChartRecordArray(chartMonthlyRecordArray);
                break;

            case 3:
                long[][] chartQuarterlyRecordArray = new long[1 + chartQuarterlyRecordCapacity][chartDailyRecordLength];
                StockPriceVolData.getStockPriceVolQuarterlyData(chartQuarterlyRecordCapacity, table, chartQuarterlyRecordArray);

                strategyDrawCandleStick.setSizeOfChartRecordArray((int) chartQuarterlyRecordArray[0][0]);
                strategyDrawCandleStick.initRecordSparseArray(chartQuarterlyRecordCapacity);
                strategyDrawCandleStick.setChartRecordArray(chartQuarterlyRecordArray);
                break;

            case 4:
                long[][] chartYearlyRecordArray = new long[1 + chartYearlyRecordCapacity][chartDailyRecordLength];
                StockPriceVolData.getStockPriceVolYearlyData(chartYearlyRecordCapacity, table, chartYearlyRecordArray);

                strategyDrawCandleStick.setSizeOfChartRecordArray((int) chartYearlyRecordArray[0][0]);
                strategyDrawCandleStick.initRecordSparseArray(chartYearlyRecordCapacity);
                strategyDrawCandleStick.setChartRecordArray(chartYearlyRecordArray);
                break;

            default:
                return ;
        }

        strategyDrawCandleStick.drawPriceVolView(canvas);

        view.setRecordSparseArray(strategyDrawCandleStick.getRecordSparseArray());

    }

    private void setChartWeeklyRecordArray() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                chartWeeklyRecordArray = new long[1 + chartWeeklyRecordCapacity][chartDailyRecordLength];
                StockPriceVolData.getStockPriceVolWeeklyData(chartWeeklyRecordCapacity, table, chartWeeklyRecordArray);

                return null;
            }

        }.executeOnExecutor( CommonServiceFacade.getInstance().getExecutorServiceCachedThreadPool() );

    }

}
