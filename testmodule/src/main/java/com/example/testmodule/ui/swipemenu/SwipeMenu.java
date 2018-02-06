package com.example.testmodule.ui.swipemenu;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

import com.example.testmodule.ALog;

/**
 * Created by mengtao1 on 2018/2/6.
 */

public class SwipeMenu extends ViewGroup {
    private boolean isMeasured = false;
    private View mMenuView;
    private View mContentView;
    private Scroller mScroller;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mMenuOffset;
    //
    private boolean isMenuShowing = false; //是否已经显示了菜单

    public SwipeMenu(Context context) {
        this(context, null);
    }

    public SwipeMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //添加背景图获取屏幕宽高
    private void init(Context context) {
        mScroller = new Scroller(context);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mMenuOffset = mScreenWidth/9;
        ALog.Log("init: "+mScreenWidth+" "+mScreenHeight+" "+mMenuOffset);
    }

    //是否显示菜单
    public boolean isMenuShowing() {
        if (getScrollX() <= 0) {
            isMenuShowing = true;
        } else {
            isMenuShowing = false;
        }
        return isMenuShowing;
    }

    //show left part
    public void showMenu() {
        mScroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0);
        invalidate();
    }

    //hide left part
    public void hideMenu() {
        mScroller.startScroll(getScrollX(), 0, mScreenWidth - mMenuOffset - getScrollX(), 0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            dealScroll();
            postInvalidate();
        }
        super.computeScroll();
    }

    private void dealScroll() {

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isMeasured) {
            mMenuView = getChildAt(0);
            mContentView = getChildAt(1);
            mMenuView.getLayoutParams().width = mScreenWidth - mMenuOffset;
            mMenuView.getLayoutParams().height = mScreenHeight;
            mContentView.getLayoutParams().width = mScreenWidth;
            mContentView.getLayoutParams().height = mScreenHeight;
            measureChild(mMenuView, widthMeasureSpec, heightMeasureSpec);
            measureChild(mContentView, widthMeasureSpec, heightMeasureSpec);
            isMeasured = true;
        }
        setMeasuredDimension(mScreenWidth * 2 - mMenuOffset, mScreenHeight);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        ALog.Log("onLayout: i "+i+" i1: "+i1+" i2: "+i2+" i3: "+i3+" mScreenWidth: "+mScreenWidth);
        if (b) {
            ALog.Log("onLayout2: i "+i+" i1: "+i1+" i2: "+i2+" i3: "+i3+" mScreenWidth: "+mScreenWidth);
            mContentView.setClickable(true);
            mMenuView.setClickable(true);
            mMenuView.setBackgroundColor(Color.TRANSPARENT);
            mMenuView.layout(0, 0, mScreenWidth - mMenuOffset, mScreenHeight);
            mContentView.layout(mScreenWidth - mMenuOffset, 0, mScreenWidth - mMenuOffset + mScreenWidth, mScreenHeight);
//            scrollTo(mScreenWidth - mMenuOffset, 0);
        }
    }
}
