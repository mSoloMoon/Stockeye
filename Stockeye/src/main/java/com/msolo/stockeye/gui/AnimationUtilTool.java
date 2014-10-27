package com.msolo.stockeye.gui;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.msolo.stockeye.R;
import com.msolo.stockeye.StockeyeApp;

/**
 * Created by mSolo on 2014/8/13.
 */
public class AnimationUtilTool {

    private static final AnimationUtilTool INSTANCE = new AnimationUtilTool();

    private TranslateAnimation showAnimation = null;
    private Animation inAnimation = null;
    private Animation outAnimation = null;
    private Animation shakeAnimation = null;

    private AnimationUtilTool() {}

    public static AnimationUtilTool getInstance() {
        return INSTANCE;
    }

    public void setShowAnimation(View view) {

        if (showAnimation == null) {
            showAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                    0.0f);
            showAnimation.setDuration(1000 * 1);
        }

        view.startAnimation(showAnimation);

    }

    public void setInAnimation(View view) {

        if (inAnimation == null) {
            inAnimation = AnimationUtils.loadAnimation(StockeyeApp.appContext, android.R.anim.fade_in);
        }
        view.startAnimation(inAnimation);

    }

    public void setOutAnimation(View view) {

        if (outAnimation == null) {
            outAnimation = AnimationUtils.loadAnimation(StockeyeApp.appContext, android.R.anim.fade_out);
        }
        view.startAnimation(outAnimation);

    }

    public void setShakeAnimation(View view) {

        if (shakeAnimation == null) {
            shakeAnimation = AnimationUtils.loadAnimation(StockeyeApp.appContext, R.anim.shake_anim);
        }
        view.startAnimation(shakeAnimation);

    }


}
