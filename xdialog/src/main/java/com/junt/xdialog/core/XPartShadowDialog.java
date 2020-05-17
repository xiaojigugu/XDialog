package com.junt.xdialog.core;

import android.content.Context;
import android.graphics.Rect;

import androidx.annotation.NonNull;

public abstract class XPartShadowDialog extends XAttachDialog {

    public XPartShadowDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getBackgroundColor() {
        return getDefaultShadowColor();
    }

    @Override
    public Rect getBackgroundBound() {
        return new Rect(0, (int) (getDialogView().getTop() + getDialogView().getTranslationY()), dialogContainer.getWidth(), dialogContainer.getHeight());
    }
}
