package com.msolo.stockeye.gui.activity.dialogfragment;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.msolo.stockeye.R;
import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.service.common.CommonServiceFacade;
import com.msolo.stockeye.gui.activity.quotation.StockQuoteMediator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mSolo on 2014/8/12.
 */
public class AddStock extends DialogFragment implements View.OnClickListener {

    private EditText et;

    public AddStock() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setCancelable(true);
        setStyle(DialogFragment.STYLE_NORMAL, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View dialogLayout = inflater.inflate(R.layout.dialog_add_stock, container);

        Button confirmBtn = (Button) dialogLayout.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(this);

        Button cancelBtn = (Button) dialogLayout.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);

        et = (EditText) dialogLayout.findViewById(R.id.input_text);
        if (savedInstanceState != null) {
            et.setText(savedInstanceState.getCharSequence("input"));
        }

        return dialogLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onClick(View v) {

        if ( v.getId() == R.id.btn_confirm ) {

            String stockcode = et.getText().toString();
            if (stockcode.isEmpty() || stockcode.length() != 8) {
                StockQuoteMediator.getInstance()
                        .toastMessage("输入有误，输入格式如下 : shXXXXXX或szXXXXXX，其中XXXXXX表示6位有效的股票代码");
                return ;
            } else {

                new AsyncTask<String, Void, Void>() {

                    @Override
                    protected Void doInBackground(String... params) {

                        String stockcode = params[0];
                        String stockname = CommonServiceFacade.getInstance().getStockResourceString(0, stockcode);

                        if (stockname == null) {
                            StockQuoteMediator.getInstance().toastMessage("找不到该股票或网络出错！请核实");
                        } else {
                            StockQuoteMediator.getInstance().addOneStockToQuote(stockcode, stockname);
                        }

                        return null;

                    }

                }.executeOnExecutor(CommonServiceFacade.getInstance().getExecutorServiceCachedThreadPool(), stockcode);

            }

        }

        dismiss();
        return ;

    }

}
