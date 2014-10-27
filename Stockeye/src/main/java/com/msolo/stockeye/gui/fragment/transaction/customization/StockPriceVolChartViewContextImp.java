package com.msolo.stockeye.gui.fragment.transaction.customization;

import android.graphics.Canvas;
import android.util.SparseArray;

/**
 * Created by mSolo on 2014/9/2.
 */
public interface StockPriceVolChartViewContextImp {

    public Canvas getCanvas();
    public int[] getXYAttrInfoArray();
    public void setRecordSparseArray(SparseArray<long[]> recordSparseArray);

}
