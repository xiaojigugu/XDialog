package com.junt.xdialog.anim;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.junt.xdialog.core.AttachDialog;

import static android.content.ContentValues.TAG;

public class XAnimatorAttach extends XAnimator {

    private AttachDialog.Direction direction;
    private View dialogView;


    public void setDialogPosition(AttachDialog.Direction direction, View dialogView) {
        this.direction = direction;
        this.dialogView = dialogView;
    }

    @Override
    public void initAnim() {
        Rect rect = new Rect();
        dialogView.getGlobalVisibleRect(rect);
        Log.i(TAG, "initAnim: rect-->"+rect.toString());
        switch (direction) {
            case LEFT:
                getView().setPivotX(rect.right);
                getView().setScaleX(0);
                break;
            case TOP:
                getView().setPivotY(rect.bottom);
                getView().setScaleY(0);
                break;
            case RIGHT:
                getView().setPivotX(rect.left);
                getView().setScaleX(0);
                break;
            case BOTTOM:
                getView().setPivotY(rect.top);
                getView().setScaleY(0);
                break;
        }
        dialogView.getGlobalVisibleRect(rect);
        Log.i(TAG, "initAnim: rect-->"+rect.toString());
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
