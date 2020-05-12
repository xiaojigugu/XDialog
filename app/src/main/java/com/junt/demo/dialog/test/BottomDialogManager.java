package com.junt.demo.dialog.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.junt.demo.R;
import com.junt.xdialog.anim.XAnimator;
import com.junt.xdialog.callbacks.XDialogLifeCallBack;
import com.junt.xdialog.callbacks.XDialogLifeCallbackImpl;
import com.junt.xdialog.core.XCoreDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BottomDialogManager {

    private FirstBottomDialog firstBottomDialog;
    private SecondBottomDialog secondBottomDialog;
    private FirstDialogAnimator firstDialogAnimator;
    private SecondDialogAnimator secondDialogAnimator;
    private Context context;
    private int touchSlop;

    private int bottomExtra = 0;

    private float downX, downY;
    private Handler handler = new Handler(Looper.getMainLooper());
    /**
     * 是否允许触摸事件执行
     */
    private boolean canTouch = true;

    public BottomDialogManager(Context context) {
        this.context = context;
        firstDialogAnimator = new FirstDialogAnimator();
        firstBottomDialog = new FirstBottomDialog(context, firstDialogAnimator);
        secondDialogAnimator = new SecondDialogAnimator();
        secondBottomDialog = new SecondBottomDialog(context, secondDialogAnimator);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void showFirst() {
        firstBottomDialog.show();
    }

    public void showSecond() {
        secondBottomDialog.show();
    }

    public void setBottomExtra(int bottomExtra) {
        this.bottomExtra = bottomExtra;
    }

    /**
     * 第一个Dialog
     */
    private class FirstBottomDialog extends XCoreDialog {

        List<String> list;
        FirstAdapter firstAdapter;
        private NestedScrollView nestedScrollView;

        public FirstBottomDialog(@NonNull Context context, XAnimator xAnimator) {
            super(context, xAnimator);
            list = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                list.add("测试" + i);
            }
        }

        @Override
        protected int getImplLayoutResId() {
            return R.layout.dialog_test_first;
        }

        @Override
        protected void initDialogContent() {
            paddingRoot(0, 0, 0, bottomExtra);
            nestedScrollView = findViewById(R.id.nestedScrollView);
            RecyclerView rvFirst = findViewById(R.id.rvFirst);
            rvFirst.setLayoutManager(new GridLayoutManager(context, 4));
            rvFirst.setNestedScrollingEnabled(false);
            firstAdapter = new FirstAdapter(getRealContext(), list, new FirstAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    firstDialogAnimator.animShowing();
                    showSecond();
                }
            });
            rvFirst.setAdapter(firstAdapter);
        }

        @Override
        public Drawable getBackgroundDrawable() {
            return new ColorDrawable(Color.TRANSPARENT);
        }

        @Override
        public boolean onContainerInterceptTouchEvent(@NonNull MotionEvent ev) {
            boolean isIntercept = false;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = ev.getX();
                    downY = ev.getY();
                    isIntercept = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (canTouch && getScrollTag(ev) != ScrollTag.TAP) {
                        if ((getScrollTag(ev) == ScrollTag.DOWN && isScrollToTop(nestedScrollView))
                                || getScrollTag(ev) == ScrollTag.UP && isScrollToBottom(nestedScrollView)) {
                            isIntercept = true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return isIntercept;
        }

        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (getScrollTag(event) == ScrollTag.UP || getScrollTag(event) == ScrollTag.DOWN)
                        getDialogView().setTranslationY(firstDialogAnimator.getCurrentTransY() + (event.getY() - downY));
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (canTouch && getScrollTag(event) != ScrollTag.TAP) {
                        if (!firstDialogAnimator.shouldResumeLocation()) {
                            ScrollTag scrollTag = getScrollTag(event);
                            if (scrollTag == ScrollTag.DOWN) {
                                //手势-向下滑动，则判断ScrollView是否滑至顶部
                                if (isScrollToTop(nestedScrollView)) {
                                    pauseTouchEvent();
                                    firstDialogAnimator.onScrollToTop();
                                }
                            } else if (scrollTag == ScrollTag.UP) {
                                //手势-向上滑动，则判断ScrollView是否滑至底部
                                if (isScrollToBottom(nestedScrollView)) {
                                    pauseTouchEvent();
                                    firstDialogAnimator.onScrollToBottom();
                                }
                            }
                        } else {
                            firstDialogAnimator.resumeLocation();
                        }
                    }
                    break;
            }
            return super.onTouchEvent(event);
        }
    }

    /**
     * 暂停触摸事件，等待动画执行完毕
     */
    private void pauseTouchEvent() {
        canTouch = false;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                canTouch = true;
            }
        }, firstDialogAnimator.ANIM_DURATION);
    }

    /**
     * 第二个Dialog
     */
    private class SecondBottomDialog extends XCoreDialog {
        List<String> list;
        SecondAdapter secondAdapter;
        NestedScrollView nestedScrollView;

        public SecondBottomDialog(@NonNull Context context, XAnimator xAnimator) {
            super(context, xAnimator);
            list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                list.add("测试" + i);
            }
            setCanceledOnTouchOutside(false);
        }

        @Override
        protected int getImplLayoutResId() {
            return R.layout.dialog_test_second;
        }

        @Override
        protected void initDialogContent() {
            paddingRoot(0, 0, 0, bottomExtra);
            nestedScrollView = findViewById(R.id.nestedScrollView);
            findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backToFirstDialog();
                }
            });

            findViewById(R.id.ivRefresh).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(0);
                    list.remove(0);
                    list.remove(0);
                    list.remove(0);
                    list.remove(0);
                    list.add("测试刷新");
                    secondAdapter.notifyDataSetChanged();
                }
            });

            RecyclerView rvSecond = findViewById(R.id.rvSecond);
            rvSecond.setNestedScrollingEnabled(false);
            rvSecond.setLayoutManager(new LinearLayoutManager(context));
            secondAdapter = new SecondAdapter(getRealContext(), list);
            rvSecond.setAdapter(secondAdapter);
        }

        @Override
        public Drawable getBackgroundDrawable() {
            return new ColorDrawable(Color.TRANSPARENT);
        }


        @Override
        public boolean onContainerInterceptTouchEvent(@NonNull MotionEvent ev) {
            System.out.println(getClass().getSimpleName() + "onContainerInterceptTouchEvent.action:" + ev.getAction());
            boolean isIntercept = false;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = ev.getX();
                    downY = ev.getY();
                    isIntercept = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (canTouch && getScrollTag(ev) != ScrollTag.TAP) {
                        if ((getScrollTag(ev) == ScrollTag.DOWN && isScrollToTop(nestedScrollView))
                                || getScrollTag(ev) == ScrollTag.UP && isScrollToBottom(nestedScrollView)) {
                            isIntercept = true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return isIntercept;
        }

        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            System.out.println(getClass().getSimpleName() + ".onTouchEvent.action:" + event.getAction());
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    getDialogView().setTranslationY(secondDialogAnimator.getCurrentTransY() + (event.getY() - downY));
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (canTouch && getScrollTag(event) != ScrollTag.TAP) {
                        if (!secondDialogAnimator.shouldResumeLocation()) {
                            ScrollTag scrollTag = getScrollTag(event);
                            if (scrollTag == ScrollTag.DOWN) {
                                //手势-向下滑动，则判断ScrollView是否滑至顶部
                                if (isScrollToTop(nestedScrollView)) {
                                    pauseTouchEvent();
                                    secondDialogAnimator.onScrollToTop();
                                }
                            } else if (scrollTag == ScrollTag.UP) {
                                //手势-向上滑动，则判断ScrollView是否滑至底部
                                if (isScrollToBottom(nestedScrollView)) {
                                    pauseTouchEvent();
                                    secondDialogAnimator.onScrollToBottom();
                                }
                            }
                        } else {
                            secondDialogAnimator.resumeLocation();
                        }
                    }
                    break;
            }
            return super.onTouchEvent(event);
        }

        @Override
        public XDialogLifeCallBack getXDialogCallBack() {
            return new XDialogLifeCallbackImpl() {
                @Override
                public void onTouchOutside() {
                    super.onTouchOutside();
                    backToFirstDialog();
                }
            };
        }

        @Override
        public void onBackPressed() {
            backToFirstDialog();
        }

        private void backToFirstDialog() {
            firstDialogAnimator.animShowing();
            secondBottomDialog.dismiss();
        }
    }

    /**
     * NestedScrollView是否还能向上滚动
     */
    public boolean isScrollToBottom(ViewGroup nestedScrollView) {
        return !nestedScrollView.canScrollVertically(1);
    }

    /**
     * NestedScrollView是否滑动至顶部
     */
    public boolean isScrollToTop(View nestedScrollView) {
        return !nestedScrollView.canScrollVertically(-1);
    }

    /**
     * 获取滑动标识
     */
    public ScrollTag getScrollTag(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        double distance = Math.hypot(x - downX, y - downY);
        if (distance > touchSlop) {
            float moveX = Math.abs(x - downX);
            moveX = moveX == 0 ? 1 : moveX;
            double acos = Math.acos(moveX / distance);
            if (acos > Math.toRadians(30)) {
                //垂直滑动
                if (y > downY) {
                    //向下滑动
                    return ScrollTag.DOWN;
                }
                return ScrollTag.UP;
            }
            return ScrollTag.HORIZONTAL;
        }
        return ScrollTag.TAP;
    }

    /**
     * 滑动标识
     */
    enum ScrollTag {
        UP,
        DOWN,
        HORIZONTAL,
        TAP
    }
}
