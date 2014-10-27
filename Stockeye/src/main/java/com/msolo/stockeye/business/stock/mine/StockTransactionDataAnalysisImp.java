package com.msolo.stockeye.business.stock.mine;

import java.util.ArrayList;

/**
 *
 * apply : Abstract Factory Pattern - abstractProduct interface
 * apply : Strategy Pattern - strategy interface
 *
 * Created by mSolo on 2014/8/24.
 */

public interface StockTransactionDataAnalysisImp<T> {

    public String[] analyzeToTransactionRecordItemArray(String stockcode, String queryRecordId, ArrayList<T> objTranRecArrayList);

}
