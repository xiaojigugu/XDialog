package com.junt.demo.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.junt.demo.Main2Activity;
import com.junt.demo.R;
import com.junt.xdialog.core.XCoreDialog;

import androidx.annotation.NonNull;

/**
 * 用于测试Activity onDestroy时Dialog自动dismiss
 */
public class LifeCycleDialog extends XCoreDialog {

    public LifeCycleDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutResId() {
        return R.layout.dialog_life;
    }

    @Override
    protected void initDialogContent() {
        findViewById(R.id.tvFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getRealContext(), Main2Activity.class));
                ((Activity) getRealContext()).finish();
            }
        });

        findViewById(R.id.tvNotFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRealContext().startActivity(new Intent(getRealContext(), Main2Activity.class));
            }
        });
    }

}
