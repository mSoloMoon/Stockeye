package com.msolo.stockeye.gui.fragment.transaction.customization;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;

import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.gui.UtilViewTool;

/**
 * Created by mSolo on 2014/9/2.
 */
public class StockPriceVolStrategyDrawCandleStick {

    // 0:maxHeight; 1:minLowest; 2:maxPriceCount; 3:minPriceCount; 4:maxTranCount; 5:minTranCount; 6:maxVol; 7:minVol; 8: maxMoney; 9:minMoney
    // 0:date, 1:weekday, 2:last, 3:open, 4:openFlag, 5:close, 6:closeFlag, 7:highest, 8:lowest, 9:priceCount, 10:tranCount, 11:vol, 12:money, 13:timeGap

    private int drawType;
    private int drawRecordCount;

    private int sizeOfChartDailyRecordArray;

    // --------------------------
    private int priceCount;
    private int volCount;

    private int drawWidth;
    private int drawStartX;

    private int drawPriceStartY;
    private int drawPriceEndY;
    private int drawVolStartY;
    private int drawVolEndY;

    // ----------------------------------
    private int maxHighest;
    private int minLowest;

    private int maxPriceCount;
    private int minPriceCount;
    private int maxTranCount;
    private int minTranCount;

    private long maxTimeGap;
    private long minTimeGap;

    private long maxVol;
    private long minVol;
    private long maxMoney;
    private long minMoney;

    // ----------------------------------
    private long[][] chartRecordArray;

    private SparseArray<long[]> recordSparseArray;

    public StockPriceVolStrategyDrawCandleStick() {}

    public void drawPriceVolView(Canvas canvas) {

        drawRecordCount = sizeOfChartDailyRecordArray > drawRecordCount ? drawRecordCount : sizeOfChartDailyRecordArray;

        filterPriceVolInfo();

        int textSize = StockeyeApp.isLargeScreen ? 24 : 18;
        int startX = StockeyeApp.isLargeScreen ? 25 : 15;

        drawPriceVolOfPriceInfo(startX, textSize, canvas);
        drawPriceVolOfVolInfo(startX, textSize, canvas);

        drawPriceVolRecord(canvas);

    }

    private void drawPriceVolOfPriceInfo(int startX, int textSize, Canvas canvas) {

        canvas.drawText( String.format("%.2f", (float)maxHighest / 100),
                startX, drawPriceEndY+10, UtilViewTool.getBrush(1, UtilViewTool.COLOR_RED, textSize) );
        canvas.drawText( String.format("%.2f", (float)minLowest / 100),
                startX, drawPriceStartY+10, UtilViewTool.getBrush(1, UtilViewTool.COLOR_GREEN, textSize) );

        int eachPriceLength = (drawPriceStartY - drawPriceEndY) / priceCount;
        int eachPrice = (maxHighest - minLowest) / priceCount;
        for (int i=1; i<priceCount; i++) {
            canvas.drawText( String.format("%.2f", (float)(maxHighest - eachPrice*i) / 100),
                    startX, drawPriceEndY+10+eachPriceLength*i, UtilViewTool.getBrush(1, UtilViewTool.COLOR_RED, textSize) );
        }

    }

    private void drawPriceVolOfVolInfo(int startX, int textSize, Canvas canvas) {

        Paint paint = UtilViewTool.getBrush(1, UtilViewTool.COLOR_YELLOW, textSize);

        int volDivider = 0;
        if ( maxVol > 100000000 ) {
            volDivider = 100000000;
            canvas.drawText( String.format("%.2f亿", (float)maxVol / volDivider), startX, drawVolEndY+10, paint);
        } else {
            volDivider = 10000;
            canvas.drawText( String.format("%.2f万", (float)maxVol / volDivider), startX, drawVolEndY+10, paint);
        }
        canvas.drawText( String.format("%.2f", (float)minVol / volDivider), startX, drawVolStartY+10, paint);

        int eachVolLength = (drawVolStartY - drawVolEndY) / volCount;
        long eachVol = (maxVol - minVol) / volCount;
        for (int i=1; i<volCount; i++) {
            canvas.drawText( String.format("%.2f", (float)(maxVol - eachVol*i) / volDivider), startX, drawVolEndY+eachVolLength*i, paint);
        }

    }

    private void drawPriceVolRecord(Canvas canvas) {

        Paint paint = UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, 12);

        int recordCount = drawRecordCount - 1;
        int lengthOfPriceY = drawPriceStartY - drawPriceEndY;

        float priceFactor = (float)(lengthOfPriceY) / (maxHighest - minLowest);
        float volFactor = (float)(drawVolStartY - drawVolEndY) / (maxVol - minVol);

        float xStart = drawStartX;

        float closePriceStartY = drawPriceStartY - (chartRecordArray[recordCount][5] - minLowest) * priceFactor;

