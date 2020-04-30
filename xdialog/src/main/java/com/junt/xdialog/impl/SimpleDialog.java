package com.junt.xdialog.impl;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.junt.xdialog.R;
import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.core.CoreDialog;

import androidx.annotation.NonNull;

public class SimpleDialog extends CoreDialog {

    private String text;
    private TextView textView;

    public SimpleDialog(@NonNull Context context) {
        super(context);
    }

    public SimpleDialog(@NonNull Context context, XAnimator xAnimator) {
        super(context, xAnimator);
    }

    @Override
    protected int getImplLayoutResId() {
        return R.layout.dialog_simple;
    }

    @Override
    protected void onAnimBind() {

    }

    public void setText(String text) {
        this.text = text;
        if (textView != null) {
            textView.setText(text);
        }
    }

    @Override
    protected void onDialogViewAdd() {
        findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        textView = findViewById(R.id.textView);
        textView.setText(text);
    }
}
