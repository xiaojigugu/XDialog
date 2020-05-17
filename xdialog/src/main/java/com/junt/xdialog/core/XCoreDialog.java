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
import android.util.Log;
import android.view.InputDevice;
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

import java.util.Stack;

import androidx.annotation.NonNull;

public abstract class XCoreDialog extends Dialog {
    protected final String TAG = getClass().getSimpleName();
    private boolean isCancelOnTouchOutSide = true;
    protected XAnimator xAnimator;
    private DialogStack dialogStack;
    protected DialogContainerLayout dialogContainer;
    protected int touchSlop;
    /**
     * DialogView是否已经完成初始化（包含动画得绑定及初始化）
     */
    protected boolean isReady = false;

    public XCoreDialog(@NonNull Context context) {
        this(context, new XAnimatorScale());
    }

    public XCoreDialog(@NonNull Context context, XAnimator xAnimator) {
        this(context, R.style.XDialog);
        this.xAnimator = xAnimator;
        dialogStack = DialogStack.getInstance();
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
            if (!getClass().getSimpleName().equals("XMessage")) {
                registerActivityLifeCallBack((Activity) context);
            }
        }
        if (getXDialogCallBack() != null) {
            getXDialogCallBack().onCreateInstance(this);
        }
    }

    /**
     * 注册生命周期回调
     * @param context Activity
     */
    private void registerActivityLifeCallBack(@NonNull Activity context) {
        context.getApplication().registerActivityLifecycleCallbacks(new ActivityLifeCycleCallback() {
            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                super.onActivityDestroyed(activity);
                if (activity == getOwnerActivity()) {
                    onDestroy();
                }
            }
        });
    }

    public XCoreDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(TAG + ".onCreate");
        initStatusBar();
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        dialogContainer = (DialogContainerLayout) getLayoutInflater().inflate(R.layout.dialog_container, (ViewGroup) getWindow().getDecorView(), false);
        dialogContainer.setTouchCallBack(new DialogContainerLayout.TouchCallBack() {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                return XCoreDialog.this.onContainerInterceptTouchEvent(ev);
            }

            @Override
            public boolean onTouchEvent(MotionEvent ev) {
                return XCoreDialog.this.onContainerTouchEvent(ev);
            }
        });

        final View view = LayoutInflater.from(getContext()).inflate(getImplLayoutResId(), dialogContainer, false);
        view.setAlpha(0);
        dialogContainer.addView(view);
        onDialogViewAdded();

        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(dialogContainer, params);
        if (getXDialogCallBack() != null) {
            getXDialogCallBack().onCreate();
        }

        initDialogContent();
        if (getXDialogCallBack() != null) {
            getXDialogCallBack().onContentReady(getDialogView());
        }
        runOnQueue(new Runnable() {
            @Override
            public void run() {
                onDialogViewCreated();
                drawBackground();

                if (xAnimator != null) {
                    onAnimBind();
                    onAnimInitialized();
                }
            }
        });
    }

    /**
     * 绘制背景阴影
     */
    private void drawBackground() {
        Rect backgroundBound = getBackgroundBound();
        int backgroundColor = getBackgroundColor();
        if (backgroundBound != null && backgroundColor != 0) {
            dialogContainer.setBackgroundColor(backgroundColor, backgroundBound);
        } else {
            dialogContainer.setBackgroundColor(backgroundColor);
        }
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
    public XDialogLifeCallBack getXDialogCallBack() {
        return null;
    }

    /**
     * DialogView已添加至根容器内
     */
    protected void onDialogViewAdded() {
    }

    /**
     * DialogView已添加至视图并且初始化完成（可以获取宽高）
     */
    protected void onDialogViewCreated() {
    }

    protected void onShow() {
    }

    protected void onDismiss() {
        hide();
        if (getXDialogCallBack() != null) {
            getXDialogCallBack().onDismiss();
        }
    }

    protected void onDestroy() {
        destroy();
        if (getXDialogCallBack() != null) {
            getXDialogCallBack().onDestroy();
        }
    }

    /**
     * XAnimator已经绑定目标DialogView但还未调用initAnim()
     */
    protected void onAnimBind() {
        xAnimator.bindAnimView(getDialogView(), getRealContext(), XCoreDialog.this);
        if (getXDialogCallBack() != null) {
            getXDialogCallBack().onAnimatorBindDialogView(xAnimator);
        }
    }

    /**
     * XAnimator已经绑定目标DialogView并且已经调用initAnim()
     */
    protected void onAnimInitialized() {
        isReady = true;
        xAnimator.initAnim();
        if (getXDialogCallBack() != null) {
            getXDialogCallBack().onAnimInitialized(xAnimator);
        }
    }

    /**
     * @return Dialog的根布局背景色
     */
    public int getBackgroundColor() {
        return getDefaultShadowColor();
    }

    /**
     * @return 默认的透明度阴影背景
     */
    protected int getDefaultShadowColor() {
        return Color.parseColor("#80000000");
    }

    public Rect getBackgroundBound() {
        return null;
    }

    /**
     * 获取DialogView
     */
    public View getDialogView() {
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

    /**
     * Dialog容器拦截事件
     */
    public boolean onContainerInterceptTouchEvent(@NonNull MotionEvent ev) {
        return false;
    }

    /**
     * Dialog容器消费事件
     */
    public boolean onContainerTouchEvent(@NonNull MotionEvent ev) {
        return false;
    }

    private Rect rect = new Rect();

    /**
     * 该事件坐标是否处于所有已经实例化的DialogView内容区域之外
     *
     * @param event 触摸事件
     * @return true-处于DialogView内容区域之外 false-处于DialogView内容区域之内
     */
    private boolean isTouchOutSide(MotionEvent event) {
        Stack<XCoreDialog> xCoreDialogStack = DialogStack.getInstance().getXCoreDialogStack();
        boolean isTouchOutside = true;
        for (XCoreDialog xCoreDialog : xCoreDialogStack) {
            if (xCoreDialog.getRealContext() != getRealContext()) {
                continue;
            }
            View dialogView = xCoreDialog.getDialogView();
            dialogView.getGlobalVisibleRect(rect);
            if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                isTouchOutside = false;
                break;
            }
        }
        if (isTouchOutside && getXDialogCallBack() != null) {
            getXDialogCallBack().onTouchOutside(event);
        }
        return isTouchOutside;
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
                getDialogView().setAlpha(1f);
                if (xAnimator != null) {
                    xAnimator.animShow();
                    delayRun(new Runnable() {
                        @Override
                        public void run() {
                            onShow();
                            if (getXDialogCallBack() != null) {
                                getXDialogCallBack().onShow();
                            }
                        }
                    }, xAnimator.ANIM_DURATION);
                } else {
                    onShow();
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
        if (xAnimator != null && !isReady) {
            Log.e(TAG, TAG + " is not ready");
            return;
        }
        System.out.println(TAG + ".dismiss");
        dialogStack.removeDialog(this);
        if (xAnimator != null) {
            xAnimator.animDismiss();
            delayRun(new Runnable() {
                @Override
                public void run() {
                    onDismiss();
                }
            }, xAnimator.ANIM_DURATION);
        } else {
            onDismiss();
        }
    }

    /**
     * 走正常的dismiss方法，下次使用会重新创建View
     */
    protected void destroy() {
        XCoreDialog.super.dismiss();
    }

    /**
     * dismiss Dialog
     * dialog隐藏后执行runnable
     *
     * @param runnable 需要执行的runnable
     */
    public void dismissAndRun(final Runnable runnable) {
        dismiss();
        if (xAnimator != null) {
            delayRun(runnable, xAnimator.ANIM_DURATION);
        } else {
            delayRun(runnable, 0);
        }
    }

    /**
     * 延时dismiss
     *
     * @param delay 延时时间
     */
    public void delayDismiss(int delay) {
        delayRun(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, delay);
    }

    /**
     * 延时dismiss Dialog
     * dialog隐藏后执行runnable
     *
     * @param delay    延时时间
     * @param runnable 需要执行的runnable
     */
    public void delayDismissAndRun(int delay, final Runnable runnable) {
        delayDismiss(delay);
        if (xAnimator != null) {
            delayRun(runnable, xAnimator.ANIM_DURATION + delay);
        } else {
            delayRun(runnable, delay);
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
     * 根布局（DecorView）Padding
     */
    protected void paddingRoot(int left, int top, int right, int bottom) {
        getWindow().getDecorView().setPadding(left, top, right, bottom);
    }

    /**
     * 同步window的状态栏，与Activity的window一致
     */
    private void initStatusBar() {
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
