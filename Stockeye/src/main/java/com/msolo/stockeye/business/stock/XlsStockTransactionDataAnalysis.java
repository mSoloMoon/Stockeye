package com.msolo.stockeye.business.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by mSolo on 2014/8/18.
 */
public class XlsStockTransactionDataAnalysis {

    private ArrayList<String> xlsOfTime = new ArrayList<String>();
    private ArrayList<String> xlsOfPrice = new ArrayList<String>();
    private ArrayList<String> xlsOfVol = new ArrayList<String>();
    private ArrayList<String> xlsOfMoney = new ArrayList<String>();

    private TreeMap<Float, Integer> priceOfFifteen = new TreeMap<Float, Integer>();

    private Map<String, Integer> mapPriceTranCount = new LinkedHashMap<String, Integer>();
    private Map<String, Long> mapPriceTotalVol = new LinkedHashMap<String, Long>();
    private Map<String, Long> mapPriceTotalMoney = new LinkedHashMap<String, Long>();
    private Map<String, Long> mapPriceMaxSingleVol = new LinkedHashMap<String, Long>();
    private Map<String, Long> mapPriceMinSingleVol = new LinkedHashMap<String, Long>();

    private Map<String, String> mapPriceBeginTime = new LinkedHashMap<String, String>();
    private Map<String, String> mapPriceEndTime = new LinkedHashMap<String, String>();
    private Map<String, String> mapPriceMaxSingleVolTime = new LinkedHashMap<String, String>();
    private Map<String, String> mapPriceMinSingleVolTime = new LinkedHashMap<String, String>();
    private Map<String, String> mapPriceEndTimeVol = new LinkedHashMap<String, String>();

    private Map<Float, String> mapFloatPriceToStringPrice = new TreeMap<Float, String>();

    protected XlsStockTransactionDataAnalysis() {}

    protected void analyzeXlsContentToObjTranDailyRec(String xlsContent, String[] dailyRecordItemArray) {

        prepareMemory();

        int size = splitXlsContent(xlsContent.split("\n"), dailyRecordItemArray);

        splitForSingleMaxVol(size, dailyRecordItemArray);
        splitForFifteenMinute(size, dailyRecordItemArray);
        splitForPriceStatistic(size, dailyRecordItemArray);

    }

    private int splitXlsContent(String[] xlsContent, String[] dailyRecordItemArray) {

        long totalVol = 0l, totalMoney = 0l;
        for ( int i=(xlsContent.length-1); i>0; i-- ) {

            String[] itemsOfTranRec = xlsContent[i].split("\t");

            int pointIndex = itemsOfTranRec[1].indexOf('.');

            xlsOfTime.add(itemsOfTranRec[0]);
            if (itemsOfTranRec[1].length() - pointIndex > 2) {
                xlsOfPrice.add(itemsOfTranRec[1].substring(0, pointIndex+3));
            } else {
                xlsOfPrice.add(itemsOfTranRec[1].substring(0, pointIndex+2) + "0");
            }

            xlsOfVol.add(itemsOfTranRec[3]);
            xlsOfMoney.add(itemsOfTranRec[4]);

            totalVol += Long.parseLong(itemsOfTranRec[3]);
            totalMoney += Long.parseLong(itemsOfTranRec[4]);
        }

        dailyRecordItemArray[3] = xlsOfPrice.get(0).replace(".", "");
        dailyRecordItemArray[4] = xlsOfPrice.get(xlsOfPrice.size() - 1).replace(".", "");

        dailyRecordItemArray[8] = String.valueOf( xlsOfTime.size() );
        dailyRecordItemArray[9] = String.valueOf( totalVol );
        dailyRecordItemArray[10] = String.valueOf( totalMoney );

        return xlsOfTime.size();

    }

