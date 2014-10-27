package com.msolo.stockeye.gui.fragment.transaction.common;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.msolo.stockeye.R;
import com.msolo.stockeye.gui.AnimationUtilTool;
import com.msolo.stockeye.gui.fragment.transaction.FragmentStockTranContentWebView;
import com.msolo.stockeye.gui.fragment.transaction.customization.YoutubeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Created by mSolo on 2014/8/21.
 */
public class MvcViewFragmentStockTranContent
        implements AdapterView.OnItemSelectedListener {

    private static final MvcViewFragmentStockTranContent INSTANCE = new MvcViewFragmentStockTranContent();

    private String stockname;
    private String stockcode;

    private int seletedId;

    private ArrayAdapter<String> singleMaxVolRecordsAdapter = null;
    private ArrayAdapter<String> generalRecordsAdapter = null;
    private ArrayAdapter<String> timeSortRecordsAdapter = null;
    private ArrayAdapter<String> priceSortRecordsAdapter = null;
    private ArrayAdapter<String> volSortRecordsAdapter = null;
    private ArrayAdapter<String> priceCountSortRecordsAdapter = null;

    private TextView stocknameTextview;
    private TextView stockcodeTextview;
    private TextView stockClosePriceTextview;
    private TextView stockOpenPriceTextview;
    private TextView stockLastPriceTextview;
    private TextView stockHighestPriceTextview;
    private TextView stockLowestPriceTextview;
    private TextView stockPriceCountTextview;
    private TextView stockTranCountTextview;
    private TextView stockVolumeTextview;
    private TextView stockMoneyTextview;
    private TextView stockPeriodDecoratorTextview;
    private TextView stockPeriodTextview;

    private Activity hostActivity;
    private Spinner stockTranDetailContentSpinner;
    private ListView stockTranDetailContent;
    private FragmentStockTranContentWebView webViewMaker;
    private YoutubeLayout youtubeLayout;

    private ArrayAdapterAbstractTranRec mvcModelArrayAdapter;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            youtubeLayout.setVisibility(View.VISIBLE);
            youtubeLayout.maximize();
        }
    };

    private MvcViewFragmentStockTranContent() {}

    public static MvcViewFragmentStockTranContent getInstance() {
        return INSTANCE;
    }

    public void execute() {

        hostActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                stocknameTextview.setText(stockname);
                stockcodeTextview.setText(stockcode);

                setKeyPrice();

                stockPriceCountTextview.setText(mvcModelArrayAdapter.getPriceCount());
                stockTranCountTextview.setText(mvcModelArrayAdapter.getTranCount());
                AnimationUtilTool.getInstance().setInAnimation( stockTranCountTextview );

                stockVolumeTextview.setText(mvcModelArrayAdapter.getVolume());
                AnimationUtilTool.getInstance().setInAnimation( stockVolumeTextview );

                stockMoneyTextview.setText(mvcModelArrayAdapter.getMoney());
                stockPeriodDecoratorTextview.setText(mvcModelArrayAdapter.getPeriodDecorator());
                stockPeriodTextview.setText(mvcModelArrayAdapter.getPeriod());

            }

        });

    }

    public void setUpActivity(Activity activity) {
        hostActivity = activity;
    }

    public void setBriefTextView(View layoutView) {

        stocknameTextview = (TextView) layoutView.findViewById(R.id.stock_name);
        stockcodeTextview = (TextView) layoutView.findViewById(R.id.stock_code);
        stocknameTextview.setClickable(true);
        stocknameTextview.setOnClickListener(onClickListener);
        stockcodeTextview.setClickable(true);
        stockcodeTextview.setOnClickListener(onClickListener);

        stockClosePriceTextview = (TextView) layoutView.findViewById(R.id.stock_close_price);
        stockOpenPriceTextview = (TextView) layoutView.findViewById(R.id.stock_open_price);
        stockLastPriceTextview = (TextView) layoutView.findViewById(R.id.stock_last_price);
        stockHighestPriceTextview = (TextView) layoutView.findViewById(R.id.stock_highest_price);
        stockLowestPriceTextview = (TextView) layoutView.findViewById(R.id.stock_lowest_price);
        stockPriceCountTextview = (TextView) layoutView.findViewById(R.id.stock_price_count);
        stockTranCountTextview = (TextView) layoutView.findViewById(R.id.stock_tran_count);
        stockVolumeTextview = (TextView) layoutView.findViewById(R.id.stock_volume);
        stockMoneyTextview = (TextView) layoutView.findViewById(R.id.stock_money);
        stockPeriodDecoratorTextview = (TextView) layoutView.findViewById(R.id.stock_period_decorator);
        stockPeriodTextview = (TextView) layoutView.findViewById(R.id.stock_period);

    }

    public void setDetailLayoutView( RelativeLayout layout, FragmentStockTranContentWebView wvMaker) {

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(hostActivity,
                R.array.price_menu, R.layout.fragment_stock_detail_page_tran_detail_spinner);
        stockTranDetailContentSpinner = (Spinner) layout.findViewById(R.id.price_menu);
        stockTranDetailContentSpinner.setAdapter(adapterSpinner);
        stockTranDetailContentSpinner.setOnItemSelectedListener(this);

        stockTranDetailContent = (ListView) layout.findViewById(R.id.detail_transaction_record_statistical);

        webViewMaker = wvMaker;

    }

    public void setYoutubeLayout(YoutubeLayout layout) {
        youtubeLayout = layout;
    }

    public void setUpMvcModel(String[] params, ArrayAdapterAbstractTranRec model) {

        if (params != null) {
            stockname = params[0];
            stockcode = params[1];
        }

        mvcModelArrayAdapter = model;

        if (singleMaxVolRecordsAdapter != null) {
            singleMaxVolRecordsAdapter = null;
            generalRecordsAdapter = null;
            timeSortRecordsAdapter = null;
            priceSortRecordsAdapter = null;
            volSortRecordsAdapter = null;
            priceCountSortRecordsAdapter = null;

            stockTranDetailContentSpinner.setSelection(0);
            onItemSelected(null, null, 0, 0l);
        }
        
    }

    private Iterator<String> getTranDetailPriceIterator(StringBuilder strBuilder) {

        strBuilder.append(mvcModelArrayAdapter.getEachPriceHeaderTitle());

        Queue<String> eachPriceRecordQueue = new LinkedList<String>();
        for (String rec : mvcModelArrayAdapter.getEachPriceRecords()) {
            eachPriceRecordQueue.add(rec);
        }

        return eachPriceRecordQueue.iterator();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        seletedId = (int)id;
        mvcModelArrayAdapter.setupDetailArrayAdapter( seletedId );

        hostActivity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {

                switch (seletedId) {

                    case 0 :    // 前34笔成交量最多的交易信息
                        setThirtyFourSingleMaxVolTranDetail();
                        return ;

                    case 1 :    // 交易概况
                        setGeneralTranDetail();
                        return ;

                    case 2 :    // 交易按时间排列
                        setTranDetailDisplayByTime();
                        break;

                    case 3 :    // 交易按价格排列
                        setTranDetailDisplayByPrice();
                        break;

                    case 4 :    // 交易按成交量排列
                        setTranDetailDisplayByVol();
                        break;

                    case 5 :    // 交易按成交数排列
                        setTranDetailDisplayByTranCount();
                        break;

                    default :
                        break;

                }

                if (seletedId == 3 || seletedId == 4 || seletedId == 5) {
                    mvcModelArrayAdapter.visulizeEachPriceRecords(webViewMaker);
                }
                
            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // 默认 : 前34笔成交量最多的交易信息
    }

    private int getThePriceColor(int colorFlag) {

        int greenColor = 0xFF00C600;
        int redColor = 0xFFBE0D15;
        int whiteColor = 0xFFFFFFFF;

        if (colorFlag == 2) {
            return greenColor;
        } else if (colorFlag == 1) {
            return redColor;
        }

        return whiteColor;

    }

    private void setKeyPrice() {

        stockClosePriceTextview.setTextColor( getThePriceColor(mvcModelArrayAdapter.getCloseFlag()) );
        stockClosePriceTextview.setText(mvcModelArrayAdapter.getClosePrice());
        AnimationUtilTool.getInstance().setInAnimation( stockClosePriceTextview );

        stockOpenPriceTextview.setTextColor( getThePriceColor(mvcModelArrayAdapter.getOpenFlag()) );
        stockOpenPriceTextview.setText(mvcModelArrayAdapter.getOpenPrice());
        AnimationUtilTool.getInstance().setInAnimation( stockOpenPriceTextview );

        stockLastPriceTextview.setText(mvcModelArrayAdapter.getLastPrice());

        stockHighestPriceTextview.setTextColor( getThePriceColor(mvcModelArrayAdapter.getHighestFlag()) );
        stockHighestPriceTextview.setText(mvcModelArrayAdapter.getHighestPrice());

        stockLowestPriceTextview.setTextColor( getThePriceColor(mvcModelArrayAdapter.getLowestFlag()) );
        stockLowestPriceTextview.setText(mvcModelArrayAdapter.getLowestPrice());

    }

    private void setThirtyFourSingleMaxVolTranDetail() {

        if (mvcModelArrayAdapter != null) {

            if (singleMaxVolRecordsAdapter == null) {

                singleMaxVolRecordsAdapter = mvcModelArrayAdapter.getSingleMaxVolArrayAdapter(R.layout.listview_tran_detail_fragment_stock_detail);

            }
            stockTranDetailContent.setAdapter(singleMaxVolRecordsAdapter);

            mvcModelArrayAdapter.visulizeSingleMaxVolRecords(webViewMaker);

        }

    }

    private void setGeneralTranDetail() {

        if (generalRecordsAdapter == null) {

            generalRecordsAdapter = mvcModelArrayAdapter.getGeneralRecordsArrayAdapter(R.layout.listview_tran_detail_fragment_stock_detail);

        }
        stockTranDetailContent.setAdapter(generalRecordsAdapter);

        mvcModelArrayAdapter.visulizeGeneralRecords(webViewMaker);

    }

    private void setTranDetailDisplayByPrice() {

        if (priceSortRecordsAdapter == null) {

            StringBuilder priceSortRecordsBuilder = new StringBuilder();

            ArrayList<Integer> sortPriceKeys = new ArrayList<Integer>();
            Map<String, String> sortPriceTranDetailRecs = new TreeMap<String, String>();

            Iterator<String> iterator = getTranDetailPriceIterator(priceSortRecordsBuilder);
            while (iterator.hasNext()) {

                String theRecord = iterator.next();
                String[] arrayOfSplitRecord = theRecord.split("\t");

                sortPriceTranDetailRecs.put(arrayOfSplitRecord[1].replace(".", ""), theRecord);
                sortPriceKeys.add(Integer.valueOf(arrayOfSplitRecord[1].replace(".", "")));
            }

            Collections.sort(sortPriceKeys);
            Collections.reverse(sortPriceKeys);
            for (Integer key : sortPriceKeys) {
                priceSortRecordsBuilder.append("=").append(sortPriceTranDetailRecs.get(String.valueOf(key)));
            }

            priceSortRecordsAdapter = mvcModelArrayAdapter.getEachPriceRecordsArrayAdapter(R.layout.listview_tran_detail_fragment_stock_detail,
                    priceSortRecordsBuilder.toString().split("="));

        }
        stockTranDetailContent.setAdapter(priceSortRecordsAdapter);

    }

    private void setTranDetailDisplayByTime() {

        if (timeSortRecordsAdapter == null) {

            StringBuilder timeSortRecordsBuilder = new StringBuilder();

            Iterator<String> iterator = getTranDetailPriceIterator(timeSortRecordsBuilder);
            while (iterator.hasNext()) {
                timeSortRecordsBuilder.append("=").append(iterator.next());
            }

            timeSortRecordsAdapter = mvcModelArrayAdapter.getEachPriceRecordsArrayAdapter(R.layout.listview_tran_detail_fragment_stock_detail,
                    timeSortRecordsBuilder.toString().split("="));

        }
        stockTranDetailContent.setAdapter(timeSortRecordsAdapter);

    }

    private void setTranDetailDisplayByTranCount() {

        if (priceCountSortRecordsAdapter == null) {

            StringBuilder priceCountSortRecordsBuilder = new StringBuilder();

            ArrayList<Integer> sortPriceCountKeys = new ArrayList<Integer>();
            Map<String, String> sortPriceCountTranDetailRecs = new TreeMap<String, String>();

            Iterator<String> iterator = getTranDetailPriceIterator(priceCountSortRecordsBuilder);
            while (iterator.hasNext()) {

                String theRecord = iterator.next();
                String[] arrayOfSplitRecord = theRecord.split("\t");

                sortPriceCountTranDetailRecs.put(arrayOfSplitRecord[2], theRecord);
                sortPriceCountKeys.add(Integer.valueOf(arrayOfSplitRecord[2]));
            }

            Collections.sort(sortPriceCountKeys);
            Collections.reverse(sortPriceCountKeys);
            for (Integer key : sortPriceCountKeys) {
                priceCountSortRecordsBuilder.append("=").append(sortPriceCountTranDetailRecs.get(key.toString()));
            }

            priceCountSortRecordsAdapter = mvcModelArrayAdapter.getEachPriceRecordsArrayAdapter(R.layout.listview_tran_detail_fragment_stock_detail,
                    priceCountSortRecordsBuilder.toString().split("="));

        }
        stockTranDetailContent.setAdapter(priceCountSortRecordsAdapter);

    }

    private void setTranDetailDisplayByVol() {

        if (volSortRecordsAdapter == null) {

            StringBuilder volSortRecordsBuilder = new StringBuilder();

            ArrayList<Long> sortVolKeys = new ArrayList<Long>();
            Map<String, String> sortVolTranDetailRecs = new TreeMap<String, String>();

            Iterator<String> iterator = getTranDetailPriceIterator(volSortRecordsBuilder);
            while (iterator.hasNext()) {

                String theRecord = iterator.next();
                String[] arrayOfSplitRecord = theRecord.split("\t");
                String[] volOfArrayOfSplitRecord = arrayOfSplitRecord[3].split("\n");

                sortVolTranDetailRecs.put(volOfArrayOfSplitRecord[0], theRecord);
                sortVolKeys.add(Long.valueOf(volOfArrayOfSplitRecord[0]));
            }

            Collections.sort(sortVolKeys);
            Collections.reverse(sortVolKeys);
            for (Long key : sortVolKeys) {
                volSortRecordsBuilder.append("=").append(sortVolTranDetailRecs.get(key.toString()));
            }

            volSortRecordsAdapter = mvcModelArrayAdapter.getEachPriceRecordsArrayAdapter(R.layout.listview_tran_detail_fragment_stock_detail,
                    volSortRecordsBuilder.toString().split("="));

        }
        stockTranDetailContent.setAdapter(volSortRecordsAdapter);

    }

}