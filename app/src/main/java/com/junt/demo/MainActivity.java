package com.junt.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.junt.demo.dialog.LifeCycleDialog;
import com.junt.demo.dialog.MyAttachDialog;
import com.junt.xdialog.anim.XSideAnimator;
import com.junt.xdialog.core.AttachDialog;
import com.junt.xdialog.core.PositionDialog;
import com.junt.xdialog.core.XSideDialog;
import com.junt.xdialog.impl.XConfirmDialog;
import com.junt.xdialog.impl.XLoadingDialog;
import com.junt.xdialog.impl.XMessage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tvSideLeft).setOnClickListener(this);
        findViewById(R.id.tvSideTop).setOnClickListener(this);
        findViewById(R.id.tvSideRight).setOnClickListener(this);
        findViewById(R.id.tvSideBottom).setOnClickListener(this);

        findViewById(R.id.tvSimple).setOnClickListener(this);
        findViewById(R.id.tvAttach).setOnClickListener(this);
        findViewById(R.id.tvLoading).setOnClickListener(this);
        findViewById(R.id.tvMsg).setOnClickListener(this);
        findViewById(R.id.tvMsgRandomLoc).setOnClickListener(this);
        findViewById(R.id.tvJump).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSideLeft:
                showLeftSideDialog();
                break;
            case R.id.tvSideTop:
                showTopSideDialog();
                break;
            case R.id.tvSideRight:
                showRightSideDialog();
                break;
            case R.id.tvSideBottom:
                showBottomSideDialog();
                break;
            case R.id.tvSimple:
                showSimpleConfirmDialog();
                break;
            case R.id.tvAttach:
                showAttachDialog();
                break;
            case R.id.tvLoading:
                showLoadingDialog();
                break;
            case R.id.tvMsg:
                showMsgDialog("标准吐司");
                findViewById(R.id.tvMsg).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showMsgDialog("第二个标准吐司");
                    }
                }, 1000);
                break;
            case R.id.tvMsgRandomLoc:
                showRandomMsgDialog(10, 200);
                showRandomMsgDialog(50, 400);
                showRandomMsgDialog(100, 600);
                break;
            case R.id.tvJump:
                showLifeCycleDialog();
                break;
        }
    }

    /**
     * 左侧侧边菜单
     */
    private void showLeftSideDialog() {
        XSideDialog xSideDialog = new XSideDialog(this, XSideAnimator.Orientation.LEFT) {
            @Override
            protected int getImplLayoutResId() {
                return R.layout.dialog_side_left;
            }

            @Override
            protected void initDialogContent() {

            }
        };
        Rect rect = new Rect();
        //左侧Dialog仅left有效
        rect.left = 0;
        xSideDialog.setMargin(rect);
        xSideDialog.show();
    }

    /**
     * 顶部侧边菜单
     */
    private void showTopSideDialog() {
        XSideDialog xSideDialog = new XSideDialog(this, XSideAnimator.Orientation.TOP) {
            @Override
            protected int getImplLayoutResId() {
                return R.layout.dialog_side_top;
            }

            @Override
            protected void initDialogContent() {

            }
        };
        Rect rect = new Rect();
        //左侧Dialog仅left有效
        rect.top = 0;
        xSideDialog.setMargin(rect);
        xSideDialog.show();
    }

    /**
     * 右侧边菜单
     */
    private void showRightSideDialog() {
        XSideDialog xSideDialog = new XSideDialog(this, XSideAnimator.Orientation.RIGHT) {
            @Override
            protected int getImplLayoutResId() {
                return R.layout.dialog_side_right;
            }

            @Override
            protected void initDialogContent() {

            }
        };
        Rect rect = new Rect();
        //左侧Dialog仅left有效
        rect.right = 0;
        xSideDialog.setMargin(rect);
        xSideDialog.show();
    }

    /**
     * 底部侧边菜单
     */
    private void showBottomSideDialog() {
        XSideDialog xSideDialog = new XSideDialog(this, XSideAnimator.Orientation.BOTTOM) {
            @Override
            protected int getImplLayoutResId() {
                return R.layout.dialog_side_bottom;
            }

            @Override
            protected void initDialogContent() {

            }
        };
        Rect rect = new Rect();
        //左侧Dialog仅left有效
        rect.bottom = 0;
        xSideDialog.setMargin(rect);
        xSideDialog.show();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            showPositionDialog((int) event.getRawX(), (int) event.getRawY());
        }
        return super.onTouchEvent(event);
    }

    /**
     * 任意位置吐司Dialog
     */
    private void showRandomMsgDialog(int left, int top) {
        XMessage.makeText(this, "任意位置吐司", XMessage.Duration.LENGTH_SHORT, left, top).show();
    }

    private XMessage xMessage;

    /**
     * 标准吐司Dialog
     */
    private void showMsgDialog(String msg) {
        if (xMessage != null) {
            xMessage.cancel();
        }
        xMessage = XMessage.makeText(this, msg, XMessage.Duration.LENGTH_SHORT);
        xMessage.show();
    }


    /**
     * 显示LoadingDialog，
     */
    private void showLoadingDialog() {
        XLoadingDialog loadingDialog = new XLoadingDialog(this);
        loadingDialog.show();
        loadingDialog.delayDismissAndRun(3000, new Runnable() {
            @Override
            public void run() {
                XMessage.makeText(MainActivity.this, "加载成功!", XMessage.Duration.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 简易Dialog带确认按钮
     */
    private void showSimpleConfirmDialog() {
        XConfirmDialog xConfirmDialog = new XConfirmDialog(this);
        xConfirmDialog.setText("简易的确认Dialog");
        xConfirmDialog.show();
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
            protected void initDialogContent() {
                TextView textView = findViewById(R.id.textView);
                textView.setText("任意位置Dialog");
                findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        };
        positionDialog.setCanceledOnTouchOutside(false);
        positionDialog.setPosition(x, y);
        positionDialog.show();
    }

    private MyAttachDialog attachDialog;

    /**
     * 依附于View的Dialog
     * AttachDialog.Direction - 指明Dialog位于View的上下左右方位
     * AttachDialog.Align - 表明对齐方式，TOP、BOTTOM（上、下边界对齐只对左右位置生效），LEFT、RIGHT（左、右边界对齐只对上下位置生效）
     */
    private void showAttachDialog() {
        if (attachDialog == null) {
            attachDialog = new MyAttachDialog(this);
            attachDialog.setText("依附于View的Dialog(替代PopupView)");
            attachDialog.attach(findViewById(R.id.tvAttach), AttachDialog.Direction.BOTTOM, AttachDialog.Align.RIGHT);
        }
        attachDialog.show();
    }

    /**
     * 展示跳转Dialog
     * Activity finish时自动销毁Dialog
     */
    private void showLifeCycleDialog() {
        LifeCycleDialog lifeCycleDialog = new LifeCycleDialog(this);
        lifeCycleDialog.show();
    }


}