    private void splitForSingleMaxVol(int sizeOfTranRecArray, String[] dailyRecordItemArray) {

        TreeMap<Long, Long> xlsOfVolTreeMap = new TreeMap<Long, Long>();
        HashMap<Long, String> xlsOfSameVolTimeIndexHashMap = new HashMap<Long, String>();
        for (int i=0; i<sizeOfTranRecArray; i++) {

            Long volLong = Long.valueOf(xlsOfVol.get(i));
            if ( xlsOfVolTreeMap.containsKey(volLong) ) {
                StringBuilder timeIndex = new StringBuilder();

                if ( xlsOfSameVolTimeIndexHashMap.containsKey(volLong) ) {
                    timeIndex.append( xlsOfSameVolTimeIndexHashMap.get(volLong) ).append(i).append("_");
                } else {
                    timeIndex.append(i).append("_");
                }

                xlsOfSameVolTimeIndexHashMap.put( volLong, timeIndex.toString() );
                continue;
            }

            xlsOfVolTreeMap.put(volLong, Long.valueOf(i));

        }

        long singleSumVol = 0l;
        StringBuilder singleRecQueueStrBuilder = new StringBuilder();
        for (int i=0; i<34; i++) {

            Map.Entry<Long, Long> entry = xlsOfVolTreeMap.pollLastEntry();

            long vol = entry.getKey().longValue();

            singleSumVol += vol;

            int location = entry.getValue().intValue();
            singleRecQueueStrBuilder.append( xlsOfTime.get(location) ).append( "\t" )
                    .append( xlsOfPrice.get(location) ).append( "\t" )
                    .append( vol ).append( "\t" )
                    .append( xlsOfMoney.get(location) ).append("_");

            if ( xlsOfSameVolTimeIndexHashMap.containsKey( Long.valueOf(vol) ) ) {

                singleSumVol += vol;

                String[] timeIndexArray = xlsOfSameVolTimeIndexHashMap.get( Long.valueOf(vol) ).split("_");

                for ( int j=0, size=timeIndexArray.length-2; j<size; j++ ) {

                    location = Integer.parseInt(timeIndexArray[j]);

                    singleRecQueueStrBuilder.append( xlsOfTime.get(location) ).append( "\t" )
                            .append( xlsOfPrice.get(location) ).append( "\t" )
                            .append( vol ).append( "\t" )
                            .append( xlsOfMoney.get(location) ).append("_");

                    i++;

                }

            }

        }

        dailyRecordItemArray[12] = String.valueOf(singleSumVol);
        dailyRecordItemArray[13] = singleRecQueueStrBuilder.toString();

    }

    private void splitForFifteenMinute(int sizeOfTranRecArray, String[] dailyRecordItemArray) {

        StringBuilder fifteenMinuteStrBuilder = new StringBuilder();

        int[] fifteenMinuteSplitIndexArray = getFifteenMinuteSplitIndexArray(sizeOfTranRecArray);

        int lengthOfFifteenMinuteArray = fifteenMinuteSplitIndexArray.length;
        int indexOfTranRecArray = 0;
        for (int i=0; i<lengthOfFifteenMinuteArray; i++) {

            priceOfFifteen.clear();

            int priceCount = 0;
            int tranCount = 0;
            long sumVolOfFifteen = 0;
            long sumMoneyOfFifteen = 0;

            for (; indexOfTranRecArray < fifteenMinuteSplitIndexArray[i]; indexOfTranRecArray++) {

                priceOfFifteen.put( Float.parseFloat(xlsOfPrice.get(indexOfTranRecArray)),
                        Integer.valueOf(indexOfTranRecArray) );

                tranCount += 1;
                sumVolOfFifteen += Long.parseLong( xlsOfVol.get(indexOfTranRecArray) );
                sumMoneyOfFifteen += Long.parseLong( xlsOfMoney.get(indexOfTranRecArray) );
            }

            priceCount = priceOfFifteen.size();
            if ( priceCount == 0 ) {

                fifteenMinuteStrBuilder.append("0:0\n0.00\n0").append("\t")
                        .append("0:0\n0.00\n0").append("\t")
                        .append(0).append("/")
                        .append(0).append("\t")
                        .append(0).append("\t")
                        .append(0).append("_");

                continue;

            }

            int location1 = priceOfFifteen.firstEntry().getValue().intValue();
            int location2 = priceOfFifteen.lastEntry().getValue().intValue();
            StringBuilder timePriceRec1 = new StringBuilder();
            StringBuilder timePriceRec2 = new StringBuilder();

            if (location1 < location2) {
                timePriceRec1.append( xlsOfTime.get(location1) ).append("\n")
                        .append( xlsOfPrice.get(location1) ).append("\n")
                        .append( xlsOfVol.get(location1) );
                timePriceRec2.append( xlsOfTime.get(location2) ).append("\n")
                        .append( xlsOfPrice.get(location2) ).append("\n")
                        .append( xlsOfVol.get(location2) );
            } else {
                timePriceRec1.append( xlsOfTime.get(location2) ).append("\n")
                        .append( xlsOfPrice.get(location2) ).append("\n")
                        .append( xlsOfVol.get(location2) );
                timePriceRec2.append( xlsOfTime.get(location1) ).append("\n")
                        .append( xlsOfPrice.get(location1) ).append("\n")
                        .append( xlsOfVol.get(location1) );
            }

            fifteenMinuteStrBuilder.append(timePriceRec1).append("\t")
                    .append(timePriceRec2).append("\t")
                    .append(priceCount).append("/")
                    .append(tranCount).append("\t")
                    .append(sumVolOfFifteen).append("\t")
                    .append(sumMoneyOfFifteen).append("_");

        }

        dailyRecordItemArray[14] = new String( fifteenMinuteStrBuilder.toString() );

    }

