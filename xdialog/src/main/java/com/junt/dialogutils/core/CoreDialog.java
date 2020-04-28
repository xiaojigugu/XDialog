package com.junt.dialogutils.core;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.junt.dialogutils.R;
import com.junt.dialogutils.anim.XAnimatorScale;
import com.junt.dialogutils.anim.XAnimator;
import com.junt.dialogutils.callbacks.ActivityLifeCycleCallback;

import androidx.annotation.NonNull;

public abstract class CoreDialog extends Dialog{
    private final String TAG = getClass().getSimpleName();
    private boolean isCancelOnTouchOutSide = true;
    private XAnimator xAnimator;
    private DialogStack dialogStack;
    private FrameLayout dialogContainer;
    private int touchSlop;

    public CoreDialog(@NonNull Context context) {
        this(context, new XAnimatorScale());
    }

    public CoreDialog(@NonNull Context context, XAnimator xAnimator) {
        this(context, R.style.XDialog);
        this.xAnimator = xAnimator;
        dialogStack = DialogStack.getInstance();
        if (context instanceof Activity){
            ((Activity) context).getApplication().registerActivityLifecycleCallbacks(new ActivityLifeCycleCallback(){
                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {
                    super.onActivityDestroyed(activity);
                    dismiss();
                }
            });
        }
    }

    public CoreDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("dialog.onCreate");
        setStatusBarTrans();
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        dialogContainer = (FrameLayout) getLayoutInflater().inflate(R.layout.dialog_container, (ViewGroup) getWindow().getDecorView(), false);
        dialogContainer.setBackground(getBackgroundDrawable());
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(dialogContainer, params);
        onDialogCreated();
    }

    private void onDialogCreated() {
        final View view = getLayoutInflater().inflate(getImplLayoutResId(), dialogContainer, false);
        view.setAlpha(0);
        dialogContainer.addView(view);
        runOnQueue(new Runnable() {
            @Override
            public void run() {
                onDialogViewAdd();
                initViewContent();
                if (xAnimator != null) {
                    xAnimator.bindAnimView(view);
                }
            }
        });
    }

    /**
     * DialogView已添加至视图
     */
    protected void onDialogViewAdd() {

    }

    protected abstract int getImplLayoutResId();

    protected abstract void initViewContent();

    private Drawable getBackgroundDrawable() {
        return new ColorDrawable(Color.parseColor("#80000000"));
    }

    protected View getDialogView() {
        return dialogContainer.getChildAt(0);
    }

    protected Rect getDialogViewVisibleRect() {
        Rect rect = new Rect();
        getDialogView().getGlobalVisibleRect(rect);
        return rect;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        isCancelOnTouchOutSide = cancel;
    }

    private float downX, downY;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float x = event.getX();
                float y = event.getY();

                double slop = Math.hypot(y - downY, x - downX);
                if (slop <= touchSlop && isTouchOutSide(event) && isCancelOnTouchOutSide) {
                    dismiss();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private Rect rect = new Rect();

    private boolean isTouchOutSide(MotionEvent event) {
        View dialogView = dialogContainer.getChildAt(0);
        dialogView.getGlobalVisibleRect(rect);
        if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void show() {
        super.show();
        dialogStack.addDialog(this);
        runOnQueue(new Runnable() {
            @Override
            public void run() {
                if (xAnimator != null) {
                    System.out.println("dialog->show" + Thread.currentThread().getName());
                    getDialogView().setAlpha(1);
                    xAnimator.animShow();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        dialogStack.removeDialog(this);
        if (xAnimator != null) {
            xAnimator.animDismiss();
            delayRun(new Runnable() {
                @Override
                public void run() {
                    hide();
                }
            }, xAnimator.ANIM_DURATION);
        } else {
            hide();
        }
    }
    
    private void delayRun(Runnable runnable, int delay) {
        dialogContainer.postDelayed(runnable, delay);
    }

    private void runOnQueue(Runnable runnable) {
        getDialogView().post(runnable);
    }

    /**
     * window透明状态栏
     */
    private void setStatusBarTrans() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.BLACK);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}