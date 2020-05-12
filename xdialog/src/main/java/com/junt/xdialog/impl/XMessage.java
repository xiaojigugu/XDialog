package com.junt.xdialog.impl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.junt.xdialog.R;
import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.anim.XAnimatorAlpha;
import com.junt.xdialog.anim.XAnimatorScale;
import com.junt.xdialog.core.PositionDialog;
import com.junt.xdialog.core.XCoreDialog;
import com.junt.xdialog.utils.ScreenUtils;

import androidx.annotation.NonNull;

/**
 * 替代Toast
 */
public class XMessage extends PositionDialog {
    private int left, top;
    private int duration;
    private TextView tvMsg;
    private CharSequence msg;

    public XMessage(@NonNull Context context) {
        this(context, new XAnimatorAlpha());
    }

    public XMessage(@NonNull Context context, XAnimator xAnimator) {
        super(context, xAnimator);
    }

    @Override
    protected int getImplLayoutResId() {
        return R.layout.dialog_message;
    }

    @Override
    protected void initDialogContent() {
        tvMsg = findViewById(R.id.tvMsg);
        tvMsg.setText(msg);
    }

    @Override
    protected void onDialogViewCreated() {
        int x = left + getDialogView().getWidth() / 2;
        int y = top;
        if (left == Integer.MAX_VALUE) {
            x = getDialogView().getLeft() + getDialogView().getWidth() / 2;
        }
        setPosition(x, y);
        super.onDialogViewCreated();
    }

    @Override
    public Drawable getBackgroundDrawable() {
        return new ColorDrawable(Color.TRANSPARENT);
    }

    public void setMsg(CharSequence msg) {
        this.msg = msg;
        if (tvMsg != null) {
            tvMsg.setText(msg);
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPivot(int left, int top) {
        this.left = left;
        this.top = top;
    }

    @Override
    public void cancel() {
        dismiss();
    }

    @Override
    protected void onShow() {
        super.onShow();
        delayDismissAndRun(duration, new Runnable() {
            @Override
            public void run() {
                //一次性即使消息dismiss以后直接destroy
                destroy();
            }
        });
    }

    public static XMessage makeText(Context context, XAnimator xAnimator, CharSequence msg, int duration) {
        XMessage xMessage = new XMessage(context, xAnimator);
        xMessage.setMsg(msg);
        xMessage.setDuration(duration);
        Point screenPoint = ScreenUtils.getScreenPoint(context);
        xMessage.setPivot(Integer.MAX_VALUE, screenPoint.y - ScreenUtils.dp2px(context, 50));
        return xMessage;
    }

    public static XMessage makeText(Context context, CharSequence msg, int duration) {
        return makeText(context, new XAnimatorAlpha(), msg, duration);
    }

    private static final int DURATION_SHORT = 2000;
    private static final int DURATION_LONG = 4000;

    public static XMessage makeText(Context context, CharSequence msg, Duration duration) {
        return makeText(context, msg, duration == Duration.LENGTH_SHORT ? DURATION_SHORT : DURATION_LONG);
    }

    public static XMessage makeText(Context context, CharSequence msg, Duration duration, int left, int top) {
        XMessage xMessage = new XMessage(context);
        xMessage.setMsg(msg);
        xMessage.setDuration(duration == Duration.LENGTH_SHORT ? DURATION_SHORT : DURATION_LONG);
        xMessage.setPivot(left, top);
        return xMessage;
    }

    public enum Duration {
        LENGTH_SHORT,
        LENGTH_LONG
    }
}
