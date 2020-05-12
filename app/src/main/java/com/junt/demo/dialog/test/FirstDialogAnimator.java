package com.junt.demo.dialog.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Point;
import android.graphics.Rect;

import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.utils.ScreenUtils;

public class FirstDialogAnimator extends XAnimator {
    /**
     * 是否反向播放动画（dialog显示中执行的动画）
     */
    private boolean isShowingAnimReverse = false;
    /**
     * 当前是否处于展开状态
     */
    private boolean isExpand = false;
    /**
     * 当前是否处于动画播放状态
     */
    private boolean isPlaying = false;
    /**
     * 屏幕尺寸
     */
    private Point point;
    /**
     * dialogView目前可视区域
     */
    private Rect dialogViewVisibleRect;
    /**
     * 底部导航栏高度（不存在则为0）
     */
    private int navBarHeight;

    @Override
    public void initAnim() {
        dialogViewVisibleRect = new Rect();
        getView().getGlobalVisibleRect(dialogViewVisibleRect);
        //屏幕真实尺寸
        point = getRealScreenSize();
        //屏幕高-dialogView的Top-导航栏高度（如果导航栏可见）

        navBarHeight = ScreenUtils.isNavBarVisible(getContext()) ? ScreenUtils.getNavBarHeight() : 0;
        int startTransY = point.y - dialogViewVisibleRect.top - navBarHeight;
        getView().setTranslationY(startTransY);
        saveTranslation(startTransY);
    }

    @Override
    public void animShow() {
        //初始只占用屏幕的1/3的高度
        int trans = point.y - navBarHeight - point.y / 3;
        saveTranslation(trans);
        getView().animate().translationY(trans).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void animShowing() {
        System.out.println(getClass().getSimpleName() + ".animShowing");
        if (!isShowingAnimReverse) {
            //向左移动
            getView().animate().translationX(-getView().getWidth()).setDuration(ANIM_DURATION).start();
            isShowingAnimReverse = true;
        } else {
            //向右移动
            getView().animate().translationX(0).setDuration(ANIM_DURATION).start();
            isShowingAnimReverse = false;
        }
    }

    @Override
    public void animDismiss() {
        getView().getGlobalVisibleRect(dialogViewVisibleRect);
        saveTranslation(getCurrentTransY() + dialogViewVisibleRect.height());
        getView().animate().translationYBy(dialogViewVisibleRect.height()).setDuration(ANIM_DURATION).start();
    }

    /**
     * 视图内容滑至底部
     * 判断是否需要展开
     */
    public void onScrollToBottom() {
        if (!isExpand && !isPlaying) {
            getView().animate()
                    .translationY(0)
                    .setDuration(ANIM_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            isPlaying = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            isPlaying = false;
                            isExpand = true;
                            saveTranslation(0);
                        }
                    }).start();
        }
    }

    /**
     * 视图内容滑动至底部
     * 判断是否 折叠/dismiss
     */
    public void onScrollToTop() {
        if (!isExpand) {
            //初始状态直接dismiss()
            getDialog().dismiss();
        } else {
            //展开状态，折叠dialog
            getView().animate()
                    .translationY(point.y - navBarHeight - point.y / 3f)
                    .setDuration(ANIM_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            isPlaying = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            isPlaying = false;
                            isExpand = false;
                            saveTranslation(point.y - navBarHeight - point.y / 3);
                        }
                    })
                    .start();
        }
    }

    public void resumeLocation() {
        getView().animate().translationY(getCurrentTransY()).setDuration(ANIM_DURATION).start();
    }

    /**
     * 是否需要恢复原位置
     */
    public boolean shouldResumeLocation() {
        if (Math.abs(getView().getTranslationY() - getCurrentTransY()) > point.y / 6f) {
            return false;
        }
        return true;
    }
}
