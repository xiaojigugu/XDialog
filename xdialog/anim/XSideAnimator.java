package com.junt.xdialog.anim;

import android.graphics.Rect;
import android.view.ViewPropertyAnimator;

import com.junt.xdialog.utils.ScreenUtils;

public class XSideAnimator extends XAnimator {
    private Rect rect = new Rect(0, 0, 0, 0);
    private Orientation orientation;

    public XSideAnimator(Orientation orientation) {
        this.orientation = orientation;
    }

    @Override
    public void initAnim() {
        Rect rect = new Rect();
        getView().getGlobalVisibleRect(rect);
        switch (orientation) {
            case LEFT:
                getView().setTranslationX(-rect.right);
                break;
            case RIGHT:
                getView().setTranslationX(ScreenUtils.getScreenPoint(getContext()).x - rect.left);
                break;
            case TOP:
                getView().setTranslationY(-rect.height());
                break;
            case BOTTOM:
                //屏幕高-dialogView的Top-导航栏高度（如果导航栏可见）
                int startTransY = ScreenUtils.getRealScreenPoint(getContext()).y - rect.top - (ScreenUtils.isNavBarVisible(getContext()) ? ScreenUtils.getNavBarHeight() : 0);
                getView().setTranslationY(startTransY);
                break;
        }
    }

    @Override
    public void animShow() {
        ViewPropertyAnimator animator = getView().animate().setDuration(ANIM_DURATION);
        switch (orientation) {
            case LEFT:
                animator.translationXBy(rect.left + getView().getWidth());
                break;
            case RIGHT:
                animator.translationXBy(-rect.right - getView().getWidth());
                break;
            case TOP:
                animator.translationYBy(rect.top + getView().getHeight());
                break;
            case BOTTOM:
                int i = -rect.bottom - getView().getHeight();
                animator.translationYBy(i);
                break;
        }
        animator.start();
    }

    @Override
    public void animShowing() {
    }

    @Override
    public void animDismiss() {
        ViewPropertyAnimator animator = getView().animate().setDuration(ANIM_DURATION);
        switch (orientation) {
            case LEFT:
                animator.translationXBy(-rect.left - getView().getWidth());
                break;
            case RIGHT:
                animator.translationXBy(rect.right + getView().getWidth());
                break;
            case TOP:
                animator.translationYBy(-rect.top - getView().getHeight());
                break;
            case BOTTOM:
                animator.translationYBy(+rect.bottom + getView().getHeight());
                break;
        }
        animator.start();
    }

    public void setMargin(Rect rect) {
        this.rect = rect;
    }

    /**
     * 指定Dialog相对于屏幕出现的方向
     */
    public enum Orientation {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }
}
