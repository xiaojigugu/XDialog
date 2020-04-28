package com.junt.xdialog.anim;

import android.graphics.Rect;

import com.junt.xdialog.utils.ScreenUtils;

public class XAnimatorBottomEnter extends XAnimator {

    private Rect dialogViewVisibleRect;
    private Rect screenRect;

    @Override
    protected void initAnim() {
        dialogViewVisibleRect = new Rect();
        getView().getGlobalVisibleRect(dialogViewVisibleRect);
        screenRect = ScreenUtils.getScreenRect(getView().getContext());
        int startTransY = screenRect.bottom - dialogViewVisibleRect.top;
        System.out.printf("dialog->dialogRect:%s screenRect:%s startTransY:%d%n", dialogViewVisibleRect.toString(), screenRect.toString(), startTransY);
        getView().setTranslationY(startTransY);
    }

    @Override
    public void animShow() {
        int transY = screenRect.bottom - dialogViewVisibleRect.height();
        System.out.println("dialog->animShow.transY" + transY);
        getView().animate().translationY(transY).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void animShowing() {

    }

    @Override
    public void animDismiss() {
        getView().animate().translationY(dialogViewVisibleRect.height()).setDuration(ANIM_DURATION).start();
    }
}
