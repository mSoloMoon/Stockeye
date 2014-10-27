package com.msolo.stockeye.service.db.build;

/**
 * Created by mSolo on 2014/8/19.
 */
public abstract class ObjAbstractTranRec {

    private int recordId;
    private int last;
    private int open;
    private int close;
    private int highest;
    private int lowest;

    private long totalVol;
    private long totalMoney;
    private int totalTranCount;
    private int totalPriceCount;

    private long singleSumVol;

    private String priceStat;

    public ObjAbstractTranRec() {}

    public int getRecordId() {
        return recordId;
    }

    public int getLast() {
        return last;
    }

    public int getOpen() {
        return open;
    }

    public int getClose() {
        return close;
    }

    public int getHighest() {
        return highest;
    }

    public int getLowest() {
        return lowest;
    }

    public long getTotalVol() {
        return totalVol;
    }

    public long getTotalMoney() {
        return totalMoney;
    }

    public int getTotalTranCount() {
        return totalTranCount;
    }

    public int getTotalPriceCount() {
        return totalPriceCount;
    }

    public long getSingleSumVol() {
        return singleSumVol;
    }

    public String getPriceStat() {
        return priceStat;
    }

    // setter
    protected void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    protected void setLast(int last) {
        this.last = last;
    }

    protected void setOpen(int open) {
        this.open = open;
    }

    protected void setClose(int close) {
        this.close = close;
    }

    protected void setHighest(int highest) {
        this.highest = highest;
    }

    protected void setLowest(int lowest) {
        this.lowest = lowest;
    }

    protected void setTotalVol(long totalVol) {
        this.totalVol = totalVol;
    }

    protected void setTotalMoney(long totalMoney) {
        this.totalMoney = totalMoney;
    }

    protected void setTotalTranCount(int totalTranCount) {
        this.totalTranCount = totalTranCount;
    }

    protected void setTotalPriceCount(int totalPriceCount) {
        this.totalPriceCount = totalPriceCount;
    }

    protected void setSingleSumVol(long singleSumVol) {
        this.singleSumVol = singleSumVol;
    }

    protected void setPriceStat(String priceStat) {
        this.priceStat = priceStat;
    }

}
