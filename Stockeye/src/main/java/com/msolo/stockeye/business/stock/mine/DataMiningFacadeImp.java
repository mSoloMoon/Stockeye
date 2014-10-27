package com.msolo.stockeye.business.stock.mine;

import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/23.
 */
public interface DataMiningFacadeImp<T> {

    public ArrayList<String[]> analyze(String stockcode, String[] queryRecordIdArrayList, T objTranRecArrayList);

    public DataMiningFacadeImp setUpDataMiningAnalyzer(int analyzerType);

}
