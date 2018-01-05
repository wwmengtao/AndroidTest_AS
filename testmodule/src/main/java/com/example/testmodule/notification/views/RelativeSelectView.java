package com.example.testmodule.notification.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.example.testmodule.ALog;

/**
 * Created by mengtao1 on 2018/1/5.
 */

public class RelativeSelectView extends RelativeLayout{
    public RelativeSelectView(Context context) {
        super(context);
    }

    public RelativeSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        ALog.Log("onInterceptTouchEvent");
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP)ALog.Log("onTouchEvent");
        return super.onTouchEvent(event);//最终调用onClickListener
    }

}
