package com.msolo.stockeye.business.stock.mine;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 *
 * apply : Facade Pattern
 * apply : Abstract Factory Pattern - ConcreteFactory
 * apply : Strategy Pattern - Context
 *
 * Created by mSolo on 2014/8/23.
 */
public class DataMiningFacade implements DataMiningFacadeImp {

    private static final DataMiningFacade INSTANCE = new DataMiningFacade();

    private WeakReference<StockTransactionDataAnalysisImp> analyzer = null;

    private DataMiningFacade() {}

    public static DataMiningFacade getInstance() {
        return INSTANCE;
    }

    // ----------------------------------------------------------------

    @Override
    public ArrayList<String[]> analyze(String stockcode, String[] queryRecordIdArray, Object objTranRecArrayList) {

        ArrayList<String[]> queryResultArrayList = new ArrayList<String[]>();
        for ( String queryRecordId : queryRecordIdArray ) {

            String[] queryResult = analyzer.get().analyzeToTransactionRecordItemArray(stockcode, queryRecordId, (ArrayList) objTranRecArrayList);
            if ( null != queryResult ) {
                queryResultArrayList.add(queryResult);
            }

        }

        return queryResultArrayList;

    }

    @Override
    public DataMiningFacade setUpDataMiningAnalyzer(int analyzerType) {

        switch (analyzerType) {

            case 1:         // weekly
                analyzer = new WeakReference<StockTransactionDataAnalysisImp>( new StockTransactionDataWeeklyAnalysis() );
                break;

            case 2:         // monthly
                analyzer = new WeakReference<StockTransactionDataAnalysisImp>( new StockTransactionDataMonthlyAnalysis() );
                break;

            case 3:         // quarterly
                analyzer = new WeakReference<StockTransactionDataAnalysisImp>( new StockTransactionDataQuarterlyAnalysis() );
                break;

            case 4:         // yearly
                analyzer = new WeakReference<StockTransactionDataAnalysisImp>( new StockTransactionDataYearlyAnalysis() );
                break;

            default:
                break;

        }

        return this;
    }
    // ----------------------------------------------------------------



}
