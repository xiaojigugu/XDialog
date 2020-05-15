# XDialog
[![](https://jitpack.io/v/com.gitee.giteeguguji/XDialog.svg)](https://jitpack.io/#com.gitee.giteeguguji/XDialog)
#### 介绍
简单易用的Dialog封装类-XDialog

<img src="https://gitee.com/giteeguguji/XDialog/raw/master/app/src/main/images/sample.gif" width="30%" height="30%" onerror="this.src='https://raw.githubusercontent.com/xiaojigugu/XDialog/master/app/src/main/images/sample.gif'">

#### 软件架构 
基于Dialog进行二次封装  
* 修改Dialog的Theme并使Window背景透明
* 自定义ViewGroup填充Dialog的DecoreView，并使该ViewGroup宽高皆MATCH_PARENT
* 所有Dialog的布局将会以该ViewGroup作为根布局进行填充渲染
* 自定义XAnimator动画类用于对所有Dialog布局对应的View进行动画效果施加
* 监听Activity的生命周期，Activirty销毁时(onDestroy)自动dismiss

#### 引用

Add it in your root build.gradle at the end of repositories:  
```  
    allprojects {  
        repositories {  
			...  
			maven { url 'https://jitpack.io' }  
		}  
	}  
```  
Step 2. Add the dependency  
```  
	dependencies {  
	        implementation 'com.gitee.giteeguguji:XDialog:v1.0'  
	}  
```  
#### 使用
#####  自定义Dialog  
1. 编写layout布局文件
```xml
<?xml version="1.0" encoding="utf-8"?>
<!--根布局里的layout_gravity将会直接影响Dialog的位置-->
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="250dp"
    android:layout_height="150dp"
    android:layout_gravity="center"
    android:background="@drawable/shape_round_corner"
    android:orientation="vertical">

    <TextView
        android:id="@+id/textView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:layout_marginBottom="20dp"
        android:gravity="center"
        android:textColor="#000000"
        android:textSize="17sp"
        tools:text="这是一个弹框提示" />

    <TextView
        android:id="@+id/tvConfirm"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="20dp"
        android:text="@string/confirm"
        android:textColor="#03A9F4" />
</FrameLayout>
```  

2. 自定义Dialog类
```java
public class XConfirmDialog extends XCoreDialog {

    private String text;
    private TextView textView;

    /**
     * 使用该默认构造方法将自动使用默认的Scale动画
     * @param context activity
     */
    public XConfirmDialog(@NonNull Context context) {
        super(context);
    }

    /**
     * 使用该构造方法可传入自定义的XAnimator
     * @param context activity
     * @param xAnimator 自定义动画
     */
    public XConfirmDialog(@NonNull Context context, XAnimator xAnimator) {
        super(context, xAnimator);
    }

    /**
     * 传递布局文件
     */
    @Override
    protected int getImplLayoutResId() {
        return R.layout.dialog_simple;
    }

    /**
     * 初始化View的内容
     */
    @Override
    protected void initDialogContent() {
        findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        textView = findViewById(R.id.textView);
        textView.setText(text);
    }

    public void setText(String text) {
        this.text = text;
        if (textView != null) {
            textView.setText(text);
        }
    }
}
```

3. 使用Dialog
<pre><code class="java">
  private void showSimpleConfirmDialog() {
        XConfirmDialog xConfirmDialog = new XConfirmDialog(MainActivity.this);
        xConfirmDialog.setText(&quot;简易的确认Dialog&quot;);
        xConfirmDialog.show();
    }
</code></pre>
<img src="https://gitee.com/giteeguguji/XDialog/raw/master/app/src/main/images/simple.png" height="30%" width="30%" onerror="this.src='https://raw.githubusercontent.com/xiaojigugu/XDialog/master/app/src/main/images/simple.png'">

4. 自定义动画  
这里以内部默认的缩放动画为例
```java
/**
 * 缩放动画
 */
public class XAnimatorScale extends XAnimator {
    /**
     * 动画未开始时DialogView的初始状态
     */
    @Override
    public void initAnim() {
        //getView()获取到Dialog布局对应的View
        //将目标DialogView缩放设置为0
        getView().setScaleX(0);
        getView().setScaleY(0);
    }

    @Override
    public void animShow() {
        //将目标DialogView的缩放级别过度到1
        getView().animate().scaleX(1f).scaleY(1f).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void animShowing() {
        //DialogView完全可见时可在这里继续施加动画
        //需要自己手动调用
        //eg. XAnimatorScale xAnimatorScale=new XAnimatorScale();
        //    xAnimatorScale.animShowing()
    }

    @Override
    public void animDismiss() {
        getView().animate().scaleX(0f).scaleY(0f).setDuration(ANIM_DURATION).start();
    }
}
```
<pre><code class="java">
//使用缩放动画
XConfirmDialog xConfirmDialog = new XConfirmDialog(MainActivity.this,new XAnimatorScale());
xConfirmDialog.setText(&quot;简易的确认Dialog&quot;);
xConfirmDialog.show();
</code></pre>

5. 设置背景色
背景色默认为有透明度的灰色阴影,若需要需改，可在自定义的Dialog类中复写以下方法：
<pre><code class="java">
  @Override
    public Drawable getBackgroundDrawable() {
        //这里将背景色修改为透明背景
        return new ColorDrawable(Color.TRANSPARENT);
    }
</code></pre>

6. 事件监听
监听Dialog的生命周期，复写以下方法：
<pre>
<code class="java">
    @Override
    public XDialogLifeCallBack getXDialogCallBack() {
        return new XDialogLifeCallbackImpl(){
            @Override
            public void onCreateInstance(XCoreDialog XCoreDialog) {
                super.onCreateInstance(XCoreDialog);
                //Dialog类实例化完成
            }

            @Override
            public void onCreate() {
                super.onCreate();
                //Dialog创建完成，创建完成后不会再次创建直至onDesteroy
            }

            @Override
            public void onContentReady(View dialogView) {
                super.onContentReady(dialogView);
               //DialogView已创建并添加进Dialog中
               //此处不能进行宽高获取等操作，若有需要请复写onDialogViewCreated();
            }

            @Override
            public void onAnimatorBindDialogView(XAnimator xAnimator) {
                super.onAnimatorBindDialogView(xAnimator);
                //自定义动画类已经绑定了DialogView但还未调用initAnim()
            }

            @Override
            public void onAnimInitialized(XAnimator xAnimator) {
                super.onAnimInitialized(xAnimator);
                //动画完全初始化
            }

            @Override
            public void onShow() {
                super.onShow();
                //Dialog显示动画播放完毕，Dialog完全显示
            }

            @Override
            public void onDestroy() {
                super.onDestroy();
                //Dialog销毁，再次使用会重新onCreate()
            }

            @Override
            public void onDismiss() {
                super.onDismiss();
                //Dialog隐藏动画播放完毕，Dialog完全隐藏
            }

            @Override
            public void onTouchOutside(MotionEvent ev) {
                super.onTouchOutside(ev);
                //触摸了Dialog外部区域
            }
        };
    }
</code>
</pre>

7. 点击外部区域是否dismiss，调用原Dialog的方法即可
<pre><code class="java">
positionDialog.setCanceledOnTouchOutside(false);
</code></pre>

8. 事件分发 
* 对Dialog的根布局Layout进行事件处理可以复写以下方法
<pre><code class="java">
        /**
         * 重写该方法进行事件拦截，拦截后会进入onContainerTouchEvent
         * 外部拦截时请注意不要拦截down，up事件，否则子View将收不到任何点击事件
         */
        @Override
        public boolean onContainerInterceptTouchEvent(@NonNull MotionEvent ev) {
            boolean isIntercept = false;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isIntercept = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isIntercept = false;
                    break;
            }
            return isIntercept;
        }

        /**
         * 重写该方法进行触摸事件处理
         * @return false-进入onTouchEvent true-消费事件
         */
        @Override
        public boolean onContainerTouchEvent(@NonNull MotionEvent ev) {
            return super.onContainerTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            return super.onTouchEvent(event);
        }
</code></pre>
* 事件穿透  
如果需要在点击Dialog外部区域时底层的界面能够响应触摸事件，则做如下处理：
<pre><code class="java">
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        ((Activity) getRealContext()).dispatchTouchEvent(event);
        return false;
    }
</code></pre>
