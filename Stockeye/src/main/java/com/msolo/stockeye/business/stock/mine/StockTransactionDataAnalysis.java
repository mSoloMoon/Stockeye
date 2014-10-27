package com.msolo.stockeye.business.stock.mine;

import com.msolo.stockeye.service.common.CommonServiceFacade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

/**
 *
 * apply : Abstract Factory Pattern - AbstractFactory
 *
 * Created by mSolo on 2014/8/23.
 */
public abstract class StockTransactionDataAnalysis {

    protected int highest;
    protected int lowest;
    protected int totalTranCount;
    protected int dateCount;

    protected long totalVol;
    protected long totalMoney;
    protected long singleSumVol;

    private Map<String, String> mapSingleRec = new HashMap<String, String>();
    private Map<String, Long> mapPriceVolForSingleRec = new HashMap<String, Long>();

    private Map<String, String> mapPriceDate = new LinkedHashMap<String, String>();
    private Map<String, Integer> mapPriceTranCount = new LinkedHashMap<String, Integer>();
    private Map<String, Long> mapPriceTotalVol = new LinkedHashMap<String, Long>();
    private Map<String, Long> mapPriceTotalMoney = new LinkedHashMap<String, Long>();

    private StringBuilder eachDayBuilder = null;

    protected void classifyingEachPriceStat(String theDate, String[] priceStat) {

        for (String priceStatRec : priceStat) {

            // 时间        priceStatComb[0]
            // 价格        priceStatComb[1]
            // 交易数      priceStatComb[2]
            // 成交量      priceStatComb[3]
            // 成交额      priceStatComb[4]
            String[] priceStatComb = priceStatRec.split("\t");

            if (mapPriceTranCount.containsKey(priceStatComb[1])) {

                mapPriceTranCount.put(priceStatComb[1], Integer.parseInt(priceStatComb[2]) + mapPriceTranCount.get(priceStatComb[1]));
                mapPriceTotalVol.put( priceStatComb[1], Long.parseLong(priceStatComb[3].split("\n")[0]) + mapPriceTotalVol.get(priceStatComb[1]) );
                mapPriceTotalMoney.put( priceStatComb[1], Long.parseLong(priceStatComb[4]) + mapPriceTotalMoney.get(priceStatComb[1]) );

            } else {

                if (theDate.length() < 10) {
                    mapPriceDate.put( priceStatComb[1], priceStatComb[0] );
                    mapPriceTranCount.put( priceStatComb[1], Integer.parseInt(priceStatComb[2]) );
                    mapPriceTotalVol.put( priceStatComb[1], Long.parseLong(priceStatComb[3].split("\n")[0]) );
                    mapPriceTotalMoney.put( priceStatComb[1], Long.parseLong(priceStatComb[4]) );
                } else {
                    mapPriceDate.put( priceStatComb[1], theDate );
                    mapPriceTranCount.put( priceStatComb[1], Integer.parseInt(priceStatComb[2]) );
                    mapPriceTotalVol.put( priceStatComb[1], Long.parseLong(priceStatComb[3].split("\n")[0]) );
                    mapPriceTotalMoney.put( priceStatComb[1], Long.parseLong(priceStatComb[4]) );
                }

            }

        }
    }

    protected void formatEachDayRecord(int startDateIntFmt, int openPrice, int closePrice, int highestPrice, int lowestPrice,
                                     int priceCount, int tranCount, long vol, long money) {

        eachDayBuilder.append(CommonServiceFacade.getInstance().getDateString(startDateIntFmt)).append("\t")
                .append(String.format("%.2f", (float) openPrice / 100)).append("\n")
                .append(String.format("%.2f", (float) closePrice / 100)).append("\n")
                .append(String.format("%.2f", (float) highestPrice / 100)).append("\n")
                .append(String.format("%.2f", (float) lowestPrice / 100)).append("\t")
                .append(priceCount).append("\n")
                .append(tranCount).append("\t")
                .append(vol).append("\t")
                .append(money).append("=");

    }

