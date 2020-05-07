package com.junt.xdialog.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.junt.xdialog.R;
import com.junt.xdialog.anim.XAnimatorScale;
import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.callbacks.ActivityLifeCycleCallback;
import com.junt.xdialog.callbacks.XDialogLifeCallBack;

import androidx.annotation.NonNull;

public abstract class CoreDialog extends Dialog {
    protected final String TAG = getClass().getSimpleName();
    private boolean isCancelOnTouchOutSide = true;
    protected XAnimator xAnimator;
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
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
            ((Activity) context).getApplication().registerActivityLifecycleCallbacks(new ActivityLifeCycleCallback() {
                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {
                    super.onActivityDestroyed(activity);
                    if (activity == getOwnerActivity()) {
                        CoreDialog.super.dismiss();
                        if (getXDialogCallBack()!=null){
                            getXDialogCallBack().onDismiss();
                        }
                    }
                }
            });
        }
        if (getXDialogCallBack() != null) {
            getXDialogCallBack().onCreateInstance(this);
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

        final View view = LayoutInflater.from(getContext()).inflate(getImplLayoutResId(), dialogContainer, false);
        view.setAlpha(0);
        dialogContainer.addView(view);

        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(dialogContainer, params);
        if (getXDialogCallBack() != null) {
            getXDialogCallBack().onCreate();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDialogContent();
        if (getXDialogCallBack() != null) {
            getXDialogCallBack().onContentReady(getDialogView());
        }
        runOnQueue(new Runnable() {
            @Override
            public void run() {
                System.out.println("dialog.onStart");
                onDialogViewAdd();
                if (xAnimator != null) {
                    xAnimator.bindAnimView(getDialogView(), getRealContext());
                    onAnimBind();
                    if (getXDialogCallBack() != null) {
                        getXDialogCallBack().onAnimatorBindDialogView(xAnimator);
                    }
                    xAnimator.initAnim();
                    if (getXDialogCallBack() != null) {
                        getXDialogCallBack().onAnimInitialized(xAnimator);
                    }
                }
            }
        });
    }

    /**
     * @return Dialog的布局id
     */
    protected abstract int getImplLayoutResId();

    /**
     * 初始化dialogView的内容
     */
    protected abstract void initDialogContent();


    /**
     * 获取Callback实例
     */
    protected XDialogLifeCallBack getXDialogCallBack(){
        return null;
    };

    /**
     * DialogView已添加至视图并且初始化完成
     */

    protected void onDialogViewAdd() {
    }

    /**
     * XAnimator已经绑定目标DialogView但还未调用initAnim()
     */
    protected void onAnimBind() {
    }

    /**
     * @return Dialog的根布局背景色
     */
    protected Drawable getBackgroundDrawable() {
        return new ColorDrawable(Color.parseColor("#80000000"));
    }

    protected View getDialogView() {
        return dialogContainer.getChildAt(0);
    }

    /**
     * @return DialogView在屏幕上的可见区域
     */
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

    /**
     * 该事件坐标是否处于DialogView内容区域之外
     *
     * @param event 触摸事件
     * @return true-处于DialogView内容区域之外 false-处于DialogView内容区域之内
     */
    private boolean isTouchOutSide(MotionEvent event) {
        View dialogView = dialogContainer.getChildAt(0);
        dialogView.getGlobalVisibleRect(rect);
        if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 传递过来的Context会被包装成ContextThemeWrapper，调用getContext()无法获取传递过来的真实context
     *
     * @return 真实的Context
     */
    protected Context getRealContext() {
        return ((ContextWrapper) getContext()).getBaseContext();
    }

    /**
     * 显示Dialog
     */
    @Override
    public void show() {
        super.show();
        dialogStack.addDialog(this);
        runOnQueue(new Runnable() {
            @Override
            public void run() {
                if (xAnimator != null) {
                    System.out.println("dialog.show");
                    getDialogView().setAlpha(1);
                    xAnimator.animShow();
                    if (getXDialogCallBack() != null) {
                        delayRun(new Runnable() {
                            @Override
                            public void run() {
                                getXDialogCallBack().onShow();
                            }
                        }, xAnimator.ANIM_DURATION);
                    }
                }
            }
        });
    }

    /**
     * dismiss Dialog
     * 实例化成功以后仅调用hide()方法来进行隐藏，避免多次调用onCreate()
     * 只有在Activity destroy时才会调用真实的dismiss方法
     */
    @Override
    public void dismiss() {
        System.out.println("dialog.dismiss");
        dialogStack.removeDialog(this);
        if (xAnimator != null) {
            xAnimator.animDismiss();
            delayRun(new Runnable() {
                @Override
                public void run() {
                    hide();
                    if (getXDialogCallBack()!=null){
                        getXDialogCallBack().onHide();
                    }
                }
            }, xAnimator.ANIM_DURATION);
        } else {
            hide();
            if (getXDialogCallBack()!=null){
                getXDialogCallBack().onHide();
            }
        }
    }

    /**
     * dismiss Dialog
     * 实例化成功以后仅调用hide()方法来进行隐藏，避免多次调用onCreate()
     * dialog隐藏后执行runnable
     *
     * @param runnable 需要执行的runnable
     */
    public void dismissAndRun(final Runnable runnable) {
        System.out.println("dialog.dismiss");
        dialogStack.removeDialog(this);
        if (xAnimator != null) {
            xAnimator.animDismiss();
            delayRun(new Runnable() {
                @Override
                public void run() {
                    hide();
                    runnable.run();
                }
            }, xAnimator.ANIM_DURATION);
        } else {
            hide();
            runnable.run();
        }
    }

    /**
     * 延迟执行
     *
     * @param runnable 需要执行的runnable
     * @param delay    延迟时间 ms
     */
    protected void delayRun(Runnable runnable, int delay) {
        dialogContainer.postDelayed(runnable, delay);
    }

    /**
     * 执行队列
     *
     * @param runnable cunnable
     */
    protected void runOnQueue(Runnable runnable) {
        getDialogView().post(runnable);
    }

    /**
     * 同步window的状态栏，与Activity的window一致
     */
    private void setStatusBarTrans() {
        Window activityWindow = ((Activity) getRealContext()).getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            window.getDecorView().setSystemUiVisibility(activityWindow.getDecorView().getSystemUiVisibility());

            window.addFlags(window.getAttributes().flags);
            window.setStatusBarColor(activityWindow.getStatusBarColor());
            window.setNavigationBarColor(activityWindow.getNavigationBarColor());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(window.getAttributes().flags, window.getAttributes().flags);
        }
    }
}
