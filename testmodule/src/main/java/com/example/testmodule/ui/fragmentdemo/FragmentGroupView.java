package com.example.testmodule.ui.fragmentdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.testmodule.ALog;

/**
 * Created by mengtao1 on 2018/2/6.
 * 由于此时的Activity视图是自定义视图FragmentGroupView，如果FragmentGroupView继承的是ViewGroup，那么因此Activity需要重新刷新才可以
 * 看到右侧的fragment，即执行recreate才可以看到。直接集成自LinearLayout没有这样的问题。
 */

public class FragmentGroupView extends LinearLayout {
    private boolean isMeasured = false;
    private View mMenuView;
    private View mContentView;
    private int mScreenWidth;
    private int mScreenHeight;

    public FragmentGroupView(Context context) {
        this(context, null);
    }

    public FragmentGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FragmentGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //添加背景图获取屏幕宽高
    private void init(Context context) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        ALog.Log("init: "+mScreenWidth+" "+mScreenHeight);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMenuView = getChildAt(0);
        mContentView = getChildAt(1);
        mContentView.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_light));
    }

}