    /**
     *
     * aggregate those 34 single max vol records
     *      time/date   price   vol     money
     * <p>
     * Note: split by "\t"
     *
     * @param theDate
     * @param singleRecQueue
     *
     */
    protected void formatSingleRecStat(String theDate, Queue<String> singleRecQueue) {

        int singRecQueueSize = singleRecQueue.size();
        for (int i=0; i<singRecQueueSize; i++) {

            String[] singleRecComb = singleRecQueue.poll().split("\t");
            if ( mapSingleRec.containsKey(singleRecComb[1]) ) {

                String[] wmqySingleRecComb = mapSingleRec.get(singleRecComb[1]).split("\t");

                mapSingleRec.put(singleRecComb[1], new StringBuilder().append(wmqySingleRecComb[0]).append("\t")
                        .append(singleRecComb[1]).append("\t")
                        .append(Long.valueOf(wmqySingleRecComb[2]) + Long.valueOf(singleRecComb[2])).append("\t")
                        .append(Long.valueOf(wmqySingleRecComb[3]) + Long.valueOf(singleRecComb[3]))
                        .toString());

                mapPriceVolForSingleRec.put(singleRecComb[1], Long.valueOf(wmqySingleRecComb[2]) + Long.valueOf(singleRecComb[2]));

            } else {

                if (theDate.length() < 10) {
                    mapSingleRec.put(singleRecComb[1], new StringBuilder().append(singleRecComb[0]).append("\t")
                            .append(singleRecComb[1]).append("\t")
                            .append(singleRecComb[2]).append("\t")
                            .append(singleRecComb[3])
                            .toString());
                } else {
                    mapSingleRec.put(singleRecComb[1], new StringBuilder().append(theDate).append("\t")
                            .append(singleRecComb[1]).append("\t")
                            .append(singleRecComb[2]).append("\t")
                            .append(singleRecComb[3])
                            .toString());
                }

                mapPriceVolForSingleRec.put(singleRecComb[1], Long.valueOf(singleRecComb[2]));
            }

        }
    }

    protected StringBuilder getEachDayBuilder() {
        return eachDayBuilder;
    }

    protected String getEachDayRecord() {
        return eachDayBuilder.deleteCharAt( eachDayBuilder.length() - 1 ).toString();
    }

    protected int getPriceCount() {
        return mapPriceDate.size();
    }

    protected String getSingleRec() {

        StringBuilder singleRecs = new StringBuilder();

        ArrayList<Map.Entry<String,Long>> singleRecSortlist = new ArrayList<Map.Entry<String,Long>>(mapPriceVolForSingleRec.entrySet());
        Collections.sort(singleRecSortlist, new Comparator<Map.Entry<String, Long>>() {

            // sort by vol
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }

        });

        int size = singleRecSortlist.size();
        for (int i=0; i<size; i++) {
            singleRecs.append( mapSingleRec.get( singleRecSortlist.get(i).getKey() ) ).append("=");
        }

        return singleRecs.deleteCharAt(singleRecs.length() - 1).toString();

    }

    protected String getPriceStat() {

        StringBuilder priceStat = new StringBuilder();

        Iterator<String> keyIterator = mapPriceDate.keySet().iterator();
        while(keyIterator.hasNext()) {

            String key = keyIterator.next();

            priceStat.append(mapPriceDate.get(key)).append("\t")
                    .append(key).append("\t")
                    .append(mapPriceTranCount.get(key)).append("\t")
                    .append(mapPriceTotalVol.get(key)).append("\t")
                    .append(mapPriceTotalMoney.get(key)).append("=");

        }

        priceStat.deleteCharAt(priceStat.length() - 1);

        return priceStat.toString();

    }

    protected void initMemory() {

        highest = 0;
        lowest = 0;
        totalTranCount = 0;
        dateCount = 0;
        totalVol = 0l;
        totalMoney = 0l;
        singleSumVol = 0l;

        mapSingleRec.clear();
        mapPriceVolForSingleRec.clear();

        mapPriceDate.clear();
        mapPriceTranCount.clear();
        mapPriceTotalVol.clear();
        mapPriceTotalMoney.clear();

        eachDayBuilder = new StringBuilder();

    }

    protected void sumTranCountAndVolAndMoneyAndSingleVol(int tranCount, long vol, long money, long singleVol) {

        totalTranCount += tranCount;

        totalVol += vol;
        totalMoney += money;
        singleSumVol += singleVol;

    }

}