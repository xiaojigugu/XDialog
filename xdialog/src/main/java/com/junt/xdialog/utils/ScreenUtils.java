package com.junt.xdialog.utils;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtils {
    private static Point point = new Point();

    public static Point getScreenPoint(Context context) {
        getDefaultDisplay(context).getRealSize(point);
        return point;
    }

    private static Display getDefaultDisplay(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }
}
