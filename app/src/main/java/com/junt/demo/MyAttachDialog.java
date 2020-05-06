package com.junt.demo;

import android.content.Context;
import android.widget.TextView;

import com.junt.xdialog.core.AttachDialog;

import androidx.annotation.NonNull;

public class MyAttachDialog extends AttachDialog {
    private String text;
    private TextView textView;

    public MyAttachDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutResId() {
        return R.layout.dialog_attach;
    }

    @Override
    protected void onDialogViewAdd() {
        super.onDialogViewAdd();
        textView = findViewById(R.id.textView);
        textView.setText(text);
    }

    public void setText(String text) {
        this.text = text;
        if (textView != null)
            textView.setText(text);
    }

    /**
     * dialog与依附的View之间的间隔
     *
     * @return dp
     */
    @Override
    protected int getExtra() {
        return 10;
    }
}
