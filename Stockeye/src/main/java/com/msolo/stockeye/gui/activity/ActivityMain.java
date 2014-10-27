package com.msolo.stockeye.gui.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.msolo.stockeye.R;
import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.business.stock.DownloadStockTransactionDataController;
import com.msolo.stockeye.gui.AnimationUtilTool;
import com.msolo.stockeye.gui.activity.dialogfragment.AddStock;
import com.msolo.stockeye.gui.activity.dialogfragment.DeleteStock;
import com.msolo.stockeye.gui.activity.quotation.StockQuoteMediator;
import com.msolo.stockeye.gui.fragment.FragmentLogin;

public class ActivityMain extends FragmentActivity {

    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        if ( StockeyeApp.isLargeScreen ) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        } else {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            FragmentNavigationDrawer fragmentDrawer = (FragmentNavigationDrawer) fragmentManager.findFragmentById(R.id.navigation_drawer);
            fragmentDrawer.setUpDrawerLayout((DrawerLayout) findViewById(R.id.drawerlayout));

        }


        StockQuoteMediator.getInstance().setSupportFragmentManager(fragmentManager);

        fragmentManager.beginTransaction().replace(R.id.content, new FragmentLogin(), "login").commit();

    }

    @Override
    protected void onStart() {

        super.onStart();

        DownloadStockTransactionDataController.getInstance().startDownloadService();

    }

    @Override
    protected void onStop() {

        super.onStop();

        DownloadStockTransactionDataController.getInstance().stopDownloadService();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        finish();
        System.exit(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onStockSelfListClick(View view) {

        switch ( view.getId() ) {

            case R.id.expand_stock:
                break;
            case R.id.share_info:
                break;

            case R.id.add_stock:
                AddStock fragmentAddStock = new AddStock();
                fragmentAddStock.show(getFragmentManager(), "dialog_add_stock");
                break;

            case R.id.delete_stock:
                DeleteStock fragmentDeleteStock = new DeleteStock();
                fragmentDeleteStock.show(getFragmentManager(), "dialog_del_stock");
                break;

            default:
                break;

        }

    }

    public void onStockShareDealingClick(View view) {



    }

    public void onNavigationTabClick(View view) {

        AnimationUtilTool.getInstance().setShakeAnimation(view);

        switch ( view.getId() ) {

            case R.id.home:

                fragmentManager.beginTransaction().replace(R.id.content, new FragmentLogin(), "login").commit();
                break;

            case R.id.notebook:


                break;

            case R.id.download:     // controll download: download, pause, continue, stop, ...

                break;

            case R.id.update:       // update stock transaction data

                int downloadNumber = DownloadStockTransactionDataController.getInstance().updateDownloadStockcodeList();
                if ( downloadNumber == 0) {
                    Toast.makeText(this, "没有需要更新交易数据的股票", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, String.format("本次将更新 %d 股票", downloadNumber), Toast.LENGTH_SHORT).show();
                }

                break;

            default:

                break;

        }

    }

}
