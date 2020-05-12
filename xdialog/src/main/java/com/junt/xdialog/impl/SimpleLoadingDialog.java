package com.junt.xdialog.impl;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.junt.xdialog.R;
import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.core.XCoreDialog;

import androidx.annotation.NonNull;

public class SimpleLoadingDialog extends XCoreDialog {
    private String text;
    private TextView textView;

    public SimpleLoadingDialog(@NonNull Context context) {
        super(context);
    }

    public SimpleLoadingDialog(@NonNull Context context, XAnimator xAnimator) {
        super(context, xAnimator);
    }

    @Override
    protected int getImplLayoutResId() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void initDialogContent() {
        textView = findViewById(R.id.tvText);
        textView.setText(TextUtils.isEmpty(text) ? "正在加载..." : text);
    }

    public void setText(String text) {
        this.text = text;
        if (textView != null) {
            textView.setText(TextUtils.isDigitsOnly(text) ? "正在加载..." : text);
        }
    }


}
