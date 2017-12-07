package com.example.testmodule.windowmanager;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;

/**
 * Created by mengtao1 on 2017/12/7.
 */

public class WindowTools {
    /**
     * canDrawOverlays：判断用户是否允许显示悬浮视图
     * @return
     */
    public static boolean canDrawOverlays(Context context){
        if(null == context)return false;
        boolean canDrawOverlays = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            canDrawOverlays = true;
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canDrawOverlays = (Settings.canDrawOverlays(context))? true : false;
        }
        return canDrawOverlays;
    }

    /**
     * dip2px：将dp数值转换为px数值
     * @param context
     * @param dp
     * @return
     */
    public static int dip2px(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    //记录手机当前宽高数值
    public static void getScreenRealWidthHeight(Context context){
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int mRealScreenWidth, mRealScreenHeight;
        Point size = new Point();
        //以下获取手机真实高度，包括状态栏高度、用户可用高度以及底部导航栏高度(如果有的话)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {//JELLY_BEAN_MR1数值为17
            mWindowManager.getDefaultDisplay().getRealSize(size);
            mRealScreenWidth = size.x;
            mRealScreenHeight = size.y;
        }else {
            mWindowManager.getDefaultDisplay().getSize(size);
            mRealScreenWidth = size.x;
            mRealScreenHeight = size.y;
        }
    }

    /**
     * addOnGlobalLayoutListener：为view注册ViewTreeObserver.OnGlobalLayoutListener监听器
     * @param view
     * @param l
     */
    public static void addOnGlobalLayoutListener(View view, OnGlobalLayoutListener l){
        view.getViewTreeObserver().addOnGlobalLayoutListener(l);
    }

    /**
     * removeOnGlobalLayoutListener：为view注销ViewTreeObserver.OnGlobalLayoutListener监听器
     * @param view
     * @param l
     */
    public static void removeOnGlobalLayoutListener(View view, OnGlobalLayoutListener l) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(l);
        } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(l);
        }
    }
}
