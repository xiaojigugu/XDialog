package com.junt.xdialog.widgests;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.junt.xdialog.utils.ScreenUtils;

import androidx.annotation.Nullable;

public class LoadingView extends View {

    private Paint paint;
    private float radius;
    private float radiusOffset;
    private float stokeWidth;

    private int backColor = Color.parseColor("#33000000");
    private int foreColor = Color.BLACK;
    private ArgbEvaluator argbEvaluator;
    int lineCount = 12; // 共12条线
    private final float stepAngle = 360f / lineCount;
    int realLineCount = 12;
    float centerX, centerY; // 中心x，y
    private int time = 1;//转动次数

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(backColor);
        argbEvaluator = new ArgbEvaluator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h) / 2f;
        radiusOffset = radius / 2f;

        centerX = getMeasuredWidth() / 2f;
        centerY = getMeasuredHeight() / 2f;

        stokeWidth = getMeasuredWidth() * 2f / ScreenUtils.dp2px(getContext(), 10);
        paint.setStrokeWidth(stokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < lineCount; i++) {
            double x = Math.cos(Math.toRadians(stepAngle * i));
            double y = Math.sin(Math.toRadians(stepAngle * i));
            canvas.drawLine(
                    centerX + (float) x * radiusOffset
                    , centerY + (float) y * radiusOffset
                    , centerX + (float) x * radius,
                    centerY + (float) y * radius
                    , paint);
        }

        for (int i = 0; i < realLineCount; i++) {
            paint.setColor((Integer) argbEvaluator.evaluate(1.0f / 12 * i, foreColor, backColor));
            double x = Math.cos(Math.toRadians((time + i) * stepAngle));
            double y = Math.sin(Math.toRadians((time + i) * stepAngle));
            canvas.drawLine(
                    centerX + (float) x * radiusOffset
                    , centerY + (float) y * radiusOffset
                    , centerX + (float) x * radius,
                    centerY + (float) y * radius
                    , paint);
        }
        postDelayed(rotateTask, 80);
    }

    private Runnable rotateTask = new Runnable() {
        @Override
        public void run() {
            time++;
            postInvalidate();
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(rotateTask);
    }
}
