package com.junt.xdialog.core;

import android.content.Context;
import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.anim.XAnimatorBottomEnter;

import androidx.annotation.NonNull;

public abstract class BottomDialog extends XCoreDialog {

    public BottomDialog(@NonNull Context context) {
        this(context,new XAnimatorBottomEnter());
    }

    public BottomDialog(@NonNull Context context, XAnimator xAnimator) {
        super(context, xAnimator);
    }
}
