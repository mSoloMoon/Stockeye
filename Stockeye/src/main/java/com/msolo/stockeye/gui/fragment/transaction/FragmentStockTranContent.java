package com.msolo.stockeye.gui.fragment.transaction;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.msolo.stockeye.R;
import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.gui.fragment.publ.FragmentViewPagerAdapter;
import com.msolo.stockeye.gui.fragment.transaction.common.MvcModelFragmentStockTranContent;
import com.msolo.stockeye.gui.fragment.transaction.common.MvcViewFragmentStockTranContent;
import com.msolo.stockeye.gui.fragment.transaction.customization.StockPriceVolStrategy;
import com.msolo.stockeye.gui.fragment.transaction.customization.YoutubeLayout;
import com.msolo.stockeye.service.common.CommonServiceFacade;
import com.msolo.stockeye.service.db.access.DatabaseQueryServiceFacade;

import java.util.ArrayList;

/**
 * Created by mSolo on 2014/8/11.
 */
public class FragmentStockTranContent extends Fragment
        implements View.OnClickListener, View.OnLongClickListener, OnDateSetListener, AdapterView.OnItemSelectedListener {

    private boolean isSpinnerIdSelected = false;
    private int spinnerIdOrLongClickViewId;

    private StringBuilder selectedDateRangeForSpinner;

    private String stockcode;

    private TextView dayType;
    private TextView weekType;
    private TextView monthType;
    private TextView quarterType;
    private TextView yearType;
    private Spinner spinnerPeriod;

    private View mTopLayout;
    private ViewPager stockViewPager;
    private ListView stockTranDetailContent;
    private YoutubeLayout youtubeLayout;

    private FragmentStockTranContentWebView webViewMaker;
    private MvcViewFragmentStockTranContent updateMvcView;

    public FragmentStockTranContent() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateMvcView.setUpActivity( getActivity() );
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTopLayout = inflater.inflate(R.layout.fragment_stock_content, container, false);

        setUpPeriodCommand();

        setUpViewPagerAndTranDataDetailView(inflater);

        setUpKChartOfPriceVolDataSource();

        updateMvcView.setBriefTextView(mTopLayout);
        updateMvcView.execute();

        return mTopLayout;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //
    // ------------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {

        changeBackground(v);

        updateMvcView.setUpMvcModel( null, MvcModelFragmentStockTranContent.getMvcModel( v.getId(), null ) );
        updateMvcView.execute();

    }

    @Override
    public boolean onLongClick(View v) {

        spinnerIdOrLongClickViewId = v.getId();

        changeBackground(v);

        showDatePickerDialog();

        return true;

    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        StringBuilder theLongClickDate = new StringBuilder().append( String.format("%d%02d%02d", year, month + 1, day) );

        if ( !DatabaseQueryServiceFacade.getInstance().setUpQueryer(0).isTranRecExist(stockcode, theLongClickDate.toString()) ) {

            if (!isSpinnerIdSelected) {
                Toast.makeText(StockeyeApp.appContext, "该日期交易数据不存在", Toast.LENGTH_LONG).show();
                return;
            }

        }

        if (isSpinnerIdSelected) {
            if (selectedDateRangeForSpinner == null) {
                selectedDateRangeForSpinner = theLongClickDate;
                showDatePickerDialog();
                return ;
            } else {
                theLongClickDate.insert(0, selectedDateRangeForSpinner);
                isSpinnerIdSelected = false;
                spinnerPeriod.setSelection(0);
            }
        }

        updateMvcView.setUpMvcModel( null, MvcModelFragmentStockTranContent.getMvcModel( spinnerIdOrLongClickViewId, theLongClickDate.toString() ) );
        updateMvcView.execute();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnerIdOrLongClickViewId = (int) id;

        switch (spinnerIdOrLongClickViewId) {
            case 1 :

                break;

            case 2 :

                break;

            case 3 :

                break;

            case 4 :

                break;

            case 5 :

                break;

            case 6 :

                isSpinnerIdSelected = true;
                selectedDateRangeForSpinner = null;

                showDatePickerDialog();
                break;

            case 7 :

                break;

            case 8 :

                break;

            case 9 :

                break;

            case 10 :

                break;

            case 11 :

                break;

            case 12 :

                break;

            default :

                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void resetPeriodTypeBackground() {

        dayType.setBackgroundResource(R.drawable.rounded_rectangle_light_gray);
        weekType.setBackgroundResource(R.drawable.rounded_rectangle_light_gray);
        monthType.setBackgroundResource(R.drawable.rounded_rectangle_light_gray);
        quarterType.setBackgroundResource(R.drawable.rounded_rectangle_light_gray);
        yearType.setBackgroundResource(R.drawable.rounded_rectangle_light_gray);

        dayType.setBackgroundResource(R.drawable.rounded_rectangle_skyblue);

    }

    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
    }

    public void setUpKChartPriceVolStrategy() {

        StockPriceVolStrategy strategy = new StockPriceVolStrategy();
        strategy.executeOnExecutor( CommonServiceFacade.getInstance().getExecutorServiceCachedThreadPool(), stockcode );

        youtubeLayout.setStockPriceVolstrategy(strategy);
        youtubeLayout.requestChartViewLayout();

    }

    public void setUpMvcView(MvcViewFragmentStockTranContent updateView) {
        updateMvcView = updateView;
    }

    private void changeBackground(View v) {

        dayType.setBackgroundResource(R.drawable.rounded_rectangle_light_gray);
        weekType.setBackgroundResource(R.drawable.rounded_rectangle_light_gray);
        monthType.setBackgroundResource(R.drawable.rounded_rectangle_light_gray);
        quarterType.setBackgroundResource(R.drawable.rounded_rectangle_light_gray);
        yearType.setBackgroundResource(R.drawable.rounded_rectangle_light_gray);

        v.setBackgroundResource(R.drawable.rounded_rectangle_skyblue);

    }

    private void setUpKChartOfPriceVolDataSource() {

        youtubeLayout = (YoutubeLayout) mTopLayout.findViewById(R.id.dragLayout);

        setUpKChartPriceVolStrategy();

        updateMvcView.setYoutubeLayout( youtubeLayout );

    }

    private void setUpPeriodCommand() {

        TableRow periodicTypeSelector = (TableRow) mTopLayout.findViewById(R.id.tran_periodic_type_selector);

        dayType = (TextView) periodicTypeSelector.findViewById(R.id.type_day);
        dayType.setBackgroundResource(R.drawable.rounded_rectangle_skyblue);
        weekType = (TextView) periodicTypeSelector.findViewById(R.id.type_week);
        monthType = (TextView) periodicTypeSelector.findViewById(R.id.type_month);
        quarterType = (TextView) periodicTypeSelector.findViewById(R.id.type_quarter);
        yearType = (TextView) periodicTypeSelector.findViewById(R.id.type_year);

        dayType.setOnClickListener(this);
        weekType.setOnClickListener(this);
        monthType.setOnClickListener(this);
        quarterType.setOnClickListener(this);
        yearType.setOnClickListener(this);

        dayType.setOnLongClickListener(this);
        weekType.setOnLongClickListener(this);
        monthType.setOnLongClickListener(this);
        quarterType.setOnLongClickListener(this);
        yearType.setOnLongClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(StockeyeApp.appContext, R.array.other_period, R.layout.spinner_period);
        spinnerPeriod = (Spinner) periodicTypeSelector.findViewById(R.id.spinner_period);
        spinnerPeriod.setAdapter(adapter);
        spinnerPeriod.setOnItemSelectedListener(this);

    }

    private void setUpViewPagerAndTranDataDetailView( LayoutInflater inflater ) {

        RelativeLayout contentRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_stock_detail_page_tran_detail, null);
        stockTranDetailContent = (ListView) contentRelativeLayout.findViewById(R.id.detail_transaction_record_statistical);

        TextView gotoTop = (TextView) contentRelativeLayout.findViewById(R.id.goto_top);
        gotoTop.setClickable(true);
        gotoTop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (!stockTranDetailContent.isStackFromBottom()) {
                    stockTranDetailContent.setStackFromBottom(true);
                }

                stockTranDetailContent.setStackFromBottom(false);
            }
        });

        TextView gotoBottom = (TextView) contentRelativeLayout.findViewById(R.id.goto_bottom);
        gotoBottom.setClickable(true);
        gotoBottom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (!stockTranDetailContent.isStackFromBottom()) {
                    stockTranDetailContent.setStackFromBottom(true);
                }
            }
        });

        WebView webView = (WebView) inflater.inflate(R.layout.fragment_stock_detail_page_webview, null);

        ArrayList<View> viewList = new ArrayList<View>();
        viewList.add(contentRelativeLayout);
        viewList.add(webView);

        stockViewPager = (ViewPager) mTopLayout.findViewById(R.id.viewpager);
        stockViewPager.setAdapter(new FragmentViewPagerAdapter(viewList));
        stockViewPager.setCurrentItem(0);

        webViewMaker = new FragmentStockTranContentWebView( webView );
        updateMvcView.setDetailLayoutView(contentRelativeLayout, webViewMaker);

    }

    private void showDatePickerDialog() {

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, 2014, 7, 15, false);
        datePickerDialog.setCancelable( true );
        datePickerDialog.setVibrate( false );
        datePickerDialog.setFirstDayOfWeek(1);
        datePickerDialog.setYearRange(2006, 2028);
        datePickerDialog.setOnDateSetListener(this);
        datePickerDialog.show( getActivity().getSupportFragmentManager(), "datepicker" );

    }

}