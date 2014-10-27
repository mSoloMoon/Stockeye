package com.msolo.stockeye.service.db.build;

/**
 * Created by mSolo on 2014/8/16.
 */
public class ObjTranWMQYRec extends ObjAbstractTranRec {

    private int startDate;
    private int endDate;
    private int dateCount;

    private String singleRecStat;
    private String eachDayBasic;

    public ObjTranWMQYRec() {}

    public int getRecordId() {
        return super.getRecordId();
    }

    public int getStartDate() {
        return startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public int getDateCount() {
        return dateCount;
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

    public String getSingleRecStat() {
        return singleRecStat;
    }

    public String getEachDayBasic() {
        return eachDayBasic;
    }

    public String getPriceStat() {
        return super.getPriceStat();
    }

    // setter
    protected void setRecordId(int recordId) {
        super.setRecordId(recordId);
    }

    protected void setStartDate(int start) {
        this.startDate = start;
    }

    protected void setEndDate(int end) {
        this.endDate = end;
    }

    protected void setDateCount(int count) {
        dateCount = count;
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

    protected void setSingleRecStat(String singleRecs) {
        singleRecStat = singleRecs;
    }

    protected void setEachDayBasic(String eachDayBasic) {
        this.eachDayBasic = eachDayBasic;
    }

    protected void setPriceStat(String priceStat) {
        super.setPriceStat(priceStat);
    }

    public String[] toStringArray() {

        return
                new String[] {
                    String.valueOf(super.getRecordId()),
                    String.valueOf(startDate),
                    String.valueOf(endDate),
                    String.valueOf(dateCount),

                    String.valueOf(super.getLast()),
                    String.valueOf(super.getOpen()),
                    String.valueOf(super.getClose()),
                    String.valueOf(super.getHighest()),
                    String.valueOf(super.getLowest()),

                    String.valueOf(super.getTotalPriceCount()),
                    String.valueOf(super.getTotalTranCount()),
                    String.valueOf(super.getTotalVol()),
                    String.valueOf(super.getTotalMoney()),

                    String.valueOf(super.getSingleSumVol()),

                    singleRecStat,
                    eachDayBasic,
                    super.getPriceStat()
                };

    }

}
