package com.junt.xdialog.anim;

import android.view.View;

import com.junt.xdialog.core.XAttachDialog;

public class XAnimatorAttach extends XAnimator {

    private XAttachDialog.Direction direction;
    private View dialogView;

    public void setDialogPosition(XAttachDialog.Direction direction, View dialogView) {
        this.direction = direction;
        this.dialogView = dialogView;
    }

    @Override
    public void initAnim() {
        switch (direction) {
            case LEFT:
                getView().setPivotX(getView().getWidth());
                getView().setPivotY(getView().getHeight() / 2f);
                getView().setScaleX(0);
                break;
            case TOP:
                getView().setPivotY(getView().getHeight());
                getView().setPivotX(getView().getWidth() / 2f);
                getView().setScaleY(0);
                break;
            case RIGHT:
                getView().setPivotX(0);
                getView().setScaleX(0);
                break;
            case BOTTOM:
                getView().setPivotY(0);
                getView().setScaleY(0);
                break;
        }
    }

    @Override
    public void animShow() {
        switch (direction) {
            case LEFT:
            case RIGHT:
                getView().animate().scaleX(1).setDuration(ANIM_DURATION).start();
                break;
            case TOP:
            case BOTTOM:
                getView().animate().scaleY(1).setDuration(ANIM_DURATION).start();
                break;
        }
    }

    @Override
    public void animShowing() {

    }

    @Override
    public void animDismiss() {
        switch (direction) {
            case LEFT:
            case RIGHT:
                getView().animate().scaleX(0).setDuration(ANIM_DURATION).start();
                break;
            case TOP:
            case BOTTOM:
                getView().animate().scaleY(0).setDuration(ANIM_DURATION).start();
                break;
        }
    }
}
