package com.junt.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.junt.xdialog.core.BottomDialog;
import com.junt.xdialog.core.PositionDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            showPositionDialog((int) event.getRawX(), (int) event.getRawY());
        }
        return super.onTouchEvent(event);
    }

    public void onTextClicked(View view) {
        System.out.println("点击了TextView");
        showBottomDialog();
    }

    private void showBottomDialog() {
        BottomDialog bottomDialog=new BottomDialog(this) {
            @Override
            protected int getImplLayoutResId() {
                return R.layout.dialog_bottom;
            }

            @Override
            protected void initViewContent() {

            }
        };
        bottomDialog.show();
    }

    private void showPositionDialog(int x, int y) {
        PositionDialog positionDialog = new PositionDialog(this) {
            @Override
            protected int getImplLayoutResId() {
                return R.layout.dialog_simple;
            }

            @Override
            protected void initViewContent() {

            }
        };
        positionDialog.setPosition(x, y);
        positionDialog.show();
    }
}
