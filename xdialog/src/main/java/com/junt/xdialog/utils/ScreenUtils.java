package com.junt.xdialog.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtils {
    private static Rect rect = new Rect();

    public static Rect getScreenRect(Context context) {
        getDefaultDisplay(context).getRectSize(rect);
        return rect;
    }

    private static Display getDefaultDisplay(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }
}