        float openPriceY = 0.00f;
        float closePriceY = 0.00f;
        float highestPriceY = 0.00f;
        float lowestPriceY = 0.00f;

        float tranCountY = 0.00f;
        float volY = 0.00f;

        float forwardWidth = 0;
        int color = 0;
        int counter = 0;
        for (int i=recordCount; i>0; i--) {

            closePriceY = drawPriceStartY - (chartRecordArray[i][5] - minLowest) * priceFactor;
            volY = drawVolStartY - (chartRecordArray[i][11] - minVol)* volFactor ;

            color = (int)chartRecordArray[i][6];

            forwardWidth = xStart + drawWidth;

            if (drawType == 0) {

                paint.setColor(UtilViewTool.COLOR_WHITE);
                canvas.drawLine(xStart, closePriceStartY, forwardWidth, closePriceY, paint);

                closePriceStartY = closePriceY;

            } else {

                paint.setColor(color);

                openPriceY = drawPriceStartY - (chartRecordArray[i][3] - minLowest) * priceFactor;
                highestPriceY = drawPriceStartY - (chartRecordArray[i][7] - minLowest) * priceFactor;
                lowestPriceY = drawPriceStartY - (chartRecordArray[i][8] - minLowest) * priceFactor;

                float priceCenterX = 0;
                float factorOfpriceCenterX = 0.00f;

                canvas.drawRect(xStart, openPriceY, xStart + drawWidth, closePriceY, paint);
                if ( drawWidth % 2 != 0) {
                    factorOfpriceCenterX = 0.00f;
                } else {
                    factorOfpriceCenterX = 0.5f;
                }
                priceCenterX = xStart + drawWidth / 2;
                canvas.drawLine(priceCenterX - factorOfpriceCenterX, highestPriceY,
                        priceCenterX - factorOfpriceCenterX, lowestPriceY, paint);

            }

            paint.setColor(color);
            canvas.drawRect(xStart, drawVolStartY, forwardWidth, volY, paint);

            recordSparseArray.put( counter, chartRecordArray[i] );

            xStart = forwardWidth;
            counter++;

        }

    }

    private void filterPriceVolInfo() {

        for (int i=drawRecordCount; i>0; i--) {

            int highest = (int) chartRecordArray[i][7];
            maxHighest = highest > maxHighest ? highest : maxHighest;

            int lowest = (int) chartRecordArray[i][8];
            if ( lowest != 0 ) {
                minLowest = lowest < minLowest ? lowest : minLowest;
            }

            int priceCount = (int) chartRecordArray[i][9];
            maxPriceCount = priceCount > maxPriceCount ? priceCount : maxPriceCount;
            minPriceCount = priceCount < minPriceCount ? priceCount : minPriceCount;

            int tranCount = (int) chartRecordArray[i][10];
            maxTranCount = tranCount > maxTranCount ? tranCount : maxTranCount;
            minTranCount = tranCount < minTranCount ? tranCount : minTranCount;

            long vol = chartRecordArray[i][11];
            maxVol = vol > maxVol ? vol : maxVol;
            minVol = vol < minVol ? vol : minVol;

            long money = chartRecordArray[i][12];
            maxMoney = money > maxMoney ? money : maxMoney;
            minMoney = money < minMoney ? money : minMoney;

            long timeGap = chartRecordArray[i][13];
            maxTimeGap = timeGap > maxTimeGap ? timeGap : maxTimeGap;
            minTimeGap = timeGap < minTimeGap ? timeGap : minTimeGap;

        }

        maxVol += maxVol/10;

    }

    public SparseArray<long[]> getRecordSparseArray() {
        return recordSparseArray;
    }

    public void init() {

        maxHighest = 0;
        minLowest = 10000;
        maxPriceCount = 0;
        minPriceCount = 100000;
        maxTranCount = 0;
        minTranCount = 100000000;
        maxVol = 0l;
        minVol = 100000000000l;
        maxMoney = 0l;
        minMoney = 1000000000000l;
        maxTimeGap = 0;
        minTimeGap = 450;

        sizeOfChartDailyRecordArray = 0;

    }

    public void initRecordSparseArray(int arrayCapacity) {
        recordSparseArray = new SparseArray<long[]>(arrayCapacity);
    }

    public void setChartRecordArray(long[][] recordArray) {
        chartRecordArray = recordArray;
    }

    public void setSizeOfChartRecordArray(int size) {
        sizeOfChartDailyRecordArray = size;
    }

    public void setXYAttrInfo(int[] infoArray) {

        priceCount = infoArray[0];
        volCount = infoArray[1];

        drawWidth = infoArray[2];

        drawStartX = infoArray[3];

        drawPriceStartY = infoArray[5];
        drawPriceEndY = infoArray[6];

        drawVolStartY = infoArray[7];
        drawVolEndY = infoArray[8];

        drawType = infoArray[9];
        drawRecordCount = infoArray[11];

    }

}
