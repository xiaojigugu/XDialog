package com.junt.xdialog.anim;

import android.graphics.Point;

import com.junt.xdialog.utils.ScreenUtils;

public class XAnimatorBottomEnter extends XAnimator {

    private Point point;

    @Override
    public void initAnim() {
        point = ScreenUtils.getScreenPoint(getView().getContext());
        int startTransY = point.y;
        getView().setTranslationY(startTransY);
    }

    @Override
    public void animShow() {
        int transY =-getView().getMeasuredHeight();
        getView().animate().translationYBy(transY).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void animShowing() {

    }

    @Override
    public void animDismiss() {
        getView().animate().translationYBy(getView().getMeasuredHeight()).setDuration(ANIM_DURATION).start();
    }
}
