package com.example.testmodule.windowmanager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.example.testmodule.ALog;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mengtao1 on 2017/12/5.
 */

public class StatusBarViewManager implements WindowHelper.OnScreenChangedListener{
    private static final String TAG = "StatusBarViewManager";
    private volatile static StatusBarViewManager mStatusBarViewManager = null;
    private Context mContext = null;
    private WindowManager mWindowManager = null;
    private WindowHelper mWindowHelper = null;
    private StatusBarView mStatusBarView= null;
    private LayoutParams statusBarViewParams = null;

    public static synchronized StatusBarViewManager getInstance(Context mContext) {
        if (null == mStatusBarViewManager) {
            mStatusBarViewManager = new StatusBarViewManager(mContext);
        }
        return mStatusBarViewManager;
    }

    private StatusBarViewManager(Context mContext){
        init(mContext);
    }

    private void init(Context mContext){
        this.mContext = mContext;
        this.mWindowManager = (WindowManager) mContext.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        this.mStatusBarView = new StatusBarView(mContext);
        //使用WindowHelper监听窗口状态变化
        mWindowHelper = new WindowHelper(mContext);
        mWindowHelper.registerScreenChangedListener(this);
        mWindowHelper.init();
        //
        initLayoutParams();
        //
        mStatusBarView.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_light));
        addView();
    }

    private void initLayoutParams(){

        int statusBarHeight = 30, statusBarWidth = -1;
        //获取status_bar_height资源的ID
        int heightId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (heightId > 0) {
            statusBarHeight = mContext.getResources().getDimensionPixelSize(heightId);
        }
        Point size = new Point();
        mWindowManager.getDefaultDisplay().getSize(size);
        statusBarWidth = size.x;
        int windowType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            windowType = LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            windowType = LayoutParams.TYPE_PHONE;
        }
        statusBarViewParams = new LayoutParams(
                statusBarWidth,
                statusBarHeight,
                windowType,
                LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | LayoutParams.FLAG_FULLSCREEN
                        | LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        statusBarViewParams.gravity = Gravity.TOP | Gravity.START;
    }

    private void addView(){
        if (!canDrawOverlays())return;
        if(!mStatusBarView.isViewAdded()){
            mWindowManager.addView(mStatusBarView, statusBarViewParams);
            mStatusBarView.setViewAdded(true);
            ALog.Log("addView");
        }
    }

    public void updateView(int colorID){
        if (!canDrawOverlays())return;
        mStatusBarView.setBackgroundColor(mContext.getResources().getColor(colorID));
        if(!mStatusBarView.isViewAdded()){
            addView();
        }else {
            mWindowManager.updateViewLayout(mStatusBarView, statusBarViewParams);
            ALog.Log("updateView");
        }
    }

    public void removeView(){
        if(mStatusBarView.isViewAdded()){
            mWindowManager.removeView(mStatusBarView);
            mStatusBarView.setViewAdded(false);
            ALog.Log("removeView");
        }
    }

    public void onDestroy(){
        removeView();
        mWindowHelper.release();//取消监听屏幕全屏、转屏等变化。
    }

    private boolean canDrawOverlays(){
        boolean canDrawOverlays = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            canDrawOverlays = true;
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canDrawOverlays = (Settings.canDrawOverlays(mContext))? true : false;
        }
        return canDrawOverlays;
    }

    @Override
    public void onScreenChanged(boolean fullscreenChanged, boolean isFullscreen, boolean rotationChanged, boolean isPortrait, int screenWidth, int screenHeight, int statusBarHeight) {
        ALog.Log(TAG+"_fullscreenChanged: "+fullscreenChanged+" isFullscreen: "+isFullscreen+" rotationChanged: "+rotationChanged+
                " isPortrait: "+isPortrait);
        if(isFullscreen){//说明有相机之类的应用占满当前窗口，因此此时状态栏不需要显示背景色view
            removeView();
        }else{
            updateView(android.R.color.holo_green_light);
        }
        if(rotationChanged){
            initLayoutParams();
            updateView(isPortrait ? android.R.color.holo_blue_dark: android.R.color.holo_red_light);
        }
    }

    private class StatusBarView extends View{
        private AtomicBoolean isViewAdded = new AtomicBoolean(false);

        public StatusBarView(Context context) {
            super(context);
        }

        public boolean isViewAdded(){
            return this.isViewAdded.get();
        }

        public void setViewAdded(boolean isViewAdded){
            this.isViewAdded.set(isViewAdded);
        }
    }
}
