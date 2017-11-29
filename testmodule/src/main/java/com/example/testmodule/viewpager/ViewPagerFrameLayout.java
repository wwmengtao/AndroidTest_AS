package com.example.testmodule.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by mengtao1 on 2017/11/29.
 */

public class ViewPagerFrameLayout extends FrameLayout {

    public ViewPagerFrameLayout(@NonNull Context context) {
        super(context);
    }

    public ViewPagerFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPagerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewPagerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context,attrs,defStyleAttr,defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewPager pager = null;
        int count = getChildCount();
        for(int i=0; i<count; i++) {
            if(getChildAt(i) instanceof ViewPager) {
                pager = (ViewPager)getChildAt(i);
                break;
            }
        }
        if(pager != null) {
            return pager.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}
