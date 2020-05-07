package com.junt.xdialog.anim;

import android.animation.ValueAnimator;

public class XAnimatorScale extends XAnimator {

    private ValueAnimator valueAnimatorShow;

    @Override
    public void initAnim() {
        getView().setScaleX(0);
        getView().setScaleY(0);
    }

    @Override
    public void animShow() {
        getView().animate().scaleX(1f).scaleY(1f).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void animShowing() {

    }

    @Override
    public void animDismiss() {
        getView().animate().scaleX(0f).scaleY(0f).setDuration(ANIM_DURATION).start();
    }
}
