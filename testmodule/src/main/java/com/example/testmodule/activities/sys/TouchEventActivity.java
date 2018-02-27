package com.example.testmodule.activities.sys;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.example.testmodule.BaseActivity;
import com.example.testmodule.R;
import com.example.testmodule.touchevent.EventReturn;

import static com.example.testmodule.touchevent.EventInfo.ACTION_LOG;
import static com.example.testmodule.touchevent.EventInfo.DES_ACTION_ONCLICK;
import static com.example.testmodule.touchevent.EventInfo.dispatchTouchEvent;
import static com.example.testmodule.touchevent.EventInfo.getReturnResult;
import static com.example.testmodule.touchevent.EventInfo.onClick;
import static com.example.testmodule.touchevent.EventInfo.onTouch;
import static com.example.testmodule.touchevent.EventInfo.onTouchEvent;
import static com.example.testmodule.touchevent.EventInfo.setDefaultReturnResult;
import static com.example.testmodule.touchevent.EventInfo.setReturnResult;

/**
 * 触摸事件的分发会经过这么几个顺序，dispatchTouchEvent --> onInterceptTouchEvent --> onTouchEvent，
 * 事件拦截就在onInterceptTouchEvent方法中进行，在该方法中返回true即代表拦截触摸事件。
 * 触摸事件的分发是一个典型的隧道事件，即从上到下的过程。
 * @author Mengtao1
 *
 */
public class TouchEventActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener{

    private static final String strLayout = "0.TouchEventActivity";
    private SparseArray<Integer> dispatchTouchEventArrays = null;
    private SparseArray<Integer> onTouchEventArrays = null;
    private SparseArray<Integer> onTouchArrays = null;
    private EventReturn.TouchEventActivityERA mET = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event);
        //
        getWindow().getDecorView().setOnTouchListener(this);
        //getWindow().getDecorView().setOnClickListener(this);
        //为各个View的事件处理结果赋新值
        mET =  new EventReturn.TouchEventActivityERA();
        //为TouchEventActivity的各个数组赋值
        dispatchTouchEventArrays = mET.getDispatchTouchEventArrays();
        onTouchArrays                    = mET.getOnTouchArrays();
        onTouchEventArrays           = mET.getOnTouchEventArrays();
    }

    /**
     * dispatchTouchEvent：此函数为TouchEventActivity的对应处理函数
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(!setReturnResult(event, strLayout, dispatchTouchEvent, dispatchTouchEventArrays)){
            setDefaultReturnResult(super.dispatchTouchEvent(event));
        }
        return getReturnResult(event,strLayout, dispatchTouchEvent);
    }

    /**
     * onTouch：此函数为DecorView的对应处理函数
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //ALog.fillInStackTrace("DecorView_onTouch");
        String mStrLayout = "0.DecorView";
        if(!setReturnResult(event, mStrLayout, onTouch, onTouchArrays)){
            setDefaultReturnResult(false);
        }
        return getReturnResult(event, mStrLayout, onTouch);
    }

    /**
     * onTouchEvent：此函数为TouchEventActivity的对应处理函数
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //ALog.fillInStackTrace("TouchEventActivity_onTouchEvent");
        if(!setReturnResult(event, strLayout, onTouchEvent, onTouchEventArrays)){
            setDefaultReturnResult(super.onTouchEvent(event));
        }
        return getReturnResult(event, strLayout, onTouchEvent);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        ACTION_LOG(strLayout, onClick, DES_ACTION_ONCLICK);
    }
}