package com.junt.xdialog.anim;


import android.content.Context;
import android.view.View;

import java.lang.ref.WeakReference;

public abstract class XAnimator {
    public final int ANIM_DURATION = 250;
    private WeakReference<View> viewWeakReference;
    private WeakReference<Context> contextWeakReference;

    public XAnimator() {
    }

    public void bindAnimView(View view, Context context) {
        viewWeakReference = new WeakReference<>(view);
        contextWeakReference = new WeakReference<>(context);
    }

    protected View getView() {
        return viewWeakReference.get();
    }

    protected Context getContext() {
        return contextWeakReference.get();
    }

    public abstract void initAnim();

    public abstract void animShow();

    public abstract void animShowing();

    public abstract void animDismiss();
}
