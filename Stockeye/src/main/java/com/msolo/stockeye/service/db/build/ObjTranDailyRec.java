package com.msolo.stockeye.service.db.build;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by mSolo on 2014/8/16.
 */
public class ObjTranDailyRec extends ObjAbstractTranRec {

    private int weekday;

    private int priceTimeGap;

    private Queue<String> singleRecQueue;

    // fifteenMinuteRec:
    //      timeOfPrice1Rec + "\t" +       // timeOfPriceN: highest or lowest price
    //      timeOfPrice2Rec + "\t" +
    //      priceCount + "\t" +
    //      tranCount + "\t" +
    //      sumVol + "\t" +
    //      sumMoney + ""
    private Queue<String> fifteenMinuteRecQueue;

    public ObjTranDailyRec() {

        super();

        singleRecQueue = new LinkedList<String>();
        fifteenMinuteRecQueue = new LinkedList<String>();

    }

    public int getWeekday() {
        return weekday;
    }

    public int getLast() {
        return super.getLast();
    }

    public int getOpen() {
        return super.getOpen();
    }

    public int getClose() {
        return super.getClose();
    }

    public int getHighest() {
        return super.getHighest();
    }

    public int getLowest() {
        return super.getLowest();
    }

    public long getTotalVol() {
        return super.getTotalVol();
    }

    public long getTotalMoney() {
        return super.getTotalMoney();
    }

    public int getTotalTranCount() {
        return super.getTotalTranCount();
    }

    public int getTotalPriceCount() {
        return super.getTotalPriceCount();
    }

    public long getSingleSumVol() {
        return super.getSingleSumVol();
    }

    public Queue<String> getFifteenMinuteRecQueue() {
        return fifteenMinuteRecQueue;
    }

    public Queue<String> getSingleRecQueue() {
        return singleRecQueue;
    }

    public int getPriceTimeGap() {
        return priceTimeGap;
    }

    public String getPriceStat() {
        return super.getPriceStat();
    }

    // setter
    protected void setRecordId(int recordId) {
        super.setRecordId(recordId);
    }

    protected void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    protected void setLast(int last) {
        super.setLast(last);
    }

    protected void setOpen(int open) {
        super.setOpen(open);
    }

    protected void setClose(int close) {
        super.setClose(close);
    }

    protected void setHighest(int highest) {
        super.setHighest(highest);
    }

    protected void setLowest(int lowest) {
        super.setLowest(lowest);
    }

    protected void setTotalVol(long totalVol) {
        super.setTotalVol(totalVol);
    }

    protected void setTotalMoney(long totalMoney) {
        super.setTotalMoney(totalMoney);
    }

    protected void setTotalTranCount(int totalTranCount) {
        super.setTotalTranCount(totalTranCount);
    }

    protected void setTotalPriceCount(int totalPriceCount) {
        super.setTotalPriceCount(totalPriceCount);
    }

    protected void setSingleSumVol(long singleSumVol) {
        super.setSingleSumVol(singleSumVol);
    }

    protected void setPriceTimeGap(int priceTimeGap) {
        this.priceTimeGap = priceTimeGap;
    }

    protected void pushSingleRecQueue(String singleRec) {
        singleRecQueue.add(singleRec);
    }

    protected void pushFifteenMinuteRecQueue(String fifteenMinuteRec) {
        fifteenMinuteRecQueue.add(fifteenMinuteRec);
    }

    protected void setPriceStat(String priceStat) {
        super.setPriceStat(priceStat);
    }

    protected String[] toStringArray() {

        return
                new String[] {
                    String.valueOf(super.getRecordId()),
                    String.valueOf(weekday),
                    String.valueOf(super.getLast()),
                    String.valueOf(super.getOpen()),
                    String.valueOf(super.getClose()),
                    String.valueOf(super.getHighest()),
                    String.valueOf(super.getLowest()),

                    String.valueOf(super.getTotalPriceCount()),
                    String.valueOf(super.getTotalTranCount()),
                    String.valueOf(super.getTotalVol()),
                    String.valueOf(super.getTotalMoney()),

                    String.valueOf(priceTimeGap),
                    String.valueOf(super.getSingleSumVol()),

                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),
                    singleRecQueue.poll(),

                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),
                    fifteenMinuteRecQueue.poll(),

                    super.getPriceStat()
                };

    }

}
