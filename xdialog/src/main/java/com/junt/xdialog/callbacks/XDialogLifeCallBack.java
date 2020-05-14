package com.junt.xdialog.callbacks;

import android.view.MotionEvent;
import android.view.View;

import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.core.XCoreDialog;

public interface XDialogLifeCallBack {
    /**
     * CoreDialog实例创建完成
     */
    void onCreateInstance(XCoreDialog XCoreDialog);

    /**
     * Dialog创建完成，View已经添加进window
     */
    void onCreate();

    /**
     * dialogView的内容初始化结束
     */
    void onContentReady(View dialogView);

    /**
     * XAnimator绑定了DialogView但还未进行动画初始化
     */
    void onAnimatorBindDialogView(XAnimator xAnimator);

    /**
     * XAnimator动画初始化结束
     */
    void onAnimInitialized(XAnimator xAnimator);

    /**
     * Dialog显示动画执行完毕，Dialog完全显示
     */
    void onShow();

    /**
     * Dialog隐藏动画执行完毕，Dialog完全隐藏
     */
    void onDismiss();

    /**
     * Dialog完全销毁
     */
    void onDestroy();

    /**
     * 触摸了外部区域
     * @param event
     */
    void onTouchOutside(MotionEvent event);
}
