package com.msolo.stockeye.gui.fragment.transaction.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.msolo.stockeye.R;
import com.msolo.stockeye.gui.fragment.transaction.FragmentStockTranContent;

/**
 * Created by mSolo on 2014/8/22.
 */
public class MvcControllerFragmentStockTranContent {

    private FragmentStockTranContent fragmentStockTranContent = null;
    private MvcViewFragmentStockTranContent updateMvcView = null;
    private String currentStock = null;

    public MvcControllerFragmentStockTranContent() {}

    public void setUpFragmentStockTranContent() {
        fragmentStockTranContent = new FragmentStockTranContent();
    }

    public void setUpMvcView() {
        updateMvcView = MvcViewFragmentStockTranContent.getInstance();
    }

    public void route(String stockname, String stockcode, String stockCurrentUpdatedDateArray, FragmentManager fragmentManager) {

        Fragment fragment = fragmentManager.findFragmentByTag("stockcontent");

        if ( null != currentStock && currentStock.equals(stockcode) && null != fragment ) {
            return ;
        }

        currentStock = stockcode;

        updateMvcView.setUpMvcModel( new String[]{stockname, stockcode},
                MvcModelFragmentStockTranContent.getDefaultMvcMode(stockcode, stockCurrentUpdatedDateArray) );

        fragmentStockTranContent.setStockcode(stockcode);
        if ( null == fragment || fragment.hashCode() != fragmentStockTranContent.hashCode()) {

            fragmentStockTranContent.setUpMvcView(updateMvcView);
            fragmentManager.beginTransaction().replace(R.id.content, fragmentStockTranContent, "stockcontent").commit();

        } else {

            fragmentStockTranContent.setUpKChartPriceVolStrategy();
            fragmentStockTranContent.resetPeriodTypeBackground();
            updateMvcView.execute();

        }

    }

}
