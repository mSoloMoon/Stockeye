package com.msolo.stockeye.gui.activity.dialogfragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.msolo.stockeye.R;
import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.gui.activity.quotation.StockQuoteMediator;
import com.msolo.stockeye.service.common.CommonServiceFacade;
import com.msolo.stockeye.service.db.build.DatabaseStoreServiceFacade;
import com.msolo.stockeye.setting.StockSetting;

import java.util.LinkedHashMap;

/**
 * Created by mSolo on 2014/8/14.
 */
public class DeleteStock extends DialogFragment implements View.OnClickListener {

    private String[] stocknameArray = null;
    private String[] stockcodeArray = null;
    private LinkedHashMap<Integer, Boolean> mapStocknameCheckBox = new LinkedHashMap<Integer, Boolean>();

    public DeleteStock() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setCancelable(true);
        setStyle(DialogFragment.STYLE_NORMAL, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View dialogLayout = inflater.inflate(R.layout.dialog_del_stock, container, false);

        Button confirmBtn = (Button) dialogLayout.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(this);

        Button cancelBtn = (Button) dialogLayout.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);

        String[] stocknames = StockQuoteMediator.getInstance().getStocknameArray();
        String[] stockcodes = StockQuoteMediator.getInstance().getStockcodeArray();

        int size = stocknames.length;

        if (size == 2) {
            TextView deleteStockPrompt = (TextView) dialogLayout.findViewById(R.id.del_stock_prompt);
            deleteStockPrompt.setText("巧妇难为无米之炊！加点料再看看？");

            return dialogLayout;
        }

        stocknameArray = new String[size - 2];
        stockcodeArray = new String[size - 2];
        for (int i=2; i<size; i++) {
            stocknameArray[i-2] = stocknames[i];
            stockcodeArray[i-2] = stockcodes[i];
            mapStocknameCheckBox.put(i-2, false);
        }

        ListView deleteStockList = (ListView) dialogLayout.findViewById(R.id.del_stock_lists);
        deleteStockList.setAdapter(new ArrayAdapterDeleteStock(R.layout.listview_del_stock,
                stocknameArray,
                mapStocknameCheckBox) );

        return dialogLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onClick(View v) {

        if ( v.getId() == R.id.btn_confirm && (stocknameArray != null) ) {

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {

                    for (int i=0; i < stocknameArray.length; i++) {
                        if ( mapStocknameCheckBox.get(Integer.valueOf(i)) ) {

                            if ( !StockQuoteMediator.getInstance().getStockDateArray()[i].equals(StockSetting.getInstance().getStartDateForStock()) ) {
                                new DatabaseStoreServiceFacade().dropDbTable(stockcodeArray[i]);
                            }

                            StockQuoteMediator.getInstance().removeOneStock(stockcodeArray[i]);

                        }

                    }

                    return null;

                }

            }.executeOnExecutor( CommonServiceFacade.getInstance().getExecutorServiceCachedThreadPool() );

        }

        dismiss();
        return ;

    }

    public class ArrayAdapterDeleteStock extends BaseAdapter {

        private int layoutId;
        private String[] arrayList;
        private LinkedHashMap<Integer, Boolean> mapCheckBox;
        private LinkedHashMap<CompoundButton.OnCheckedChangeListener, Integer> mapOnCheckedChangeListener;

        public ArrayAdapterDeleteStock(int resource, String[] array, LinkedHashMap<Integer, Boolean> map) {
            layoutId = resource;
            arrayList = array;
            mapCheckBox = map;
            mapOnCheckedChangeListener = new LinkedHashMap<CompoundButton.OnCheckedChangeListener, Integer>();
        }

        @Override
        public int getCount() {
            return arrayList.length;
        }

        @Override
        public Object getItem(int position) {
            return arrayList[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {

            View tableRowView = v ;

            if(tableRowView == null){
                LayoutInflater inflater = (LayoutInflater)StockeyeApp.appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                tableRowView = inflater.inflate(layoutId, null);
            }

            CheckBox deleteCheckBox = (CheckBox) tableRowView.findViewById(R.id.chk_del_stock);
            TextView deleteStockName = (TextView) tableRowView.findViewById(R.id.del_item);

            deleteCheckBox.setChecked(mapCheckBox.get(position));
            deleteStockName.setText(arrayList[position]);

            CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    mapCheckBox.put(mapOnCheckedChangeListener.get(this), isChecked);
                }

            };
            mapOnCheckedChangeListener.put(checkedChangeListener, position);

            deleteCheckBox.setOnCheckedChangeListener(checkedChangeListener);

            return tableRowView;

        }

    }

}