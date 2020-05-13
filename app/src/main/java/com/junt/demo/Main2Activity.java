package com.junt.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.junt.demo.dialog.test.BottomDialogManager;
import com.junt.xdialog.impl.XMessage;

public class Main2Activity extends AppCompatActivity {

    private BottomDialogManager bottomDialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottomDialogManager = new BottomDialogManager(this);
    }

    public void show(View view) {
        bottomDialogManager.setBottomExtra(findViewById(R.id.tvSimulateTab).getHeight());
        bottomDialogManager.showFirst();
    }

    @Override
    protected void onStart() {
        super.onStart();
        XMessage.makeText(Main2Activity.this, "跨Activity吐司", XMessage.Duration.LENGTH_LONG).show(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }
}