    private void splitForPriceStatistic(int sizeOfTranRecArray, String[] dailyRecordItemArray) {

        String openPrice = xlsOfPrice.get(0);
        String openVol = xlsOfVol.get(0);

        mapPriceBeginTime.put(openPrice, xlsOfTime.get(0));
        mapPriceEndTime.put(openPrice, xlsOfTime.get(0));
        mapPriceTranCount.put( openPrice, 1 );
        mapPriceTotalVol.put( openPrice, Long.parseLong(openVol) );
        mapPriceTotalMoney.put( openPrice, Long.parseLong(xlsOfMoney.get(0)) );
        mapPriceMaxSingleVol.put( openPrice, Long.parseLong(openVol) );
        mapPriceMinSingleVol.put( openPrice, Long.parseLong(openVol) );
        mapPriceMaxSingleVolTime.put( openPrice, xlsOfTime.get(0) );
        mapPriceMinSingleVolTime.put( openPrice, xlsOfTime.get(0) );
        mapPriceEndTimeVol.put( openPrice, openVol );

        for (int i=1; i<sizeOfTranRecArray; i++) {

            String price = xlsOfPrice.get(i);
            String time = xlsOfTime.get(i);
            long vol = Long.parseLong(xlsOfVol.get(i));
            long money = Long.parseLong(xlsOfMoney.get(i));

            if ( !mapPriceTranCount.containsKey(price) ) {

                mapPriceBeginTime.put(price, time);
                mapPriceEndTime.put(price, time);
                mapPriceTranCount.put(price, 1);
                mapPriceTotalVol.put( price, vol );
                mapPriceTotalMoney.put( price, money );
                mapPriceMaxSingleVol.put( price, vol );
                mapPriceMinSingleVol.put( price, vol );
                mapPriceMaxSingleVolTime.put(price, time );
                mapPriceMinSingleVolTime.put(price, time );
                mapPriceEndTimeVol.put(price, String.valueOf(vol) );

            } else {

                int transCount = mapPriceTranCount.get(price) + 1;
                long transVol =  mapPriceTotalVol.get(price) + vol;
                long transMoney = mapPriceTotalMoney.get(price) + money;

                mapPriceEndTime.put(price, time);
                mapPriceTranCount.put(price, transCount);
                mapPriceTotalVol.put(price, transVol);
                mapPriceTotalMoney.put(price, transMoney);
                mapPriceEndTimeVol.put(price, String.valueOf(vol) );

                if ( mapPriceMaxSingleVol.get(price) < vol ) {
                    mapPriceMaxSingleVol.put(price, vol);
                    mapPriceMaxSingleVolTime.put(price, time);
                }

                if ( mapPriceMinSingleVol.get(price) > vol ) {
                    mapPriceMinSingleVol.put(price, vol);
                    mapPriceMinSingleVolTime.put(price, time);
                }
            }
        }

        dailyRecordItemArray[7] = String.valueOf( mapPriceTranCount.size() );

        Set<String> priceSet = mapPriceTranCount.keySet();
        Iterator<String> iterator = priceSet.iterator();
        while ( iterator.hasNext() ) {
            String price = iterator.next();
            mapFloatPriceToStringPrice.put( Float.parseFloat(price), price );
        }

        Iterator<Float> iteratorFloat = mapFloatPriceToStringPrice.keySet().iterator();
        float lowest = iteratorFloat.next().floatValue();
        float highest = 0.00f;
        while( iteratorFloat.hasNext() ) {
            highest = iteratorFloat.next().floatValue();
        }

        dailyRecordItemArray[5] = String.format("%.2f", highest).replace(".", "");
        dailyRecordItemArray[6] = String.format("%.2f", lowest).replace(".", "");

        StringBuilder priceStat = new StringBuilder();

        Iterator<String> priceKey = mapPriceBeginTime.keySet().iterator();
        while ( priceKey.hasNext() ) {
            String key = priceKey.next();
            priceStat.append( mapPriceBeginTime.get(key) ).append("\n")
                    .append(mapPriceMaxSingleVolTime.get(key)).append("\n")
                    .append( mapPriceMinSingleVolTime.get(key) ).append("\n")
                    .append( mapPriceEndTime.get(key) ).append("\t")
                    .append( key ).append("\t")
                    .append( mapPriceTranCount.get(key) ).append("\t")
                    .append( mapPriceTotalVol.get(key) ).append("\n")
                    .append( mapPriceMaxSingleVol.get(key) ).append("\n")
                    .append( mapPriceMinSingleVol.get(key) ).append("\n")
                    .append( mapPriceEndTimeVol.get(key) ).append("\t")
                    .append(mapPriceTotalMoney.get(key)).append("=");
        }

        priceStat.deleteCharAt(priceStat.length() - 1);

        dailyRecordItemArray[15] = new String(priceStat.toString());

        int lastCutPointIndex = priceStat.lastIndexOf("=");
        String endPriceTime = priceStat.substring(lastCutPointIndex + 1, lastCutPointIndex + 6);

        dailyRecordItemArray[11] = String.valueOf( calculateTimeGap(endPriceTime) );

    }

