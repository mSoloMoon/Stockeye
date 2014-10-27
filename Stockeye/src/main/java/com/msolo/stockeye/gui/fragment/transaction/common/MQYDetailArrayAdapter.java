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
public class MQYDetailArrayAdapter extends ArrayAdapter<String> {

    private static int spinnerId;
    private static int mLastPrice;

    private int layoutId;
    private Context mContext;
    private String[] recordList;

    private StringBuilder dateStrBuilder;

    static class ViewHolder {
        TextView itemForTime;
        TextView itemForPrice;
        TextView itemForPriceCount;
        TextView itemForVolume;
        TextView itemForMoney;
    }

    public MQYDetailArrayAdapter(Context context, int resource, String[] list) {

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
    public View getView(int position, View listViewContainer, ViewGroup parent) {

        ViewHolder holder;

        if (listViewContainer == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listViewContainer = inflater.inflate(layoutId, null);

            holder = new ViewHolder();
            listViewContainer.setTag(holder);

        } else {

            holder = (ViewHolder)listViewContainer.getTag();

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

        switch (spinnerId) {
            case 0:    // 34笔成交量最多的交易汇总

                holder.itemForPrice.setText(itemsOfRecord[1]);

                if (position == 1) {
                    holder.itemForTime.setText("");
                    holder.itemForPriceCount.setText("总手:");
                    holder.itemForVolume.setText(String.format("%.2f", ((float) Long.parseLong(itemsOfRecord[3])) / 1000000));
                    holder.itemForVolume.setTextColor(UtilViewTool.COLOR_YELLOW);
                    holder.itemForMoney.setText("百万");

                    break;
                }

                if (!dateStrBuilder.toString().equals(itemsOfRecord[0])) {
                    dateStrBuilder.replace(0, 10, itemsOfRecord[0]);
                    holder.itemForTime.setText(itemsOfRecord[0]);
                } else {
                    holder.itemForTime.setText("");
                }

                holder.itemForPriceCount.setText("");
                holder.itemForVolume.setText(String.format("%,.2f", Float.parseFloat(itemsOfRecord[2]) / 10000));
                holder.itemForMoney.setText(String.format("%,.2f", Float.parseFloat(itemsOfRecord[3]) / 1000000));

                if ((Integer.parseInt(itemsOfRecord[1].replace(".", "")) - mLastPrice) > 0) {
                    holder.itemForTime.setTextColor(UtilViewTool.COLOR_RED);
                    holder.itemForPrice.setTextColor(UtilViewTool.COLOR_RED);
                    holder.itemForVolume.setTextColor(UtilViewTool.COLOR_RED);
                    holder.itemForMoney.setTextColor(UtilViewTool.COLOR_RED);
                }
                if ((Integer.parseInt(itemsOfRecord[1].replace(".", "")) - mLastPrice) < 0) {
                    holder.itemForTime.setTextColor(UtilViewTool.COLOR_GREEN);
                    holder.itemForPrice.setTextColor(UtilViewTool.COLOR_GREEN);
                    holder.itemForVolume.setTextColor(UtilViewTool.COLOR_GREEN);
                    holder.itemForMoney.setTextColor(UtilViewTool.COLOR_GREEN);
                }

                break;

            case 1:    // 月，季，年概况

                holder.itemForTime.setText(itemsOfRecord[0]);
                holder.itemForPrice.setText(itemsOfRecord[1]);
                holder.itemForPriceCount.setText(itemsOfRecord[2]);
                holder.itemForVolume.setText(String.format("%,.3f", Float.parseFloat(itemsOfRecord[3]) / 10000));
                holder.itemForMoney.setText(String.format("%,.3f", Float.parseFloat(itemsOfRecord[4]) / 1000000));

                break;
            case 2:    // 交易 按时间排列
            case 3:    // 交易 按价格排列
            case 4:    // 交易 按成交量排列
            case 5:    // 交易 按成交次数排列
                //  firstTime, maxSingleVolTime, minSingleVolTime, price, tranCount, vol, money, maxSingleVol, minSingleVol
                // 		日 ：	firstTime	price 	tran-counter	volume		money
                //				maxVolTime                          vol
                //              minVolTime                          vol

                if (!dateStrBuilder.toString().equals(itemsOfRecord[0])) {
                    dateStrBuilder.replace(0, 10, itemsOfRecord[0]);
                    holder.itemForTime.setText(itemsOfRecord[0]);
                } else {
                    holder.itemForTime.setText("");
                }
                holder.itemForPrice.setText(itemsOfRecord[1]);
                holder.itemForPriceCount.setText(itemsOfRecord[2]);

                holder.itemForVolume.setText(String.format("%,.3f", Float.parseFloat(itemsOfRecord[3]) / 10000));
                holder.itemForMoney.setText(String.format("%,.3f", Float.parseFloat(itemsOfRecord[4]) / 1000000));

                int theCmpPrice = 0;
                try {
                    theCmpPrice = Integer.parseInt(itemsOfRecord[1].replace(".", ""));
                } catch (NumberFormatException e) {
                    StringBuilder theCmpPriceBuilder = new StringBuilder();
                    for(int i=0; i<itemsOfRecord[1].length(); i++) {
                        if (itemsOfRecord[1].charAt(i) >= 48 && itemsOfRecord[1].charAt(i) <= 57) {
                            theCmpPriceBuilder.append(itemsOfRecord[1].charAt(i));
                        }
                    }
                    theCmpPrice = Integer.parseInt(theCmpPriceBuilder.toString());
                }

                if ( (theCmpPrice - mLastPrice) > 0 ) {
                    holder.itemForTime.setTextColor(UtilViewTool.COLOR_RED);
                    holder.itemForPrice.setTextColor(UtilViewTool.COLOR_RED);
                    holder.itemForPriceCount.setTextColor(UtilViewTool.COLOR_RED);
                    holder.itemForVolume.setTextColor(UtilViewTool.COLOR_RED);
                    holder.itemForMoney.setTextColor(UtilViewTool.COLOR_RED);
                }
                if ( (theCmpPrice - mLastPrice) < 0) {
                    holder.itemForTime.setTextColor(UtilViewTool.COLOR_GREEN);
                    holder.itemForPrice.setTextColor(UtilViewTool.COLOR_GREEN);
                    holder.itemForPriceCount.setTextColor(UtilViewTool.COLOR_GREEN);
                    holder.itemForVolume.setTextColor(UtilViewTool.COLOR_GREEN);
                    holder.itemForMoney.setTextColor(UtilViewTool.COLOR_GREEN);
                }

                break;
            default:
                break;
        }

        return listViewContainer;
    }

}