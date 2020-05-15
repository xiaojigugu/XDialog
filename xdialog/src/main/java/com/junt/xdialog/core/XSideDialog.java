package com.junt.xdialog.core;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.anim.XSideAnimator;

import androidx.annotation.NonNull;

/**
 * 侧边Dialog
 */
public abstract class XSideDialog extends XCoreDialog {

    private XSideAnimator.Orientation orientation;
    private float showTranslationX, showTranslationY;
    private VelocityTracker velocityTracker;

    public XSideDialog(@NonNull Context context, XSideAnimator.Orientation orientation) {
        this(context, new XSideAnimator(orientation));
        this.orientation = orientation;
    }

    private XSideDialog(@NonNull Context context, XAnimator xAnimator) {
        super(context, xAnimator);
    }

    @Override
    protected void onShow() {
        super.onShow();
        showTranslationX = getDialogView().getTranslationX();
        showTranslationY = getDialogView().getTranslationY();
    }

    public void setMargin(Rect rect) {
        ((XSideAnimator) xAnimator).setMargin(rect);
    }

    private float downX, downY;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                switch (orientation) {
                    case LEFT:
                        if (moveX < downX) {
                            getDialogView().setTranslationX(showTranslationX + moveX - downX);
                        }
                        break;
                    case TOP:
                        if (moveY < downY) {
                            getDialogView().setTranslationY(showTranslationY + moveY - downY);
                        }
                        break;
                    case RIGHT:
                        if (moveX > downX) {
                            getDialogView().setTranslationX(showTranslationX + moveX - downX);
                        }
                        break;
                    case BOTTOM:
                        if (moveY > downY) {
                            getDialogView().setTranslationY(showTranslationY + moveY - downY);
                        }
                        break;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
//                System.out.println("滑动速率：Vx="+velocityTracker.getXVelocity(0)+",Vy="+velocityTracker.getYVelocity(0));
                moveX = event.getX();
                moveY = event.getY();
                switch (orientation) {
                    case LEFT:
                        if (moveX < downX && velocityTracker.getXVelocity(0) < -1000) {
                            dismiss();
                        } else {
                            getDialogView().animate().translationX(showTranslationX).setDuration(80).start();
                        }
                        break;
                    case RIGHT:
                        if (moveX > downX && velocityTracker.getXVelocity(0) > 1000) {
                            dismiss();
                        } else {
                            getDialogView().animate().translationX(showTranslationX).setDuration(80).start();
                        }
                        break;
                    case TOP:
                        if (moveY < downY && velocityTracker.getYVelocity(0) < -1000) {
                            dismiss();
                        } else {
                            getDialogView().animate().translationY(showTranslationY).setDuration(80).start();
                        }
                        break;
                    case BOTTOM:
                        if (moveY > downY && velocityTracker.getYVelocity(0) > 1000) {
                            dismiss();
                        } else {
                            getDialogView().animate().translationY(showTranslationY).setDuration(80).start();
                        }
                        break;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
