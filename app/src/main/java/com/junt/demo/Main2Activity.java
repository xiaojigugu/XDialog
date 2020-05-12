package com.junt.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.junt.demo.dialog.test.BottomDialogManager;

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
}
