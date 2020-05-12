package com.junt.xdialog.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogContainerLayout extends FrameLayout {

    private TouchCallBack touchCallBack;

    public DialogContainerLayout(@NonNull Context context) {
        super(context);
    }

    public DialogContainerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogContainerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return touchCallBack == null ? super.onInterceptTouchEvent(ev) : touchCallBack.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return touchCallBack == null ? super.onTouchEvent(event) : touchCallBack.onTouchEvent(event);
    }

    public void setTouchCallBack(TouchCallBack touchCallBack) {
        this.touchCallBack = touchCallBack;
    }

    public interface TouchCallBack {
        boolean onInterceptTouchEvent(MotionEvent ev);

        boolean onTouchEvent(MotionEvent ev);
    }
}
