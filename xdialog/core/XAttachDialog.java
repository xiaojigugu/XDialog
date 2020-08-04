package com.junt.xdialog.core;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.anim.XAnimatorAttach;
import com.junt.xdialog.callbacks.XDialogLifeCallBack;
import com.junt.xdialog.callbacks.XDialogLifeCallbackImpl;
import com.junt.xdialog.utils.ScreenUtils;

import androidx.annotation.NonNull;

public abstract class XAttachDialog extends XPositionDialog {
    private View attachView;
    private Point screenPoint;
    private Direction direction;
    private Align align;

    public XAttachDialog(@NonNull Context context) {
        super(context, new XAnimatorAttach());
    }

    public void attach(View attachView) {
        attach(attachView, null, null);
    }

    public XAttachDialog attach(View attachView, Direction direction, Align align) {
        this.attachView = attachView;
        this.direction = direction;
        this.align = align;
        screenPoint = ScreenUtils.getScreenPoint(getContext());
        return this;
    }

    @Override
    protected void onDialogViewCreated() {
        if (attachView == null) {
            Log.e(TAG, getClass().getSimpleName() + ".错误:请先调用attach()");
        } else {
            Rect dialogViewVisibleRect = getDialogViewVisibleRect();
            Rect attachViewRect = new Rect();
            attachView.getGlobalVisibleRect(attachViewRect);
            if (direction == null || align == null) {
                handleDefaultPosition(attachViewRect, dialogViewVisibleRect);
            } else {
                handleCustomPosition(attachViewRect, dialogViewVisibleRect);
            }
            //调用父类PositionDialog方法进行摆放
            XAttachDialog.super.onDialogViewCreated();
        }
    }

    @Override
    public XDialogLifeCallBack getXDialogCallBack() {
        return new XDialogLifeCallbackImpl() {
            @Override
            public void onAnimatorBindDialogView(XAnimator xAnimator) {
                //摆放完成后初始化Animator
                ((XAnimatorAttach) xAnimator).setDialogPosition(direction, getDialogView());
            }
        };
    }

    /**
     * 自定义摆放
     */
    private void handleCustomPosition(Rect attachViewRect, Rect dialogViewVisibleRect) {
        switch (direction) {
            case LEFT://左侧
                if (align == Align.TOP) {
                    //上对齐
                    setPosition(attachViewRect.left - dialogViewVisibleRect.width() / 2 - getExtraDP(), attachViewRect.top + dialogViewVisibleRect.height() / 2);
                } else if (align == Align.BOTTOM) {
                    //下对齐
                    setPosition(attachViewRect.left - dialogViewVisibleRect.width() / 2 - getExtraDP(), attachViewRect.bottom - dialogViewVisibleRect.height() / 2);
                } else {
                    //居中对齐
                    setPosition(attachViewRect.left - dialogViewVisibleRect.width() / 2 - getExtraDP(), attachViewRect.centerY());
                }
                break;
            case TOP://上侧
                if (align == Align.LEFT) {
                    //左对齐
                    setPosition(attachViewRect.left + dialogViewVisibleRect.width() / 2, attachViewRect.top - dialogViewVisibleRect.height() / 2 - getExtraDP());
                } else if (align == Align.RIGHT) {
                    //右对齐
                    setPosition(attachViewRect.right - dialogViewVisibleRect.width() / 2, attachViewRect.top - dialogViewVisibleRect.height() / 2 - getExtraDP());
                } else {
                    //居中
                    setPosition(attachViewRect.centerX(), attachViewRect.top - dialogViewVisibleRect.height() / 2 - getExtraDP());
                }
                break;
            case RIGHT://右侧
                if (align == Align.TOP) {
                    //上对齐
                    setPosition(attachViewRect.right + dialogViewVisibleRect.width() / 2 + getExtraDP(), attachViewRect.top + dialogViewVisibleRect.height() / 2);
                } else if (align == Align.BOTTOM) {
                    //其他默认下对齐
                    setPosition(attachViewRect.right + dialogViewVisibleRect.width() / 2 + getExtraDP(), attachViewRect.bottom - dialogViewVisibleRect.height() / 2);
                } else {
                    //居中对齐
                    setPosition(attachViewRect.right + dialogViewVisibleRect.width() / 2 + getExtraDP(), attachViewRect.centerY());
                }
                break;
            case BOTTOM://下侧
                if (align == Align.LEFT) {
                    //左对齐
                    setPosition(attachViewRect.left + dialogViewVisibleRect.width() / 2, attachViewRect.bottom + dialogViewVisibleRect.height() / 2 + getExtraDP());
                } else if (align == Align.RIGHT) {
                    //其他默认右对齐
                    setPosition(attachViewRect.right - dialogViewVisibleRect.width() / 2, attachViewRect.bottom + dialogViewVisibleRect.height() / 2 + getExtraDP());
                } else {
                    //居中对齐
                    setPosition(attachViewRect.centerX(), attachViewRect.bottom + dialogViewVisibleRect.height() / 2 + getExtraDP());
                }
                break;
        }
    }

    /**
     * 默认摆放
     */
    private void handleDefaultPosition(Rect attachViewRect, Rect dialogViewVisibleRect) {
        if (screenPoint.y - attachViewRect.bottom >= dialogViewVisibleRect.height()) {
            //优先摆放在下方
            setPosition(attachViewRect.centerX(), attachViewRect.bottom + dialogViewVisibleRect.height() / 2 + getExtraDP());
        } else if (screenPoint.x - attachViewRect.right >= dialogViewVisibleRect.width()) {
            //摆放在右方
            setPosition(attachViewRect.right + dialogViewVisibleRect.width() / 2 + getExtraDP(), attachViewRect.top + dialogViewVisibleRect.height() / 2);
        } else if (attachViewRect.top >= dialogViewVisibleRect.height()) {
            //摆放在上方
            setPosition(attachViewRect.centerX(), attachViewRect.top - dialogViewVisibleRect.height() / 2 - getExtraDP());
        } else if (attachViewRect.left >= dialogViewVisibleRect.width()) {
            //摆放在左方
            setPosition(attachViewRect.left - dialogViewVisibleRect.width() / 2 - getExtraDP(), attachViewRect.top + dialogViewVisibleRect.height() / 2);
        } else {
            //没找到合适位置，摆放在屏幕中间
            setPosition(screenPoint.x / 2, screenPoint.y / 2);
        }
    }

    @Override
    public int getBackgroundColor() {
        return Color.TRANSPARENT;
    }

    public View getAttachView() {
        return attachView;
    }

    private int getExtraDP() {
        return ScreenUtils.dp2px(getContext(), getExtra());
    }

    protected int getExtra() {
        return 0;
    }

    /**
     * 相对于attachView的摆放位置
     */
    public enum Direction {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }

    /**
     * 对其方式
     */
    public enum Align {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        CENTER
    }
}
