package com.example.testmodule.touchevent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.testmodule.ALog;

import static com.example.testmodule.touchevent.EventInfo.ACTION_LOG;
import static com.example.testmodule.touchevent.EventInfo.DES_ACTION_ONCLICK;
import static com.example.testmodule.touchevent.EventInfo.dispatchTouchEvent;
import static com.example.testmodule.touchevent.EventInfo.formatStr;
import static com.example.testmodule.touchevent.EventInfo.getReturnResult;
import static com.example.testmodule.touchevent.EventInfo.onClick;
import static com.example.testmodule.touchevent.EventInfo.onTouch;
import static com.example.testmodule.touchevent.EventInfo.onTouchEvent;
import static com.example.testmodule.touchevent.EventInfo.setDefaultReturnResult;
import static com.example.testmodule.touchevent.EventInfo.setReturnResult;
import static com.example.testmodule.touchevent.EventReturn.MyTextViewERA;

/**
 * TextView.isClickable()为false，除非setOnClickListener。如果setOnTouchListener则可以响应onTouch函数
 * @author Mengtao1
 *
 */
@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView implements View.OnClickListener, View.OnTouchListener{

	private String strLayout = "3.MyTextView";
	private SparseArray<Integer>dispatchTouchEventArrays = null;
	private SparseArray<Integer>onTouchEventArrays = null;
	private SparseArray<Integer>onTouchArrays = null;
	private MyTextViewERA mEM = null;

	@RequiresApi(api = Build.VERSION_CODES.M)
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		ALog.Log("1_"+String.format(formatStr,strLayout)+" isEnabled:"+isEnabled()+" isClickable:"+isClickable()+" isLongClickable:"+isLongClickable()+" isContextClickable:"+isContextClickable());
		setOnTouchListener(this);//注册OnTouchListener可以响应onTouch函数
		//setOnClickListener(this);//注册OnClickListener可以响应onClick函数
		ALog.Log("2_"+String.format(formatStr,strLayout)+" isEnabled:"+isEnabled()+" isClickable:"+isClickable()+" isLongClickable:"+isLongClickable()+" isContextClickable:"+isContextClickable());
		//
		mEM = new MyTextViewERA();
		dispatchTouchEventArrays = mEM.getDispatchTouchEventArrays();
		onTouchArrays                    = mEM.getOnTouchArrays();
		onTouchEventArrays           = mEM.getOnTouchEventArrays();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if(!setReturnResult(event, strLayout, dispatchTouchEvent, dispatchTouchEventArrays)){
			setDefaultReturnResult(super.dispatchTouchEvent(event));
		}
		return getReturnResult(event, strLayout, dispatchTouchEvent);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(!setReturnResult(event, strLayout, onTouch, onTouchArrays)){
			setDefaultReturnResult(false);
		}
		return getReturnResult(event, strLayout, onTouch);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
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
