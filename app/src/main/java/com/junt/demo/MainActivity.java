package com.junt.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.junt.xdialog.core.AttachDialog;
import com.junt.xdialog.core.BottomDialog;
import com.junt.xdialog.core.PositionDialog;
import com.junt.xdialog.impl.SimpleConfirmDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tvBottom).setOnClickListener(this);
        findViewById(R.id.tvSimple).setOnClickListener(this);
        findViewById(R.id.tvAttach).setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            showPositionDialog((int) event.getRawX(), (int) event.getRawY());
        }
        return super.onTouchEvent(event);
    }

    /**
     * 简易Dialog带确认按钮
     */
    private void showSimpleConfirmDialog() {
        SimpleConfirmDialog simpleConfirmDialog = new SimpleConfirmDialog(this);
        simpleConfirmDialog.setText("简易的确认Dialog");
        simpleConfirmDialog.show();
    }

    /**
     * 底部弹框实现（也可自定义类继承BottomDialog）
     */
    private void showBottomDialog() {
        BottomDialog bottomDialog = new BottomDialog(this) {
            @Override
            protected void onDialogViewAdd() {

            }

            @Override
            protected int getImplLayoutResId() {
                return R.layout.dialog_bottom;
            }

            @Override
            protected void onAnimBind() {

            }
        };
        bottomDialog.show();
    }

    /**
     * 自由定位Dialog（也可自定义类继承PositionDialog）
     *
     * @param x dialog中心X坐标
     * @param y dialog中心y坐标
     */
    private void showPositionDialog(int x, int y) {
        PositionDialog positionDialog = new PositionDialog(this) {
            @Override
            protected int getImplLayoutResId() {
                return R.layout.dialog_simple;
            }

            @Override
            protected void onAnimBind() {

            }

            @Override
            protected void onDialogViewAdd() {
                super.onDialogViewAdd();
            }
        };
        positionDialog.setPosition(x, y);
        positionDialog.show();
    }

    /**
     * 依附于View的Dialog
     * AttachDialog.Direction - 指明Dialog位于View的上下左右方位
     * AttachDialog.Align - 表明对其方式，TOP、BOTTOM（上、下边界对齐只对左右位置生效），LEFT、RIGHT（左、右边界对齐只对上下位置生效）
     */
    private void showAttachDialog() {
        MyAttachDialog attachDialog = new MyAttachDialog(this);
        attachDialog.setText("依附View的Dialog");
        attachDialog.attach(findViewById(R.id.tvAttach), AttachDialog.Direction.LEFT, AttachDialog.Align.TOP).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBottom:
                showBottomDialog();
                break;
            case R.id.tvSimple:
                showSimpleConfirmDialog();
                break;
            case R.id.tvAttach:
                showAttachDialog();
                break;
        }

    }
}
