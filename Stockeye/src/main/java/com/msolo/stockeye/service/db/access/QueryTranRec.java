package com.msolo.stockeye.service.db.access;

import com.msolo.stockeye.service.db.build.ObjAbstractTranRec;

import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/23.
 */
public abstract class QueryTranRec<T> {

    protected boolean isTranRecExist(String table, String theQueryDate) {
        return false;
    }

    protected ObjAbstractTranRec queryOneTranRec(String table, String theQueryDate) {
        return null;
    }

    protected void queryTranRecRange(boolean indicatorFullySet, String table, String dailyRange, ArrayList<T> objTranRecArrayList) {
        return ;
    }

}
