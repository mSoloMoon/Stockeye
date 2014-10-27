package com.msolo.stockeye.gui.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.msolo.stockeye.R;
import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.service.userdata.StockQuoteData;
import com.msolo.stockeye.gui.activity.quotation.StockQuoteMediator;
import com.msolo.stockeye.gui.activity.quotation.StockQuoteView;

public class FragmentNavigationDrawer extends Fragment {

    private DrawerLayout mDrawerLayout = null;
    private RelativeLayout mDrawerContainer = null;

    private StockQuoteMediator mediator = null;

    public FragmentNavigationDrawer() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediator = StockQuoteMediator.getInstance();
        mediator.setActivityMain(getActivity());
        mediator.setUpStockQuoteData(new StockQuoteData());

        // select home FrameLayout
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDrawerContainer = (RelativeLayout) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);

        StockQuoteView stockQuoteView = new StockQuoteView();
        mediator.setUpStockQuoteView(stockQuoteView);

        if (!StockeyeApp.isLargeScreen) {
            // stub
        }

        TableRow shStocTableRow = (TableRow) mDrawerContainer.findViewById(R.id.sh_stock);
        TextView shStocksNameTextView = (TextView) shStocTableRow.findViewById(R.id.sh_stocks_name);
        TextView shStocksPriceTextView = (TextView) shStocTableRow.findViewById(R.id.sh_stocks_price);

        TableRow szStocTableRow = (TableRow) mDrawerContainer.findViewById(R.id.sz_stock);
        TextView szStocksNameTextView = (TextView) szStocTableRow.findViewById(R.id.sz_stocks_name);
        TextView szStocksPriceTextView = (TextView) szStocTableRow.findViewById(R.id.sz_stocks_price);

        stockQuoteView.registerForDefaultStocks(shStocksNameTextView, shStocksPriceTextView, szStocksNameTextView, szStocksPriceTextView);

        TextView stockCountTextView = (TextView) mDrawerContainer.findViewById(R.id.navigation_drawer).findViewById(R.id.stock_count);
        ListView stockSelfListListView = (ListView) mDrawerContainer.findViewById(R.id.stock_list);

        stockQuoteView.registerForStockSelfList(stockCountTextView, stockSelfListListView);
        stockQuoteView.setUp();

        if (StockeyeApp.isLargeScreen) {
            mediator.startStockPriceRefreshing();
        }

        return mDrawerContainer;

    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUpDrawerLayout(DrawerLayout drawerLayout) {

        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
                mediator.endStockPriceRefreshing();

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                mediator.startStockPriceRefreshing();

            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if (StockeyeApp.isLargeScreen) {
            mediator.endStockPriceRefreshing();
        }


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

}
