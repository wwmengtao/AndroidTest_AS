package com.example.testmodule.windowmanager;

/**
 * Created by mengtao1 on 2017/12/7.
 */

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.example.testmodule.ALog;

import java.util.ArrayList;
import java.util.List;

/**
 * FloatingMeasureView：悬浮视图，用于监听窗口的旋转动作以及自身高度变化情况。
 */
public class FloatingMeasureView extends View {
    private static final String TAG = "MeasureView_";
    private static final int colorBackground = android.R.color.holo_green_light;//FloatingMeasureView的背景色
    private Context mContext = null;
    private WindowManager.LayoutParams mLayoutParams = null;//MeasureView视图的WindowManager添加参数
    private boolean screenOrientationChanged = false;
    //
    private List<MeasureViewHeightChangedListener> mvhcListeners = null;//高度变化监听器集合
    private List<ConfigurationChangedListener> cfcListeners = null;//转屏监听器集合
    //
    public FloatingMeasureView(Context context) {
        super(context);
        this.mContext = context.getApplicationContext();
        //
        if(null == mvhcListeners)mvhcListeners = new ArrayList<>();
        if(null == cfcListeners)cfcListeners = new ArrayList<>();
        //
        setFMVLayoutParams();//设置FloatingMeasureView布局参数
        setBackgroundColor(mContext.getResources().getColor(colorBackground));//设置背景色，实际使用中可以不设置
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ALog.Log(TAG + "onAttachedToWindow");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ALog.Log(TAG + "onSizeChanged w: " + w + " h: " + h + " oldw: " + oldw + " oldh: " + oldh);
        if(0 == oldh){//1、说明是第一次show出来，因此高度数值由0变成设定数值
            return;
        }
        if (screenOrientationChanged) {//2、说明MeasureView的高度变化(如果有的话)是由于转屏引起的
            screenOrientationChanged = !screenOrientationChanged;
            return;
        }
        //3、回调监听
        boolean measureViewHeightChanged = false;
        boolean measureViewHeightBecomeLarger = false;//类似于相机等应用开启后，状态栏消失的情景，此时的MeasureView的高度变大了
        if (h != oldh) {
            measureViewHeightChanged = true;
            measureViewHeightBecomeLarger = (h > oldh);
        }
        if (mvhcListeners != null && mvhcListeners.size() > 0) {
            for(MeasureViewHeightChangedListener l : mvhcListeners){
                l.onMeasureViewHeightChanged(measureViewHeightChanged, measureViewHeightBecomeLarger);
            }
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ALog.Log(TAG + "onConfigurationChanged: " + newConfig.orientation);
        screenOrientationChanged = true;//此时发生了转屏
        if (cfcListeners != null && cfcListeners.size() > 0) {
            for(ConfigurationChangedListener l : cfcListeners){
                l.onConfigurationChanged(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT);
            }
        }
    }

    /**
     * setFMVLayoutParams：设置自身的悬浮布局参数
     */
    private void setFMVLayoutParams() {
        int FLOATING_MEASURE_VIEW_WINDOW_TYPE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /**
             * Window type: Application overlay windows are displayed above all activity windows
             * (types between {@link #FIRST_APPLICATION_WINDOW} and {@link #LAST_APPLICATION_WINDOW})
             * but below critical system windows like the status bar or IME.
             * <p>
             * The system may change the position, size, or visibility of these windows at anytime
             * to reduce visual clutter to the user and also manage resources.
             * <p>
             * Requires {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW} permission.
             * <p>
             * The system will adjust the importance of processes with this window type to reduce the
             * chance of the low-memory-killer killing them.
             * <p>
             * In multi-user systems shows only on the owning user's screen.
             */
            //public static final int TYPE_APPLICATION_OVERLAY = FIRST_SYSTEM_WINDOW + 38;
            FLOATING_MEASURE_VIEW_WINDOW_TYPE = LayoutParams.TYPE_APPLICATION_OVERLAY;
            /**
             * 注意，上述Type如果改为TYPE_APPLICATION_MEDIA则点击WindowManagerActivity运行会报错：
             * java.lang.RuntimeException: Unable to start activity ComponentInfo{com.example.testmodule/com.example.testmodule.activities.ui.WindowManagerActivity}:
             * android.view.WindowManager$BadTokenException: Unable to add window -- token null is not valid; is your activity running?
             * 原因是当前的FloatingMeasureView必须依附于某一个view，而在oncreate中view还没有加载完毕，必须要等
             * activity的生命周期函数全部执行完毕，你需要依附的view加载好后才可以执行。
             * 解决方法：在使用TYPE_APPLICATION_MEDIA的情况下，可在WindowManagerActivity的onCreate函数中使用mFloatingMeasureView.post
             * 方式添加FloatingMeasureView，这样可以保证在Activity视图加载完毕后添加。
             *
             */
        } else {
            FLOATING_MEASURE_VIEW_WINDOW_TYPE = LayoutParams.TYPE_PHONE;
        }
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.type = FLOATING_MEASURE_VIEW_WINDOW_TYPE;
        mLayoutParams.gravity = Gravity.END | Gravity.TOP;
        mLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = 8;//实际应用中可以设置宽度为0，这样用户就看不到这个视图了
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.format = PixelFormat.TRANSPARENT;
        //
        setLayoutParams(mLayoutParams);//设置悬浮窗布局参数
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mvhcListeners != null && mvhcListeners.size() > 0) {
            mvhcListeners.clear();
            mvhcListeners = null;
        }

        if (cfcListeners != null && cfcListeners.size() > 0) {
            cfcListeners.clear();
            cfcListeners = null;
        }
    }


    public interface MeasureViewHeightChangedListener {//监听MeasureView高度数值变化，此变化并非由于转屏引起的
        void onMeasureViewHeightChanged(boolean heightChanged, boolean heightBecomeLarger);
    }

    public synchronized void registerMeasureViewHeightChangedListener(MeasureViewHeightChangedListener l) {
        if (mvhcListeners != null) {
            mvhcListeners.add(l);
        }
    }

    public synchronized void unregisterMeasureViewHeightChangedListener(MeasureViewHeightChangedListener l) {
        if (mvhcListeners != null && mvhcListeners.size() > 0) {
            mvhcListeners.remove(l);
        }
    }

    public interface ConfigurationChangedListener {//用于监听屏幕转屏后的方向
        void onConfigurationChanged(boolean isPortrait);
    }

    public synchronized void registerConfigurationChangedListener(ConfigurationChangedListener l) {
        if (cfcListeners != null) {
            cfcListeners.add(l);
        }
    }

    public synchronized void unregisterConfigurationChangedListener(ConfigurationChangedListener l) {
        if (cfcListeners != null && cfcListeners.size() > 0) {
            cfcListeners.remove(l);
        }
    }
}
