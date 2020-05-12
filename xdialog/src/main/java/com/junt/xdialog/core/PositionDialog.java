package com.junt.xdialog.core;

import android.content.Context;
import android.graphics.Rect;

import com.junt.xdialog.anim.XAnimator;

import androidx.annotation.NonNull;

public abstract class PositionDialog extends XCoreDialog {

    private int x, y;

    public PositionDialog(@NonNull Context context) {
        super(context);
    }

    public PositionDialog(@NonNull Context context, XAnimator xAnimator) {
        super(context, xAnimator);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected void onDialogViewCreated() {
        Rect dialogViewVisibleRect = getDialogViewVisibleRect();
        System.out.println(getClass().getSimpleName() + ",onDialogViewCreated:" + dialogViewVisibleRect.toString());
        int dialogCenterX = dialogViewVisibleRect.left + dialogViewVisibleRect.width() / 2;
        int dialogCenterY = dialogViewVisibleRect.top + dialogViewVisibleRect.height() / 2;
        getDialogView().setTranslationX(x - dialogCenterX);
        getDialogView().setTranslationY(y - dialogCenterY);
    }
}
