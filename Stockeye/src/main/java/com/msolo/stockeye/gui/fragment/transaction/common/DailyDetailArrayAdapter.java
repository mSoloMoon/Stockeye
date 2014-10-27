package com.msolo.stockeye.gui.fragment.transaction.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.msolo.stockeye.R;
import com.msolo.stockeye.gui.UtilViewTool;

/**
 * Created by mSolo on 2014/8/22.
 */
public class DailyDetailArrayAdapter extends ArrayAdapter<String>
{

    private static int spinnerId = 0;
    private static int mLastPrice = 0;

    private int layoutId;
    private Context mContext;
    private String[] recordList ;

    static class ViewHolder {
        TextView itemForTime;
        TextView itemForPrice;
        TextView itemForPriceCount;
        TextView itemForVolume;
        TextView itemForMoney;
    }

    public DailyDetailArrayAdapter(Context context, int resource, String[] list) {

        super(context, resource, list);
        mContext = context;
        layoutId = resource;
        recordList = list;

    }

    public static void setSpinnerId(int theId) {
        spinnerId = theId;
    }

    public static void setLastPrice(int lastPrice) {
        mLastPrice = lastPrice;
    }

    @Override
    public View getView(int position, View listViewContainer, ViewGroup parent) {

        ViewHolder holder;

        if (listViewContainer == null) {

            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listViewContainer = inflater.inflate(layoutId, null);

            holder = new ViewHolder();
            listViewContainer.setTag(holder);

        } else {
            holder = (ViewHolder) listViewContainer.getTag();
        }

        holder.itemForTime = (TextView) listViewContainer.findViewById(R.id.item_for_time);
        holder.itemForPrice = (TextView) listViewContainer.findViewById(R.id.item_for_price);
        holder.itemForPriceCount = (TextView) listViewContainer.findViewById(R.id.item_for_pricecount);
        holder.itemForVolume = (TextView) listViewContainer.findViewById(R.id.item_for_volume);
        holder.itemForMoney = (TextView) listViewContainer.findViewById(R.id.item_for_money);

        String[] itemsOfRecord = recordList[position].split("\t");

        // red color : 0xFFBE0D15;
        // green color : 0xFF00C600;
        // white color : 0xFFFFFFFF;
        // yellow color : 0xFFFFFF00
        if (position == 0) {

            holder.itemForTime.setText(itemsOfRecord[0]);
            holder.itemForPrice.setText(itemsOfRecord[1]);
            holder.itemForPriceCount.setText(itemsOfRecord[2]);
            holder.itemForVolume.setText(itemsOfRecord[3]);
            holder.itemForMoney.setText(itemsOfRecord[4]);

            holder.itemForTime.setTextColor(UtilViewTool.COLOR_WHITE);
            holder.itemForPrice.setTextColor(UtilViewTool.COLOR_WHITE);
            holder.itemForPriceCount.setTextColor(UtilViewTool.COLOR_WHITE);
            holder.itemForVolume.setTextColor(UtilViewTool.COLOR_WHITE);
            holder.itemForMoney.setTextColor(UtilViewTool.COLOR_WHITE);

            return listViewContainer;
        }

        switch(spinnerId) {
            case 0 :    // 前34笔成交量最多的交易信息

                holder.itemForTime.setText(itemsOfRecord[0]);
                holder.itemForPrice.setText(itemsOfRecord[1]);

                if (position == 1) {
                    holder.itemForPriceCount.setText("总手:");
                    holder.itemForVolume.setText(String.format("%.2f", ((float)Long.parseLong(itemsOfRecord[3])) / 10000) );
                    holder.itemForVolume.setTextColor(UtilViewTool.COLOR_YELLOW);
                    holder.itemForMoney.setText("万");

                    break;
                }

                holder.itemForPriceCount.setText("");
                holder.itemForVolume.setText( String.format("%,d", Integer.parseInt(itemsOfRecord[2])) );
                holder.itemForMoney.setText( String.format("%,d", Integer.parseInt(itemsOfRecord[3])) );

                if ( (Integer.parseInt(itemsOfRecord[1].replace(".", "")) - mLastPrice) > 0 ) {
                    holder.itemForPrice.setTextColor(UtilViewTool.COLOR_RED);
                    holder.itemForVolume.setTextColor(UtilViewTool.COLOR_RED);
                }
                if ( (Integer.parseInt(itemsOfRecord[1].replace(".", "")) - mLastPrice) < 0 ) {
                    holder.itemForPrice.setTextColor(UtilViewTool.COLOR_GREEN);
                    holder.itemForVolume.setTextColor(UtilViewTool.COLOR_GREEN);
                }
                break;

            case 1 :	// 15分钟交易概况

                holder.itemForTime.setText(itemsOfRecord[0]);
                holder.itemForPrice.setText(itemsOfRecord[1]);

                holder.itemForPriceCount.setText(itemsOfRecord[2]);
                holder.itemForVolume.setText( String.format("%,.3f", ((float)Long.parseLong(itemsOfRecord[3])) / 10000) );
                holder.itemForMoney.setText( String.format("%,.3f", ((float)Long.parseLong(itemsOfRecord[4])) / 10000) );

                if ( (Integer.parseInt(itemsOfRecord[0].split("\n")[1].replace(".", "")) - mLastPrice) > 0 ) {
                    holder.itemForTime.setTextColor(UtilViewTool.COLOR_RED);
                }
                if ( (Integer.parseInt(itemsOfRecord[0].split("\n")[1].replace(".", "")) - mLastPrice) < 0 ) {
                    holder.itemForTime.setTextColor(UtilViewTool.COLOR_GREEN);
                }
                if ( (Integer.parseInt(itemsOfRecord[1].split("\n")[1].replace(".", "")) - mLastPrice) > 0 ) {
                    holder.itemForPrice.setTextColor(UtilViewTool.COLOR_RED);
                }
                if ( (Integer.parseInt(itemsOfRecord[1].split("\n")[1].replace(".", "")) - mLastPrice) < 0 ) {
                    holder.itemForPrice.setTextColor(UtilViewTool.COLOR_GREEN);
                }

                break;
            case 2 :	// 交易 按时间排列
            case 3 :	// 交易 按价格排列
            case 4 :    // 交易 按成交量排列
            case 5 :    // 交易 按成交次数排列
                //  firstTime, maxSingleVolTime, minSingleVolTime, price, tranCount, vol, money, maxSingleVol, minSingleVol
                // 		日 ：	firstTime	price 	tran-counter	volume		money
                //				maxVolTime                          vol
                //              minVolTime                          vol

                holder.itemForTime.setText(itemsOfRecord[0]);
                holder.itemForPrice.setText(itemsOfRecord[1]);
                holder.itemForPriceCount.setText(itemsOfRecord[2]);

                String[] volItems = itemsOfRecord[3].split("\n");
                String volBuilder =String.format("%,d\n%,d\n%,d\n%,d",
                        Long.parseLong(volItems[0]),
                        Long.parseLong(volItems[1]),
                        Long.parseLong(volItems[2]),
                        Long.parseLong(volItems[3]));

                holder.itemForVolume.setText( volBuilder );
                holder.itemForMoney.setText( String.format("%.2f", Float.parseFloat(itemsOfRecord[4]) / 10000 ));

                if ( (Integer.parseInt(itemsOfRecord[1].replace(".", "")) - mLastPrice) > 0 ) {
                    holder.itemForPrice.setTextColor(UtilViewTool.COLOR_RED);
                    holder.itemForPriceCount.setTextColor(UtilViewTool.COLOR_RED);
                    holder.itemForVolume.setTextColor(UtilViewTool.COLOR_RED);
                }
                if ( (Integer.parseInt(itemsOfRecord[1].replace(".", "")) - mLastPrice) < 0 ) {
                    holder.itemForPrice.setTextColor(UtilViewTool.COLOR_GREEN);
                    holder.itemForPriceCount.setTextColor(UtilViewTool.COLOR_GREEN);
                    holder.itemForVolume.setTextColor(UtilViewTool.COLOR_GREEN);
                }

                break;

            default :
                break;
        }

        return listViewContainer;
    }

}
