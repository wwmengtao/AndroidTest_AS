package com.example.testmodule.windowmanager;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;


public class WindowHelper {
    private static final String TAG = "WindowHelper";

    private static final int WINDOW_TYPE_WITHOUT_PERMISSION =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ?
                    LayoutParams.TYPE_TOAST :
                    LayoutParams.TYPE_PHONE;

    public static final boolean FLOATING_ICON_HAS_STABLE_POSTION =
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    public static final boolean WINDOW_TYPE_NEED_PERMISSION = true;
    // measure window must be TYPE_PHONE, otherwise it can not detect change of screen.
    public static int FLOATING_MEASURE_WINDOW_TYPE = LayoutParams.TYPE_PHONE;
    // floating icon must be TYPE_TOAST or TYPE_SYSTEM_ALERT, because it has stable position after entered full screen.
    public static final int FLOATING_FG_WINDOW_TYPE = LayoutParams.TYPE_SYSTEM_ALERT;


    private Context mContext;
    private WindowManager mWindowManager;
    private View mMeasureView;
    private OnScreenChangedListener mListener;
    private static int mStatusBarHeight = 100;
    private static int mRealStatusBarHeight = 100;
    private static int mRealScreenWidth = 720;
    private static int mRealScreenHeight = 1280;
    private static int mScreenWidth = 720;
    private static int mScreenHeight = 1280;
    private static int mAvailableScreenHeight = 1280;
    private static int mPanelGap = 10;
    private static int mAdjustX = 0;
    private static boolean mIsPortrait = true;
    private static boolean mIsFullscreen = false;

    public static int getFloatingBGWindowType() {
        return LayoutParams.TYPE_PHONE;
    }

    private OnGlobalLayoutListener mGlobalLayoutListener = new OnGlobalLayoutListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onGlobalLayout() {
            int h = mMeasureView.getHeight();
            mAvailableScreenHeight = h;

            boolean rotationChanged = false;
            int rotation = mWindowManager.getDefaultDisplay().getRotation();
            boolean isPortrait = (rotation == Surface.ROTATION_0) || (rotation == Surface.ROTATION_180);
            if (mIsPortrait != isPortrait) {
                mIsPortrait = isPortrait;
                rotationChanged = true;
            }

            Point size = new Point();
            mWindowManager.getDefaultDisplay().getSize(size);
            mRealScreenWidth = mScreenWidth = size.x;
            mRealScreenHeight = mScreenHeight = size.y;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mWindowManager.getDefaultDisplay().getRealSize(size);
                mRealScreenWidth = size.x;
                mRealScreenHeight = size.y;
            }
            if (h > mScreenHeight) {
                mScreenHeight = mRealScreenHeight;
            }

            boolean fullscreenChanged = false;
            boolean isFullscreen = (h == mScreenHeight || h == mScreenWidth);
            if (mIsFullscreen != isFullscreen) {
                mIsFullscreen = isFullscreen;
                fullscreenChanged = true;
            }

            mAdjustX = 0;
            if (mIsFullscreen && mScreenWidth != mRealScreenWidth) {
                if (rotation == Surface.ROTATION_90) {
                    mScreenWidth = mRealScreenWidth;
                } else if (rotation == Surface.ROTATION_270) {
                    mAdjustX = mScreenWidth - mRealScreenWidth;
                }
            }

            if (h > mScreenHeight*2/3) {
                mStatusBarHeight = mScreenHeight - h;
                if (mStatusBarHeight > 0) {
                    mRealStatusBarHeight = mStatusBarHeight;
                }
            } else {
                // in case h < 2/3 screen height, there is an error, ignore it.
                return;
            }

            if (mListener != null) {
                mListener.onScreenChanged(fullscreenChanged, mIsFullscreen, rotationChanged,
                        mIsPortrait, mScreenWidth, mScreenHeight, mStatusBarHeight);
            }
        }
    };

    public WindowHelper(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
    }

    public WindowHelper(Context context, WindowManager wm) {
        mContext = context;
        mWindowManager = wm;
    }

    public static boolean canDrawOverlays(Context context) {
        return  true;
    }

    public void init() {
        if (mMeasureView != null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            FLOATING_MEASURE_WINDOW_TYPE = LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            FLOATING_MEASURE_WINDOW_TYPE = LayoutParams.TYPE_PHONE;
        }
        LayoutParams localLayoutParams = new LayoutParams();
                localLayoutParams.type = FLOATING_MEASURE_WINDOW_TYPE;
                localLayoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
                localLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | LayoutParams.FLAG_NOT_FOCUSABLE;
                localLayoutParams.width = 0;
                localLayoutParams.height = LayoutParams.MATCH_PARENT;
                localLayoutParams.format = PixelFormat.TRANSPARENT;

        mMeasureView = new View(mContext.getApplicationContext());
        
        Point size = new Point();
        mWindowManager.getDefaultDisplay().getSize(size);
        mScreenHeight = mAvailableScreenHeight = size.y;
        mScreenWidth = size.x;
        int rotation = mWindowManager.getDefaultDisplay().getRotation();
        mIsPortrait = (rotation == Surface.ROTATION_0) || (rotation == Surface.ROTATION_180);
        mPanelGap = dip2px(mContext, 20);
        
        mWindowManager.addView(mMeasureView, localLayoutParams);
        mMeasureView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);


    }
    
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void removeOnGlobalLayoutListener(View view, OnGlobalLayoutListener l) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(l);
        } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(l);
        }
    }
    
    public void release() {
        if (mMeasureView != null) {
            removeOnGlobalLayoutListener(mMeasureView, mGlobalLayoutListener);
            mWindowManager.removeView(mMeasureView);
            mMeasureView = null;
        }
    }

    public static int dip2px(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }
    
    public static int getPanelTriggerGap() {
        return mPanelGap;
    }
    
    public static int getStatusBarHeight() {
        return mStatusBarHeight;
    }
    
    public static int getRealStatusBarHeight() {
        return mRealStatusBarHeight;
    }

    public static int getScreenWidth() {
        return mScreenWidth;
    }

    public static int getScreenHeight() {
        return mScreenHeight;
    }

    public static int getRealScreenHeight() {
        return mRealScreenHeight;
    }

    public static int getAvailableScreenHeight() {
        return mAvailableScreenHeight;
    }
    
    public static int getSmallestScreenWidth() {
        return Math.min(mScreenWidth, mScreenHeight);
    }

    public static boolean isPortrait() {
        return mIsPortrait;
    }
    
    public static boolean isFullscreen() {
        return mIsFullscreen;
    }
    
    public static int getFloatingViewAdjustY() {
        // the TYPE_SYSTEM_ALERT's location is not stable before KITKAT.
        if (FLOATING_ICON_HAS_STABLE_POSTION) {
            return getRealStatusBarHeight();
        } else {
            return getStatusBarHeight();
        }
    }

    public static int getFloatingViewAdjustX() {
        return mAdjustX;
    }
    
    public void registerScreenChangedListener(OnScreenChangedListener l) {
        mListener = l;
    }

    public void unregisterScreenChangedListener(OnScreenChangedListener l) {
        mListener = null;
    }

    public abstract interface OnScreenChangedListener {
        public abstract void onScreenChanged(boolean fullscreenChanged, boolean isFullscreen,
                                             boolean rotationChanged, boolean isPortrait,
                                             int screenWidth, int screenHeight, int statusBarHeight);
    }

}
