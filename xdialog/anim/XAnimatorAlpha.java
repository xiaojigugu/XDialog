package com.junt.xdialog.anim;

public class XAnimatorAlpha extends XAnimator {
    @Override
    public void initAnim() {
        getView().setAlpha(0);
    }

    @Override
    public void animShow() {
        getView().animate().alpha(1).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void animShowing() {

    }

    @Override
    public void animDismiss() {
        getView().animate().alpha(0).setDuration(ANIM_DURATION).start();
    }
}
