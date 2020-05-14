package com.junt.xdialog.core;

import android.content.Context;
import android.view.MotionEvent;

import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.anim.XAnimatorBottomEnter;

import androidx.annotation.NonNull;

public abstract class BottomDialog extends XCoreDialog {

    private float downX, downY;
    private boolean canTouch = true;
    private int currentTranslationY;

    public BottomDialog(@NonNull Context context) {
        this(context, new XAnimatorBottomEnter());
    }

    public BottomDialog(@NonNull Context context, XAnimator xAnimator) {
        super(context, xAnimator);
    }

    @Override
    protected void onDialogViewCreated() {
        super.onDialogViewCreated();
        currentTranslationY = xAnimator.getCurrentTransY();

        System.out.println("BottomDialog:" + currentTranslationY);
    }

    /**
     * 直接重写最开始的onTouchEvent
     * 一旦子View消费了触摸事件则不会触发这里的方法回调
     */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentTranslationY += (event.getY() - downY);
                System.out.println("BottomDialog:" + currentTranslationY);
                getDialogView().setTranslationY(currentTranslationY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }
}
