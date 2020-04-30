package com.junt.xdialog.anim;


import android.view.View;

import java.lang.ref.WeakReference;

public abstract class XAnimator {
    public final int ANIM_DURATION=250;
    private WeakReference<View> viewWeakReference;

    public XAnimator() {
    }

    public void bindAnimView(View view){
        viewWeakReference = new WeakReference<>(view);
    }

    protected View getView(){
        return viewWeakReference.get();
    }

    public abstract void initAnim();

    public abstract void animShow();

    public abstract void animShowing();

    public abstract void animDismiss();
}
