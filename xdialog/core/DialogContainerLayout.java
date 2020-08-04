package com.junt.xdialog.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogContainerLayout extends FrameLayout {

    private TouchCallBack touchCallBack;
    private int color;
    private Rect rect;
    private Paint backPaint;

    public DialogContainerLayout(@NonNull Context context) {
        this(context, null);
    }

    public DialogContainerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialogContainerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (color != 0 && rect != null) {
            backPaint.setColor(color);
            canvas.drawRect(rect, backPaint);
        }
    }

    public void setBackgroundColor(int color, Rect rect) {
        this.color = color;
        this.rect = rect;
        setWillNotDraw(false);
        invalidate();
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
