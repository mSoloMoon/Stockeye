package com.msolo.stockeye.gui;

import android.graphics.Paint;

/**
 * Created by mSolo on 2014/8/28.
 */
public class UtilViewTool {

    public static final int COLOR_WHITE = 0xFFFFFFFF;
    public static final int COLOR_BLACK = 0xFF000000;
    public static final int COLOR_GRAY = 0xFFF2F1F1;
    public static final int COLOR_RED = 0xFFBE0D15;
    public static final int COLOR_GREEN = 0xFF00C600;
    public static final int COLOR_YELLOW = 0xFFFFFF00;
    public static final int COLOR_ORANGE = 0xFFEC8C67;
    public static final int COLOR_BLUE = 0xFF249FF1;

    private UtilViewTool() {}

    public static Paint getBrush(int strokeWidth, int strokeColor, int textSize) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(strokeWidth);
        p.setColor(strokeColor);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setTextSize(textSize);
        return p;
    }

}
