package com.junt.xdialog.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
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
 * 简易吐司不能完全替代Toast
 * 若需要全局使用则必须授予悬浮窗权限
 */
public class XMessage extends PositionDialog {

    private boolean canGlobalShow = false;
    private int left, top;
    private int duration;
    private TextView tvMsg;
    private CharSequence msg;
    private static final int DURATION_SHORT = 2000;
    private static final int DURATION_LONG = 4000;

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
        int y = top + getDialogView().getHeight() / 2;
        if (left == Integer.MAX_VALUE) {
            x = getDialogView().getLeft() + getDialogView().getWidth() / 2;
        }
        if (top == Integer.MAX_VALUE) {
            Point screenPoint = ScreenUtils.getScreenPoint(getRealContext());
            y = screenPoint.y - ScreenUtils.dp2px(getRealContext(), 50);
        }
        setPosition(x, y);
        super.onDialogViewCreated();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        ((Activity) getRealContext()).dispatchTouchEvent(event);
        return false;
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
        xMessage.setPivot(Integer.MAX_VALUE, Integer.MAX_VALUE);
        return xMessage;
    }

    public static XMessage makeText(Context context, CharSequence msg, int duration) {
        return makeText(context, new XAnimatorAlpha(), msg, duration);
    }

    public static XMessage makeText(Context context, CharSequence msg, Duration duration) {
        return makeText(context, msg, duration == Duration.LENGTH_SHORT ? DURATION_SHORT : DURATION_LONG);
    }

    public static XMessage makeText(Context context, XAnimator xAnimator, CharSequence msg, Duration duration) {
        return makeText(context, xAnimator, msg, duration == Duration.LENGTH_SHORT ? DURATION_SHORT : DURATION_LONG);
    }

    public static XMessage makeText(Context context, CharSequence msg, Duration duration, int left, int top) {
        XMessage xMessage = makeText(context, msg, duration == Duration.LENGTH_SHORT ? DURATION_SHORT : DURATION_LONG);
        xMessage.setPivot(left, top);
        return xMessage;
    }

    public static XMessage makeText(Context context, XAnimator xAnimator, CharSequence msg, Duration duration, int left, int top) {
        XMessage xMessage = makeText(context, xAnimator, msg, duration == Duration.LENGTH_SHORT ? DURATION_SHORT : DURATION_LONG);
        xMessage.setPivot(left, top);
        return xMessage;
    }

    public void show(boolean canGlobalShow) {
        if (canGlobalShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getRealContext())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                        show();
                        return;
                    }
                    getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
                    show();
                    return;
                }
                Log.e(TAG, "XMessage需要悬浮窗权限才能全局显示");
                return;
            }
            getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            show();
        }
    }

    public enum Duration {
        LENGTH_SHORT,
        LENGTH_LONG
    }
}
