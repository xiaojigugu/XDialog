package com.junt.xdialog.core;

import android.content.Context;
import android.view.MotionEvent;

import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.anim.XAnimatorBottomEnter;

import androidx.annotation.NonNull;

public abstract class BottomDialog extends XCoreDialog {

    private float downX, downY;
    /**
     * Dialog完全显示后的TranslationY
     */
    private float showTransY;


    public BottomDialog(@NonNull Context context) {
        this(context, new XAnimatorBottomEnter());
    }

    public BottomDialog(@NonNull Context context, XAnimator xAnimator) {
        super(context, xAnimator);
    }

    @Override
    protected void onShow() {
        super.onShow();
        showTransY=getDialogView().getTranslationY();
    }
    /**
     * 直接重写最开始的onTouchEvent
     * 一旦子View消费了触摸事件则不会触发这里的方法回调
     */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("down:" + event.toString());
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("move:" + event.toString());
                float moveY = event.getY();
                if (moveY > downY) {
                    float moveDis = moveY - downY;
                    float dis = showTransY + moveDis;
                    getDialogView().setTranslationY(dis);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int dis = getDialogView().getHeight() - getDialogViewVisibleRect().height();
                if ((dis > getDialogView().getHeight() / 3)) {
                    dismiss();
                } else {
                    getDialogView().animate().translationYBy(-dis).setDuration(280).start();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
