package com.msolo.stockeye.gui.fragment.transaction.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.msolo.stockeye.R;

/**
 * Created by mSolo on 2014/8/22.
 */
public class WeeklyDetailArrayAdapter extends ArrayAdapter<String>
{

    private static int spinnerId = 0;
    private static int mLastPrice = 0;

    private int layoutId;
    private Context mContext;
    private String[] recordList ;

    private StringBuilder dateStrBuilder;

    public WeeklyDetailArrayAdapter(Context context, int resource, String[] list) {

        super(context, resource, list);
        mContext = context;
        layoutId = resource;
        recordList = list;

        dateStrBuilder = new StringBuilder("2000-00-00");

    }

    public static void setSpinnerId(int theId) {
        spinnerId = theId;
    }

    public static void setLastPrice(int lastPrice) {
        mLastPrice = lastPrice;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        View listViewContainer = v ;

        if(listViewContainer == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listViewContainer = inflater.inflate(layoutId, null);
        }

        TextView itemForTime = (TextView) listViewContainer.findViewById(R.id.item_for_time);
        TextView itemForPrice = (TextView) listViewContainer.findViewById(R.id.item_for_price);
        TextView itemForPriceCount = (TextView) listViewContainer.findViewById(R.id.item_for_pricecount);
        TextView itemForVolume = (TextView) listViewContainer.findViewById(R.id.item_for_volume);
        TextView itemForMoney = (TextView) listViewContainer.findViewById(R.id.item_for_money);

        String[] itemsOfRecord = recordList[position].split("\t");

        // red color : 0xFFBE0D15;
        // green color : 0xFF00C600;
        // white color : 0xFFFFFFFF;
        // yellow color : 0xFFFFFF00
        if (position == 0) {

            itemForTime.setText(itemsOfRecord[0]);
            itemForPrice.setText(itemsOfRecord[1]);
            itemForPriceCount.setText(itemsOfRecord[2]);
            itemForVolume.setText(itemsOfRecord[3]);
            itemForMoney.setText(itemsOfRecord[4]);

            itemForTime.setTextColor(0xFFFFFFFF);
            itemForPrice.setTextColor(0xFFFFFFFF);
            itemForPriceCount.setTextColor(0xFFFFFFFF);
            itemForVolume.setTextColor(0xFFFFFFFF);
            itemForMoney.setTextColor(0xFFFFFFFF);

            return listViewContainer;
        }

        switch(spinnerId) {
            case 0 :    // 34笔成交量最多的交易汇总

                itemForPrice.setText(itemsOfRecord[1]);

                if (position == 1) {
                    itemForTime.setText("");
                    itemForPriceCount.setText("总手:");
                    itemForVolume.setText(String.format("%.2f", ((float)Long.parseLong(itemsOfRecord[3])) / 10000) );
                    itemForVolume.setTextColor(0xFFFFFF00);
                    itemForMoney.setText("万");

                    break;
                }

                if (!dateStrBuilder.toString().equals(itemsOfRecord[0])) {
                    dateStrBuilder.replace(0, 10, itemsOfRecord[0]);
                    itemForTime.setText(itemsOfRecord[0]);
                } else {
                    itemForTime.setText("");
                }

                itemForPriceCount.setText("");
                itemForVolume.setText( String.format("%,.2f", Float.parseFloat(itemsOfRecord[2]) / 100) );
                itemForMoney.setText( String.format("%,.2f", Float.parseFloat(itemsOfRecord[3]) / 10000) );

                if ( (Integer.parseInt(itemsOfRecord[1].replace(".", "")) - mLastPrice) > 0 ) {
                    itemForTime.setTextColor(0xFFBE0D15);
                    itemForPrice.setTextColor(0xFFBE0D15);
                    itemForVolume.setTextColor(0xFFBE0D15);
                    itemForMoney.setTextColor(0xFFBE0D15);
                }
                if ( (Integer.parseInt(itemsOfRecord[1].replace(".", "")) - mLastPrice) < 0 ) {
                    itemForTime.setTextColor(0xFF00C600);
                    itemForPrice.setTextColor(0xFF00C600);
                    itemForVolume.setTextColor(0xFF00C600);
                    itemForMoney.setTextColor(0xFF00C600);
                }

                break;

            case 1 :	// 周概况

                itemForTime.setText(itemsOfRecord[0]);
                itemForPrice.setText(itemsOfRecord[1]);
                itemForPriceCount.setText(itemsOfRecord[2]);
                itemForVolume.setText(String.format("%,.3f", Float.parseFloat(itemsOfRecord[3]) / 10000));
                itemForMoney.setText( String.format("%,.3f", Float.parseFloat(itemsOfRecord[4]) / 1000000) );

                break;
            case 2 :	// 交易 按时间排列
            case 3 :	// 交易 按价格排列
            case 4 :    // 交易 按成交量排列
            case 5 :    // 交易 按成交次数排列
                //  firstTime, maxSingleVolTime, minSingleVolTime, price, tranCount, vol, money, maxSingleVol, minSingleVol
                // 		日 ：	firstTime	price 	tran-counter	volume		money
                //				maxVolTime                          vol
                //              minVolTime                          vol

                if (!dateStrBuilder.toString().equals(itemsOfRecord[0])) {
                    dateStrBuilder.replace(0, 10, itemsOfRecord[0]);
                    itemForTime.setText(itemsOfRecord[0]);
                } else {
                    itemForTime.setText("");
                }

                itemForPrice.setText(itemsOfRecord[1]);
                itemForPriceCount.setText(itemsOfRecord[2]);

                itemForVolume.setText( String.format("%,.3f", Float.parseFloat(itemsOfRecord[3]) / 10000) );
                itemForMoney.setText( String.format("%,.3f", Float.parseFloat(itemsOfRecord[4]) / 1000000) );

                if ( (Integer.parseInt(itemsOfRecord[1].replace(".", "")) - mLastPrice) > 0 ) {
                    itemForTime.setTextColor(0xFFBE0D15);
                    itemForPrice.setTextColor(0xFFBE0D15);
                    itemForPriceCount.setTextColor(0xFFBE0D15);
                    itemForVolume.setTextColor(0xFFBE0D15);
                    itemForMoney.setTextColor(0xFFBE0D15);
                }
                if ( (Integer.parseInt(itemsOfRecord[1].replace(".", "")) - mLastPrice) < 0 ) {
                    itemForTime.setTextColor(0xFF00C600);
                    itemForPrice.setTextColor(0xFF00C600);
                    itemForPriceCount.setTextColor(0xFF00C600);
                    itemForVolume.setTextColor(0xFF00C600);
                    itemForMoney.setTextColor(0xFF00C600);
                }

                break;
            default :
                break;
        }

        return listViewContainer;
    }

}