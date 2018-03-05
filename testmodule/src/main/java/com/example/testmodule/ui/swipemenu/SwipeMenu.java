package com.example.testmodule.ui.swipemenu;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.example.testmodule.ALog;


/**
 * Created by mengtao1 on 2018/2/7.
 */

public class SwipeMenu extends RelativeLayout {
    private boolean isMeasured = false;
    private View mMenuView;
    private View mContentView;
    private Scroller mScroller;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mMenuOffset;
    private float xLast = 0;
    private float yLast = 0;
    private int mDragWipeOffset = 0; //侧边拖动的偏移值
    private float xIntercept = 0;
    private float yIntercept = 0;
    private float mStartScale = 0.95f; //起始缩放值

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

    private void init(Context context) {
        mScroller = new Scroller(context);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mMenuOffset = mScreenWidth/8;
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        boolean intercept = false;
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float xDelta = x - xIntercept;
                float yDelta = y - yIntercept;
                if (mDragWipeOffset == 0 && Math.abs(xDelta) > 20) { //全屏滑动
                    intercept = true;
                    break;
                }
                if (!isMenuShowing()) {
                    if (x >= SizeUtil.Dp2Px(getContext(), mDragWipeOffset)) {
                        return false;
                    }
                }
                if (x + getScrollX() < mScreenWidth + mDragWipeOffset) {

                    if (Math.abs(xDelta) > Math.abs(yDelta) && Math.abs(xDelta) > 20) { //X滑动主导
                        intercept = true;
                    } else {
                        intercept = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        xLast = x;
        yLast = y;
        xIntercept = x;
        yIntercept = y;
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float xDelta = x - xLast;
                float offset = xDelta;
                touchMove_deal(offset);
                break;
            case MotionEvent.ACTION_UP:
                touchUp_deal();
                break;
        }
        xLast = x;
        yLast = y;
        return false;
    }

    //滑动处理
    private void touchMove_deal(float offset) {
        ALog.Log("touchMove_deal: "+offset);
        if (getScrollX() - offset <= 0) {
            offset = 0;
        } else if (getScrollX() + mScreenWidth - offset > mScreenWidth * 2 - mMenuOffset) {
            offset = 0;
        }
        scrollBy((int) (-offset), 0); //跟随拖动
        dealScroll();
    }


    //
    private void touchUp_deal() {
        if (getScrollX() < (mScreenWidth - mMenuOffset) / 2) {
            showMenu();
        } else {
            hideMenu();
        }
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
        float progress = getScrollX() * 1.0f / (mScreenWidth - mMenuOffset);
        float scaleValue = progress * (1 - mStartScale) + mStartScale;
        ALog.Log("scaleValue: "+scaleValue);
        mContentView.setScaleX(scaleValue);
        mContentView.setScaleY(scaleValue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentMeasureSpecWidth = MeasureSpec.makeMeasureSpec(mScreenWidth * 2 - mMenuOffset, MeasureSpec.EXACTLY);
        int parentMeasureSpecHeight = MeasureSpec.makeMeasureSpec(mScreenHeight, MeasureSpec.EXACTLY);
        super.onMeasure(parentMeasureSpecWidth, parentMeasureSpecHeight);
        if (!isMeasured) {
            mMenuView = getChildAt(0);
            mContentView = getChildAt(1);
            mMenuView.getLayoutParams().width = mScreenWidth * 2 - mMenuOffset;
            mMenuView.getLayoutParams().height = mScreenHeight;
            mContentView.getLayoutParams().width = mScreenWidth;
            mContentView.getLayoutParams().height = mScreenHeight;
            measureChild(mMenuView, parentMeasureSpecWidth, parentMeasureSpecHeight);
            measureChild(mContentView, parentMeasureSpecWidth, parentMeasureSpecHeight);
            isMeasured = true;
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        super.onLayout(b, i, i1, i2, i3);
        if (b) {
            mContentView.setClickable(true);
            mMenuView.setClickable(true);
            mMenuView.setBackgroundColor(Color.TRANSPARENT);
            mMenuView.layout(0, 0, mScreenWidth * 2 - mMenuOffset, mScreenHeight);
            mContentView.layout(mScreenWidth - mMenuOffset, 0, mScreenWidth - mMenuOffset + mScreenWidth, mScreenHeight);
            scrollTo(mScreenWidth - mMenuOffset, 0);
        }
    }
}
