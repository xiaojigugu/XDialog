package com.junt.xdialog.anim;

import android.graphics.Point;
import android.graphics.Rect;

import com.junt.xdialog.utils.ScreenUtils;

public class XAnimatorBottomEnter extends XAnimator {

    @Override
    public void initAnim() {
        Rect rect = new Rect();
        getView().getGlobalVisibleRect(rect);
        //屏幕真实尺寸
        Point point = ScreenUtils.getRealScreenPoint(getContext());
        //屏幕高-dialogView的Top-导航栏高度（如果导航栏可见）
        int startTransY = point.y - rect.top - (ScreenUtils.isNavBarVisible(getContext()) ? ScreenUtils.getNavBarHeight() : 0);
        getView().setTranslationY(startTransY);
    }

    @Override
    public void animShow() {
        int transY = -getView().getMeasuredHeight();
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
