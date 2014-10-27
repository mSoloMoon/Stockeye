package com.msolo.stockeye.gui.fragment.transaction.customization;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.msolo.stockeye.R;
import com.msolo.stockeye.StockeyeApp;
import com.msolo.stockeye.gui.UtilViewTool;
import com.msolo.stockeye.service.common.CommonServiceFacade;

/**
 * Created by mSolo on 2014/8/26.
 */
public class StockPriceVolChartView extends View implements StockPriceVolChartViewContextImp {

    private static final String TAG = "K线图";

    private static final int CHART_MENU_INTERVAL_WIDTH = 20;

    private static final int PRICE_COUNT = 5;
    private static final int VOL_COUNT = 3;
    private static final int MAX_ENLARGE_COUNT = 5;

    private static final int CHART_PRICE_END_Y = 60;
    private static final int CHART_INTERVAL_HEIGHT = 50;

    private static final int CHART_STARTY_FACTOR = 20;

    private static final int BITMAP_MENU_START_Y = 10;

    private boolean isBackgroundBlank;
    private boolean hasMenuTouch;

    private int menuDrawType;
    private int menuPeriodType;

    private int enlargeCount;
    private int enlargeWidth;
    private int enlargeWidthStep;
    private int chartBackgroundHeight;

    private int chartPriceVolStartX;
    private int chartPriceVolEndX;

    private int chartPriceStartY;
    private int chartVolStartY;
    private int chartVolEndY;

    private int bitmapEnlargeStartX;
    private int bitmapEnlargeEndX;
    private int bitmapEnlargeHeight;

    private int bitmapMinusStartX;
    private int bitmapMinusEndX;
    private int bitmapMinusHeight;

    private int[] menuPeriodStartX = new int[5];
    private int[] menuPeriodEndX = new int[5];
    private int menuPeriodStartY;
    private int menuPeriodEndY;

    private int touchX;

    private Canvas gCanvas;
    private Bitmap backgroundBitmap;
    private Bitmap prototypeBackgroundBitmap;
    private Bitmap chartBitmap;

    private SparseArray<long[]> recordSparseArray;

    public StockPriceVolChartView(Context context) {
        super(context);
    }

    public StockPriceVolChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        enlargeCount = 0;
        menuDrawType = 0;

        chartPriceVolStartX = StockeyeApp.isLargeScreen ? 120 : 80;
        enlargeWidth = StockeyeApp.isLargeScreen ? 4 : 2;
        enlargeWidthStep = StockeyeApp.isLargeScreen ? 4 : 2;
        touchX = -1;

        isBackgroundBlank = true;
        hasMenuTouch = false;

