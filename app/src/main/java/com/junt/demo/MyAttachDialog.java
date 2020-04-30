package com.junt.demo;

import android.content.Context;
import android.widget.TextView;

import com.junt.xdialog.core.AttachDialog;

import androidx.annotation.NonNull;

public class MyAttachDialog extends AttachDialog {

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
        TextView textView=findViewById(R.id.textView);
        textView.setText("这是一个依附View的Dialog");
    }

    @Override
    protected int getExtra() {
        return 10;
    }
}
