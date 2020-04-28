package com.junt.dialogutils.impl;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.junt.dialogutils.R;
import com.junt.dialogutils.anim.XAnimator;
import com.junt.dialogutils.core.CoreDialog;

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

    public void setText(String text) {
        this.text = text;
        if (textView != null) {
            textView.setText(text);
        }
    }

    @Override
    protected void initViewContent() {
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