    //
    // =============================================================================
    //

    private int calculateTimeGap(String endTime) {

        int endTimeHour = Integer.parseInt(endTime.substring(0,2));
        int endTimeMinute = Integer.parseInt(endTime.substring(3,5));

        int sumTimeMinute = endTimeMinute + 5;
        if (endTimeHour >= 13 && endTimeHour < 14) {
            if ( sumTimeMinute < 60 ) {
                return Integer.parseInt( String.format("2%02d", sumTimeMinute) );
            } else {
                return Integer.parseInt( String.format("3%02d", sumTimeMinute - 60) );
            }
        }

        if (endTimeHour >=14) {
            return Integer.parseInt( String.format("3%02d", sumTimeMinute) );
        }

        if (endTimeMinute <= 25) {
            return Integer.parseInt( new StringBuilder().append(endTimeHour - 10).append(String.format("%02d", 60 - 25 + endTimeMinute)).toString() );
        }

        return Integer.parseInt( new StringBuilder().append(endTimeHour - 9).append(String.format("%02d", endTimeMinute - 25)).toString() );
    }

    private int[] getFifteenMinuteSplitIndexArray(int sizeOfTranRecArray) {

        int[] fifteenMinuteSplitIndexArray = new int[16];

        int[] fifteenMinuteTimes = new int[]{
                94500, 100000, 101500, 103000, 104500, 110000, 111500, 113000,
                131500, 133000, 134500, 140000, 141500, 143000, 144500 };

        int startIndex = 0;
        for (int i=0; i<15; i++) {

            for (; startIndex < sizeOfTranRecArray; startIndex++) {

                int timeIntFormat = Integer.parseInt( xlsOfTime.get(startIndex).replace(":", "") );
                if (timeIntFormat <= fifteenMinuteTimes[i]) {
                    continue;
                } else {
                    fifteenMinuteSplitIndexArray[i] = startIndex;
                    startIndex++;
                    break;
                }
            }
        }

        fifteenMinuteSplitIndexArray[15] = sizeOfTranRecArray;

        return fifteenMinuteSplitIndexArray;
    }

    private void prepareMemory() {

        xlsOfTime.clear();
        xlsOfPrice.clear();
        xlsOfVol.clear();
        xlsOfMoney.clear();

        priceOfFifteen.clear();

        mapPriceBeginTime.clear();
        mapPriceEndTime.clear();
        mapPriceTranCount.clear();
        mapPriceTotalVol.clear();
        mapPriceTotalMoney.clear();
        mapPriceMaxSingleVol.clear();
        mapPriceMinSingleVol.clear();
        mapPriceMaxSingleVolTime.clear();
        mapPriceMinSingleVolTime.clear();
        mapPriceEndTimeVol.clear();

        mapFloatPriceToStringPrice.clear();

    }

}
