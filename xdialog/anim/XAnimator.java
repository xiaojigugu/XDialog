package com.junt.xdialog.anim;


import android.content.Context;
import android.graphics.Point;
import android.view.View;

import com.junt.xdialog.core.XCoreDialog;
import com.junt.xdialog.utils.ScreenUtils;

import java.lang.ref.WeakReference;

public abstract class XAnimator {
    public final int ANIM_DURATION = 250;
    private WeakReference<View> viewWeakReference;
    private WeakReference<Context> contextWeakReference;
    private XCoreDialog xCoreDialog;
    private int currentTransY, currentTransX;

    public XAnimator() {
    }

    public void bindAnimView(View view, Context context, XCoreDialog xCoreDialog) {
        viewWeakReference = new WeakReference<>(view);
        contextWeakReference = new WeakReference<>(context);
        this.xCoreDialog = xCoreDialog;

    }

    protected View getView() {
        return viewWeakReference.get();
    }

    protected Context getContext() {
        return contextWeakReference.get();
    }

    protected XCoreDialog getDialog() {
        return xCoreDialog;
    }

    protected Point getRealScreenSize() {
        return ScreenUtils.getRealScreenPoint(getContext());
    }

    /**
     * 保存位移
     *
     * @param trans length=2时，trans[0] - yTrans,trans[1] - xTrans
     *              length=1时，trans[0] - yTrans
     */
    protected void saveTranslation(int... trans) {
        if (trans.length == 0) {
            return;
        }
        currentTransY = trans[0];
        if (trans.length == 2) {
            currentTransX = trans[1];
        }
    }

    public int getCurrentTransX() {
        return currentTransX;
    }

    public int getCurrentTransY() {
        return currentTransY;
    }

    /**
     * 对目标View进行动画初始化
     */
    public abstract void initAnim();

    /**
     * 显示动画
     */
    public abstract void animShow();

    /**
     * 目标DialogView已经显示时对其施加动画
     */
    public abstract void animShowing();

    /**
     * 隐藏动画
     */
    public abstract void animDismiss();
}
