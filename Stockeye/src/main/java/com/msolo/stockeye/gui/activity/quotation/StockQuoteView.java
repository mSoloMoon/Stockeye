package com.msolo.stockeye.gui.activity.quotation;

/**
 * Created by mSolo on 2014/8/9.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.msolo.stockeye.R;
import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.business.notification.StockQuoteNotificationBuilder;
import com.msolo.stockeye.gui.AnimationUtilTool;
import com.msolo.stockeye.gui.UtilViewTool;
import com.msolo.stockeye.gui.fragment.transaction.common.MvcControllerFragmentStockTranContent;
import com.msolo.stockeye.service.userdata.StockPriceObserverImp;
import com.msolo.stockeye.service.userdata.StockcodeObserverImp;
import com.msolo.stockeye.setting.StockSetting;

import java.util.ArrayList;
import java.util.List;

public class StockQuoteView implements StockcodeObserverImp, StockPriceObserverImp {

    private List<String> stocknameArrayList = null;
    private String[] stockcodeArray = null;
    private String[] stockCurrentUpdatedDateArray = null;
    private String[] priceDataArray = null;

    private TextView stockSHTextView = null;
    private TextView stockSHPriceTextView = null;
    private TextView stockSZTextView = null;
    private TextView stockSZPriceTextView = null;
    private TextView stockCountTextView = null;
    private ListView stocksListView = null;

    private ArrayAdapterStockListview adpaterStockList = null;
    private MvcControllerFragmentStockTranContent mvcController = null;

    // red color : 0xFFBE0D15;
    // green color : 0xFF00C600;
    // white color : 0xFFFFFFFF;

    public StockQuoteView() {

        stocknameArrayList = new ArrayList<String>();
        mvcController = new MvcControllerFragmentStockTranContent();
        mvcController.setUpFragmentStockTranContent();
        mvcController.setUpMvcView();

    }
        
    public void registerForDefaultStocks(TextView shNameTextView, TextView shPriceTextView,
                                   TextView szNameTextView, TextView szPriceTextView) {

        stockSHTextView = shNameTextView;
        stockSHPriceTextView = shPriceTextView;
        stockSZTextView = szNameTextView;
        stockSZPriceTextView = szPriceTextView;

    }

    public void registerForStockSelfList(TextView counterTextView, ListView listView) {
        stockCountTextView = counterTextView;
        stocksListView = listView;
    }

    public void setUp() {

        StockQuoteMediator stockQuoteMediator = StockQuoteMediator.getInstance();

        String[] stocknameArray = stockQuoteMediator.getStocknameArray();
        for ( int i=2, size=stocknameArray.length; i<size; i++ ) {
            stocknameArrayList.add(stocknameArray[i]);
        }
        priceDataArray = stockQuoteMediator.getStockPriceArray();

        stockSHTextView.setText(stocknameArray[0]);
        stockSZTextView.setText(stocknameArray[1]);

        if (priceDataArray[0].startsWith("+")) {
            stockSHPriceTextView.setTextColor(UtilViewTool.COLOR_RED);
        } else if (priceDataArray[0].startsWith("-")) {
            stockSHPriceTextView.setTextColor(UtilViewTool.COLOR_GREEN);
        }
        stockSHPriceTextView.setText(priceDataArray[0].substring(1));

        if (priceDataArray[1].startsWith("+")) {
            stockSZPriceTextView.setTextColor(UtilViewTool.COLOR_RED);
        } else if (priceDataArray[1].startsWith("-")) {
            stockSZPriceTextView.setTextColor(UtilViewTool.COLOR_GREEN);
        }
        stockSZPriceTextView.setText(priceDataArray[1].substring(1));

        stockCountTextView.setText(String.format("%d", stocknameArrayList.size()));

        setUpStockSelfList();

    }

    @Override
    public void update(String[] stockPriceArr) {

        StockQuoteMediator.getInstance().getActivityMain().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                AnimationUtilTool animationUtilTool = AnimationUtilTool.getInstance();
                StockQuoteMediator stockQuoteMediator = StockQuoteMediator.getInstance();

                stockcodeArray = stockQuoteMediator.getStockcodeArray();
                stockCurrentUpdatedDateArray = stockQuoteMediator.getStockDateArray();
                priceDataArray = stockQuoteMediator.getStockPriceArray();

                if (priceDataArray[0].startsWith("+")) {
                    stockSHPriceTextView.setTextColor(UtilViewTool.COLOR_RED);
                } else if (priceDataArray[0].startsWith("-")) {
                    stockSHPriceTextView.setTextColor(UtilViewTool.COLOR_GREEN);
                }
                stockSHPriceTextView.setText(priceDataArray[0].substring(1));

                if (priceDataArray[1].startsWith("+")) {
                    stockSZPriceTextView.setTextColor(UtilViewTool.COLOR_RED);
                } else if (priceDataArray[1].startsWith("-")) {
                    stockSZPriceTextView.setTextColor(UtilViewTool.COLOR_GREEN);
                }
                stockSZPriceTextView.setText(priceDataArray[1].substring(1));

                animationUtilTool.setShowAnimation(stockSHPriceTextView);
                animationUtilTool.setShowAnimation(stockSZPriceTextView);

                stocknameArrayList.clear();
                String[] stocknameArray = stockQuoteMediator.getStocknameArray();
                for ( int i=2, size=stocknameArray.length; i<size; i++ ) {
                    stocknameArrayList.add(stocknameArray[i]);
                }

                stockCountTextView.setText(String.format("%d", stocknameArrayList.size()));

                adpaterStockList.notifyDataSetChanged();

                StockQuoteNotificationBuilder.getInstance().setupNotification( stockQuoteMediator.getStocknameArray(), priceDataArray );

            }

        });

    }


    @Override
    public void update(int registerNumber, String[] stockcodeArray) {
        update(null);
    }

    private void setUpStockSelfList() {

        adpaterStockList = new ArrayAdapterStockListview(StockeyeApp.appContext,
                R.layout.listview_self_stock, stocknameArrayList);

        stocksListView.setAdapter(adpaterStockList);

        stocksListView.setOnItemClickListener(getStockListOnItemClickListener());

    }

    private OnItemClickListener getStockListOnItemClickListener() {

        stockcodeArray = StockQuoteMediator.getInstance().getStockcodeArray();

        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?>parent, View view, int position, long id) {

                AnimationUtilTool.getInstance().setShakeAnimation(view.findViewById(R.id.item_for_stockname));

                String currentUpdatedDate = StockQuoteMediator.getInstance().getStockDateArray()[position];
                if ( currentUpdatedDate.equals(StockSetting.getInstance().getStartDateForStock()) ) {
                    Toast.makeText(StockeyeApp.appContext, "请更新股票数据后再查阅", Toast.LENGTH_SHORT).show();
                    return ;
                }

                mvcController.route(stocknameArrayList.get(position),
                        stockcodeArray[position + 2],
                        currentUpdatedDate,
                        StockQuoteMediator.getInstance().getSupportFragmentManager());

            }
        };

    }

    private class ArrayAdapterStockListview extends ArrayAdapter<String> {

        private int tableRowId;
        private List<String> stocknameList = null;

        public ArrayAdapterStockListview(Context c, int layoutId, List<String> stocknameArrayList) {
            super(c, layoutId, stocknameArrayList);
            tableRowId = layoutId;
            stocknameList = stocknameArrayList;
        }

        @Override
        public int getCount() {
            return stocknameList.size();
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {

            TableRow tableRow = new TableRow(StockeyeApp.appContext) ;

            LayoutInflater inflater = (LayoutInflater) StockeyeApp.appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(tableRowId, tableRow, true);

            if (stocknameList.size() < 1) {
                return tableRow;
            }

            TextView itemForName = (TextView) tableRow.findViewById(R.id.item_for_stockname);
            TextView itemForPrice = (TextView) tableRow.findViewById(R.id.item_for_stockprice);

            itemForName.setText(stocknameList.get(position));
            String priceStr = priceDataArray[position + 2];

            if (priceStr.startsWith("+")) {
                itemForPrice.setTextColor(UtilViewTool.COLOR_RED);
            } else if (priceStr.startsWith("-")) {
                itemForPrice.setTextColor(UtilViewTool.COLOR_GREEN);
            }
            itemForPrice.setText(priceStr.substring(1));
            AnimationUtilTool.getInstance().setShowAnimation(itemForPrice);

            return tableRow;

        }

    }

}
