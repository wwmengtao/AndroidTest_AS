package com.example.testmodule.windowmanager;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.example.androidcommon.windowmanager.WindowTools;
import com.example.testmodule.ALog;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mengtao1 on 2017/12/7.
 */

public class FloatingStatusBarView extends View{
    private static final int colorPortrait = android.R.color.holo_red_light;//定义手机竖屏时FloatingStatusBarView背景颜色
    private static final int colorHorizontal = android.R.color.holo_orange_dark;//定义手机横屏时FloatingStatusBarView背景颜色
    private Context mContext = null;
    private WindowManager mWindowManager = null;
    private AtomicBoolean isViewAdded = new AtomicBoolean(false);

    public FloatingStatusBarView(Context context) {
        super(context);
        this.mContext = context.getApplicationContext();
        this.mWindowManager = (WindowManager) mContext.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        setFSLayoutParams();
    }

    public boolean isViewAdded(){
        return this.isViewAdded.get();
    }

    public void setViewAdded(boolean isViewAdded){
        this.isViewAdded.set(isViewAdded);
    }

    /**
     * setFSLayoutParams：设置自身的悬浮布局参数，因为转屏时，状态栏的宽高可能都会发生变化
     */
    public void setFSLayoutParams() {
        Pair<Integer, Integer> mPair = WindowTools.getScreenRealWidthHeight(mContext);
        int statusBarWidth = mPair.first;
        if(statusBarWidth < 0)statusBarWidth = 30;//设置默认数值
        int statusBarHeight = WindowTools.getStatusBarHeight(mContext);//状态栏高度数值
        ALog.Log1("statusBarHeight: "+statusBarHeight);
        int navigationBarHeight = WindowTools.getNavigationBarHeight(mContext);//底部导航栏高度数值
        ALog.Log1("navigationBarHeight: "+navigationBarHeight);
        //
        int windowType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            windowType = LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            windowType = LayoutParams.TYPE_PHONE;
        }
        LayoutParams mLayoutParams = new LayoutParams(
            statusBarWidth,
            statusBarHeight,
            windowType,
        LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | LayoutParams.FLAG_FULLSCREEN
                | LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mLayoutParams.gravity = Gravity.TOP | Gravity.START;
        //
        setLayoutParams(mLayoutParams);//设置悬浮窗布局参数
    }

    public void addView(){
        if (!WindowTools.canDrawOverlays(mContext))return;
        if(!isViewAdded()){
            mWindowManager.addView(this, getLayoutParams());
            setViewAdded(true);
            ALog.Log1("addView");
        }
    }

    /**
     * updateView：更新视图
     * @param colorID：数值大于0，设置用户传进来的颜色；数值<0，使用预定义的横竖屏颜色。
     */
    public void updateView(int colorID){
        if (!WindowTools.canDrawOverlays(mContext))return;
        if(colorID > 0){
            setBackgroundColor(mContext.getResources().getColor(colorID));
        }else{//默认使用预定义的横竖屏背景颜色
            setStatusBarViewBackgroundColor();
        }
        if(!isViewAdded()){
            addView();
        }else {
            mWindowManager.updateViewLayout(this, getLayoutParams());
            ALog.Log1("updateView");
        }
    }

    /**
     * setStatusBarViewBackgroundColor：设置预定义横竖屏背景颜色
     */
    private void setStatusBarViewBackgroundColor(){
        int orientation = this.mContext.getResources().getConfiguration().orientation;
        boolean isPortrait = (Configuration.ORIENTATION_PORTRAIT == orientation);
        setBackgroundColor(mContext.getResources().getColor(isPortrait ? colorPortrait : colorHorizontal));
    }

    public void removeView(){
        if(isViewAdded()){
            mWindowManager.removeView(this);
            setViewAdded(false);
            ALog.Log1("removeView");
        }
    }
}
