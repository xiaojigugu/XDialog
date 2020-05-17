package com.junt.xdialog.impl;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.junt.xdialog.R;
import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.callbacks.XItemChildClickListener;
import com.junt.xdialog.core.XCoreDialog;

import androidx.annotation.NonNull;

public class XConfirmDialog extends XCoreDialog {

    private String text;
    private TextView textView;
    private XItemChildClickListener xItemChildClickListener;

    public XConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public XConfirmDialog(@NonNull Context context, XAnimator xAnimator) {
        super(context, xAnimator);
    }

    @Override
    protected int getImplLayoutResId() {
        return R.layout.dialog_simple;
    }

    @Override
    protected void initDialogContent() {
        findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (xItemChildClickListener != null) {
                    xItemChildClickListener.onChildClick(v);
                }
            }
        });

        textView = findViewById(R.id.textView);
        textView.setText(text);
    }

    public XConfirmDialog setItemChildClickListener(XItemChildClickListener xItemChildClickListener) {
        this.xItemChildClickListener = xItemChildClickListener;
        return this;
    }

    public XConfirmDialog setText(String text) {
        this.text = text;
        if (textView != null) {
            textView.setText(text);
        }
        return this;
    }
}
