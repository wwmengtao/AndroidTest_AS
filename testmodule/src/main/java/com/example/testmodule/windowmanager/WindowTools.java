package com.example.testmodule.windowmanager;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Pair;
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

    /**
     * getScreenRealWidthHeight：记录手机当前真实的宽高数值，包括状态栏高度、用户可用高度以及底部导航栏高度(如果有的话)。
     * 在有底部导航栏的情况下，此时获取的高度数值等于getScreenWidthHeight方法获取的高度值+getNavigationBarHeight
     * 获取的导航栏高度数值。
     * @param context
     * @return
     */
    public static Pair<Integer, Integer> getScreenRealWidthHeight(Context context){
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int mRealScreenWidth, mRealScreenHeight;
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {//JELLY_BEAN_MR1数值为17
            mWindowManager.getDefaultDisplay().getRealSize(size);
            mRealScreenWidth = size.x;
            mRealScreenHeight = size.y;
        }else {
            mWindowManager.getDefaultDisplay().getSize(size);
            mRealScreenWidth = size.x;
            mRealScreenHeight = size.y;
        }
        Pair<Integer, Integer> mPair = new Pair<>(mRealScreenWidth , mRealScreenHeight);
        return mPair;
    }

    //记录手机当前的宽高数值，高度数值不包括导航栏的高度(如果有导航栏的话)
    public static Pair<Integer, Integer> getScreenWidthHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Pair<Integer, Integer> mPair = new Pair<>(dm.widthPixels, dm.heightPixels);
        return mPair;
    }

    /**
     * getStatusBarHeight：获取手机状态栏的高度
     * @param mContext
     * @return
     */
    public static int getStatusBarHeight(Context mContext){
        int height = -1;
        //获取status_bar_height资源的ID
        int heightId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (heightId > 0) {
            height = mContext.getResources().getDimensionPixelSize(heightId);
        }
        return height;
    }

    /**
     * getNavigationBarHeight：获取手机导航栏的高度(如果有的话)
     * @param mContext
     * @return
     */
    public static int getNavigationBarHeight(Context mContext) {
        int height = -1;
        int heightId = mContext.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (heightId > 0) {
            height = mContext.getResources().getDimensionPixelSize(heightId);
        }
        return height;
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
