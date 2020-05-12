package com.junt.demo.dialog.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Point;

import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.utils.ScreenUtils;

public class SecondDialogAnimator extends XAnimator {
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
     * 底部导航栏高度（不存在则为0）
     */
    private int navBarHeight;
    private int top;

    @Override
    public void initAnim() {
        point = getRealScreenSize();
        navBarHeight = ScreenUtils.isNavBarVisible(getContext()) ? ScreenUtils.getNavBarHeight() : 0;
        getView().setTranslationX(getView().getWidth());

        int startTransY = point.y - navBarHeight - point.y / 3;
        getView().setTranslationY(startTransY);
        saveTranslation(startTransY);
    }

    @Override
    public void animShow() {
        //从界面外右侧平移进入
        getView().animate().translationX(0).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void animShowing() {

    }

    @Override
    public void animDismiss() {
        getView().animate().translationX(getView().getWidth()).setDuration(ANIM_DURATION).start();
    }

    /**
     * 视图内容滑至底部
     * 判断是否需要展开
     */
    public void onScrollToBottom() {
        if (!isExpand && !isPlaying && getView() != null) {
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
            //折叠状态则恢复原位置
            resumeLocation();
        } else {
            //展开状态，折叠dialog
            if (getView() == null) {
                return;
            }
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
