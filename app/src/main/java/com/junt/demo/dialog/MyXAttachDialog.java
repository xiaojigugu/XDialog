package com.junt.demo.dialog;

import android.content.Context;
import android.widget.TextView;

import com.junt.demo.R;
import com.junt.xdialog.core.XAttachDialog;

import androidx.annotation.NonNull;

public class MyXAttachDialog extends XAttachDialog {
    private String text;
    private TextView textView;

    public MyXAttachDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initDialogContent() {
        textView = findViewById(R.id.textView);
        textView.setText(text);
    }

    @Override
    protected int getImplLayoutResId() {
        return R.layout.dialog_attach;
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