        menuPeriodType = 0;

    }

    public StockPriceVolChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Canvas getCanvas() {
        return gCanvas;
    }

    @Override
    public int[] getXYAttrInfoArray() {

        return new int[]{

                PRICE_COUNT,
                VOL_COUNT,
                enlargeWidth,

                chartPriceVolStartX,
                chartPriceVolEndX,

                chartPriceStartY - CHART_STARTY_FACTOR,
                CHART_PRICE_END_Y,

                chartVolStartY,
                chartVolEndY - CHART_STARTY_FACTOR / 2,

                menuDrawType,       //  drawType : line, rect, box, ...
                menuPeriodType,     //  periodType : daily, weekly, monthly, quarterly, yearly, ...
                (chartPriceVolEndX - chartPriceVolStartX) / enlargeWidth

        };

    }

    @Override
    public void setRecordSparseArray(SparseArray<long[]> recordSparseArray) {
        this.recordSparseArray = recordSparseArray;
    }

    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (isBackgroundBlank) {

            isBackgroundBlank = !isBackgroundBlank;

            backgroundBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            gCanvas = new Canvas(backgroundBitmap);

            drawChartMenu(gCanvas);
            drawChartLayout(gCanvas);

            prototypeBackgroundBitmap = Bitmap.createBitmap(backgroundBitmap);

            ((YoutubeLayout) this.getParent()).getStrategy().applyStrategy(this);

            chartBitmap = Bitmap.createBitmap(backgroundBitmap);

        }

        if (hasMenuTouch) {

            hasMenuTouch = false;

            backgroundBitmap = Bitmap.createBitmap(prototypeBackgroundBitmap);

            gCanvas = new Canvas(backgroundBitmap);
            ((YoutubeLayout) this.getParent()).getStrategy().applyStrategy(this);

            chartBitmap = Bitmap.createBitmap(backgroundBitmap);

        }

        canvas.drawBitmap(chartBitmap, 0, 0, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, 12));
        drawEachRecordInfoBackground(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);

        touchX = (int)event.getX();
        int touchY = (int)event.getY();

        if ( event.getAction() == MotionEvent.ACTION_UP ) {
            touchX = -1;
            return true;
        }

        int outsideX = recordSparseArray.size() * enlargeWidth + chartPriceVolStartX;

        boolean isTouchEnlargeMenu = checkIsTouchEnlargeMenu(touchX, touchY);
        boolean isTouchMinusMenu = checkIsTouchMinusMenu(touchX, touchY);

        boolean isTouchDailyMenu = checkIsTouchDailyMenu(touchX, touchY);
        boolean isTouchWeeklyMenu = checkIsTouchWeeklyMenu(touchX, touchY);
        boolean isTouchMonthlyMenu = checkIsTouchMonthlyMenu(touchX, touchY);
        boolean isTouchQuarterlyMenu = checkIsTouchQuarterlyMenu(touchX, touchY);
        boolean isTouchYearlyMenu = checkIsTouchYearlyMenu(touchX, touchY);

        boolean isTouchOutsideDraw = touchX < chartPriceVolStartX || (touchY > CHART_PRICE_END_Y && touchX > outsideX);

        boolean isTouchPeriodMenu = isTouchDailyMenu || isTouchWeeklyMenu || isTouchMonthlyMenu || isTouchQuarterlyMenu || isTouchYearlyMenu;

        if ( isTouchEnlargeMenu || isTouchMinusMenu || isTouchPeriodMenu ) {
            touchX = -1;
            hasMenuTouch = !hasMenuTouch;

            if ( isTouchPeriodMenu ) {

                if (!isTouchDailyMenu && !isTouchWeeklyMenu) {
                    enlargeWidth = StockeyeApp.isLargeScreen ? 8 : 4;
                    enlargeWidthStep = StockeyeApp.isLargeScreen ? 8 : 4;
                } else {
                    enlargeWidth = StockeyeApp.isLargeScreen ? 4 : 2;
                    enlargeWidthStep = StockeyeApp.isLargeScreen ? 4 : 2;
                }
                enlargeCount = 0;

            }

        }

        if ( isTouchOutsideDraw || (touchY < CHART_PRICE_END_Y && !isTouchEnlargeMenu && !isTouchMinusMenu && !isTouchPeriodMenu) ) {
            touchX = -1;
            return true;
        }

        menuDrawType = enlargeCount == 0 ? 0 : 1;

        invalidate();           //重新绘制区域

        return true;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec) );

        chartPriceVolEndX = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec) - 10;
        chartBackgroundHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        chartPriceStartY = (chartBackgroundHeight * 2) / 3 - CHART_INTERVAL_HEIGHT;
        chartVolEndY = (chartBackgroundHeight * 2) / 3;
        chartVolStartY = chartBackgroundHeight - CHART_INTERVAL_HEIGHT - 40;

    }

    public void setBackgroundBlankFlag(boolean flag) {

        isBackgroundBlank = flag;

    }

    private void drawChartLayout(Canvas canvas) {

        Paint paint = UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, 12);

        // X - date, Y - price
        canvas.drawLine(chartPriceVolStartX, chartPriceStartY, chartPriceVolEndX, chartPriceStartY, paint);
        canvas.drawLine(chartPriceVolStartX, CHART_PRICE_END_Y, chartPriceVolStartX, chartPriceStartY, paint);

        // X - date, Y - volume
        canvas.drawLine(chartPriceVolStartX, chartVolStartY, chartPriceVolEndX, chartVolStartY, paint);
        canvas.drawLine(chartPriceVolStartX, chartVolEndY, chartPriceVolStartX, chartVolStartY, paint);

        paint = UtilViewTool.getBrush(1, UtilViewTool.COLOR_GRAY, 12);

        float eachPriceLength = (float)(chartPriceStartY - CHART_STARTY_FACTOR - CHART_PRICE_END_Y) / PRICE_COUNT;
        for (int i=1; i<PRICE_COUNT; i++) {
            canvas.drawLine(chartPriceVolStartX, CHART_PRICE_END_Y+eachPriceLength*i, chartPriceVolEndX, CHART_PRICE_END_Y+eachPriceLength*i, paint);
        }

        float eachVolLength = (float)(chartVolStartY - CHART_STARTY_FACTOR/2 - chartVolEndY) / VOL_COUNT;
        for (int i=1; i<VOL_COUNT; i++) {
            canvas.drawLine(chartPriceVolStartX, chartVolEndY+eachVolLength*i, chartPriceVolEndX, chartVolEndY+eachVolLength*i, paint);
        }

    }

    private void drawChartMenu(Canvas canvas) {

        Bitmap minus;
        Bitmap enlarge;
        if (StockeyeApp.isLargeScreen) {
            minus = BitmapFactory.decodeResource(getResources(), R.drawable.zoomout_32);
            enlarge = BitmapFactory.decodeResource(getResources(), R.drawable.zoomin_32);
        } else {
            minus = BitmapFactory.decodeResource(getResources(), R.drawable.zoomout_24);
            enlarge = BitmapFactory.decodeResource(getResources(), R.drawable.zoomin_24);
        }
        bitmapMinusEndX = chartPriceVolEndX - 20;
        bitmapMinusStartX = bitmapMinusEndX - minus.getWidth();
        bitmapMinusHeight = minus.getHeight();

        bitmapEnlargeEndX = bitmapMinusStartX - CHART_MENU_INTERVAL_WIDTH;
        bitmapEnlargeStartX = bitmapEnlargeEndX - enlarge.getWidth();
        bitmapEnlargeHeight = enlarge.getHeight();

        canvas.drawBitmap( enlarge, bitmapEnlargeStartX, BITMAP_MENU_START_Y, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, 12) );
        canvas.drawBitmap( minus, bitmapMinusStartX, BITMAP_MENU_START_Y, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, 12) );

        int textSize = 28;

        menuPeriodEndX[4] = bitmapEnlargeStartX - CHART_MENU_INTERVAL_WIDTH;
        menuPeriodStartX[4] = menuPeriodEndX[4] - 64;

        menuPeriodEndX[3] = menuPeriodStartX[4] - CHART_MENU_INTERVAL_WIDTH;
        menuPeriodStartX[3] = menuPeriodEndX[3] - 64;

        menuPeriodEndX[2] = menuPeriodStartX[3] - CHART_MENU_INTERVAL_WIDTH;
        menuPeriodStartX[2] = menuPeriodEndX[2] - 64;

        menuPeriodEndX[1] = menuPeriodStartX[2] - CHART_MENU_INTERVAL_WIDTH;
        menuPeriodStartX[1] = menuPeriodEndX[1] - 64;

        menuPeriodEndX[0] = menuPeriodStartX[1] - CHART_MENU_INTERVAL_WIDTH;
        menuPeriodStartX[0] = menuPeriodEndX[0] - 64;

        menuPeriodStartY = BITMAP_MENU_START_Y + textSize/4;
        menuPeriodEndY = BITMAP_MENU_START_Y + textSize*3/2;
        canvas.drawRect(menuPeriodStartX[4], menuPeriodStartY, menuPeriodEndX[4], menuPeriodEndY, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, 12) );
        canvas.drawRect(menuPeriodStartX[3], menuPeriodStartY, menuPeriodEndX[3], menuPeriodEndY, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, 12) );
        canvas.drawRect(menuPeriodStartX[2], menuPeriodStartY, menuPeriodEndX[2], menuPeriodEndY, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, 12) );
        canvas.drawRect(menuPeriodStartX[1], menuPeriodStartY, menuPeriodEndX[1], menuPeriodEndY, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, 12) );
        canvas.drawRect(menuPeriodStartX[0], menuPeriodStartY, menuPeriodEndX[0], menuPeriodEndY, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, 12) );

        int textEndY = BITMAP_MENU_START_Y+textSize*5/4;
        canvas.drawText( "年", menuPeriodStartX[4]+textSize/2, textEndY, UtilViewTool.getBrush(1, UtilViewTool.COLOR_BLACK, textSize) );
        canvas.drawText( "季", menuPeriodStartX[3]+textSize/2, textEndY, UtilViewTool.getBrush(1, UtilViewTool.COLOR_BLACK, textSize) );
        canvas.drawText( "月", menuPeriodStartX[2]+textSize/2, textEndY, UtilViewTool.getBrush(1, UtilViewTool.COLOR_BLACK, textSize) );
        canvas.drawText( "周", menuPeriodStartX[1]+textSize/2, textEndY, UtilViewTool.getBrush(1, UtilViewTool.COLOR_BLACK, textSize) );
        canvas.drawText( "日", menuPeriodStartX[0]+textSize/2, textEndY, UtilViewTool.getBrush(1, UtilViewTool.COLOR_BLACK, textSize) );

    }

    private void drawEachRecordInfoBackground(Canvas canvas) {

        if (touchX < 0) {
            return ;
        }

        canvas.drawLine(touchX, CHART_PRICE_END_Y, touchX, chartVolStartY, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, 12));

        int infoLeftX = chartPriceVolStartX + 40;
        int infoRightX = chartPriceVolEndX - 240;
        int drawCenterX = chartPriceVolStartX + (chartPriceVolEndX - chartPriceVolStartX) / 2;
        int drawY = CHART_PRICE_END_Y;
        int drawX = touchX < drawCenterX ? infoRightX : infoLeftX;
        int textSize = StockeyeApp.isLargeScreen ? 20 : 18;

        // 0:date, 1:weekday, 2:last, 3:open, 4:openFlag, 5:close, 6:closeFlag, 7:highest,
        // 8:lowest, 9:priceCount, 10:tranCount, 11:vol, 12:money, 13:timeGap
        long[] record = recordSparseArray.get( (touchX - chartPriceVolStartX) / enlargeWidth );
        if ( record[0] > 20000000 ) {

            canvas.drawText( String.format( "日期: %s", CommonServiceFacade.getInstance().getDateString( (int)record[0] )),
                    drawX+ 20, drawY + 20, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );
            canvas.drawText( String.format( "本周: %d", (int)record[1] ),
                    drawX+ 20, drawY + 50, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );

            canvas.drawText( String.format( "成交量: %,.4f万", (float)record[11]/10000 ),
                    drawX+ 20, drawY + 290, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );
            canvas.drawText( String.format( "成交额: %,.4f亿", (float)record[12]/100000000 ),
                    drawX+ 20, drawY + 320, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );

            canvas.drawText( String.format( "时间差: %s", getStringTimeGap((int)record[13]) ),
                    drawX+ 20, drawY + 350, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );

        } else {

            if ( record[0] > 200000 ) {

                canvas.drawText( String.format( "月份: %d-%02d", record[0]/100, record[0] - record[0]/100*100),
                        drawX+ 20, drawY + 20, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );

            } else if ( record[0] > 20000 ) {

                canvas.drawText( String.format( "季度: %d-%d", record[0]/10, record[0] - record[0]/10*10),
                        drawX+ 20, drawY + 20, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );

            } else if ( record[0] > 2000 ) {

                canvas.drawText( String.format( "年度: %d", record[0]),
                        drawX+ 20, drawY + 20, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );

            }

            if ( menuPeriodType == 1 ) {
                canvas.drawText( String.format( "第 %d 周", (int)record[1] ),
                        drawX+ 20, drawY + 50, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );
            } else {
                canvas.drawText( String.format( "天数: %d", (int)record[1] ),
                        drawX+ 20, drawY + 50, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );
            }

            canvas.drawText( String.format( "成交量: %,.4f亿", (float)record[11]/100000000 ),
                    drawX+ 20, drawY + 290, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );
            canvas.drawText( String.format( "成交额: %,.4f百亿", ((float)record[12]/100000000)/100 ),
                    drawX+ 20, drawY + 320, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );

            canvas.drawText( String.format( "34笔: %,.4f亿", (float)record[13]/100000000 ),
                    drawX+ 20, drawY + 350, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );

        }

        canvas.drawText( String.format( "昨收: %.2f", (float)record[2]/100 ),
                drawX+ 20, drawY + 80, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );
        canvas.drawText( String.format( "开盘: %.2f", (float)record[3]/100 ),
                drawX+ 20, drawY + 110, UtilViewTool.getBrush(1, (int)record[4], textSize) );
        canvas.drawText( String.format( "收盘: %.2f", (float)record[5]/100 ),
                drawX+ 20, drawY + 140, UtilViewTool.getBrush(1, (int)record[6], textSize) );
        canvas.drawText( String.format( "最高: %.2f", (float)record[7]/100 ),
                drawX+ 20, drawY + 170, UtilViewTool.getBrush(1, UtilViewTool.COLOR_RED, textSize) );
        canvas.drawText( String.format( "最低: %.2f", (float)record[8]/100 ),
                drawX+ 20, drawY + 200, UtilViewTool.getBrush(1, UtilViewTool.COLOR_GREEN, textSize) );
        canvas.drawText( String.format( "价格数: %,d", (int)record[9] ),
                drawX+ 20, drawY + 230, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );
        canvas.drawText( String.format( "交易数: %,d", (int)record[10] ),
                drawX+ 20, drawY + 260, UtilViewTool.getBrush(1, UtilViewTool.COLOR_WHITE, textSize) );

    }

    private boolean checkIsTouchEnlargeMenu(int xOfTouch, int yOfTouch) {

        boolean isTouchEnlargeMenu = (xOfTouch > bitmapEnlargeStartX && xOfTouch < bitmapEnlargeEndX &&
                yOfTouch > BITMAP_MENU_START_Y && yOfTouch < bitmapEnlargeHeight);

        if ( isTouchEnlargeMenu && enlargeCount < MAX_ENLARGE_COUNT) {
            enlargeWidth += enlargeWidthStep;
            enlargeCount++;
        }

        return isTouchEnlargeMenu;
    }

    private boolean checkIsTouchMinusMenu(int xOfTouch, int yOfTouch) {

        boolean isTouchMinusMenu = (xOfTouch > bitmapMinusStartX && xOfTouch < bitmapMinusEndX &&
                yOfTouch > BITMAP_MENU_START_Y && yOfTouch < bitmapMinusHeight);

        if ( isTouchMinusMenu && enlargeCount > 0) {
            enlargeWidth -= enlargeWidthStep;
            enlargeCount--;
        }

        return isTouchMinusMenu;

    }

    private boolean checkIsTouchDailyMenu(int xOfTouch, int yOfTouch) {

        boolean isTouchDailyMenu = (xOfTouch > menuPeriodStartX[0] && xOfTouch < menuPeriodEndX[0] &&
                yOfTouch > menuPeriodStartY && yOfTouch < menuPeriodEndY);

        if ( isTouchDailyMenu ) {
            menuPeriodType = 0;
        }

        return isTouchDailyMenu;

    }

    private boolean checkIsTouchWeeklyMenu(int xOfTouch, int yOfTouch) {

        boolean isTouchWeeklyMenu = (xOfTouch > menuPeriodStartX[1] && xOfTouch < menuPeriodEndX[1] &&
                yOfTouch > menuPeriodStartY && yOfTouch < menuPeriodEndY);

        if ( isTouchWeeklyMenu ) {
            menuPeriodType = 1;
        }

        return isTouchWeeklyMenu;

    }

    private boolean checkIsTouchMonthlyMenu(int xOfTouch, int yOfTouch) {

        boolean isTouchMonthlyMenu = (xOfTouch > menuPeriodStartX[2] && xOfTouch < menuPeriodEndX[2] &&
                yOfTouch > menuPeriodStartY && yOfTouch < menuPeriodEndY);

        if ( isTouchMonthlyMenu ) {
            menuPeriodType = 2;
        }

        return isTouchMonthlyMenu;

    }

    private boolean checkIsTouchQuarterlyMenu(int xOfTouch, int yOfTouch) {

        boolean isTouchQuarterlyMenu = (xOfTouch > menuPeriodStartX[3] && xOfTouch < menuPeriodEndX[3] &&
                yOfTouch > menuPeriodStartY && yOfTouch < menuPeriodEndY);

        if ( isTouchQuarterlyMenu ) {
            menuPeriodType = 3;
        }

        return isTouchQuarterlyMenu;

    }

    private boolean checkIsTouchYearlyMenu(int xOfTouch, int yOfTouch) {

        boolean isTouchYearlyMenu = (xOfTouch > menuPeriodStartX[4] && xOfTouch < menuPeriodEndX[4] &&
                yOfTouch > menuPeriodStartY && yOfTouch < menuPeriodEndY);

        if ( isTouchYearlyMenu ) {
            menuPeriodType = 4;
        }

        return isTouchYearlyMenu;

    }

    private String getStringTimeGap(int timeGap) {

        String timeGapStr = String.format("%d", timeGap );

        return timeGapStr.length() > 2
               ? String.format("%s:%s", timeGap / 100, timeGapStr.substring(1, 3))
               : String.format("0:%d", timeGap);

    }

}
