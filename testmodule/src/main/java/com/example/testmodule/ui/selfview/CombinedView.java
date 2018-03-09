package com.example.testmodule.ui.selfview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.testmodule.ALog;
import com.example.testmodule.R;


public class CombinedView extends FrameLayout {

	private Button leftButton;

    public CombinedView(Context context) {
        this(context,null);
    }	
	
    public void onAttachedToWindow() {
    	super.onAttachedToWindow();
    	ALog.Log("CombinedView_onAttachedToWindow");
    }
    
    public void onDetachedFromWindow() {
    	super.onDetachedFromWindow();
    	ALog.Log("CombinedView_onDetachedFromWindow");
    }
    
	public CombinedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.customview_title, this);
		leftButton = (Button) findViewById(R.id.button_left);
		leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity) getContext()).finish();
			}
		});
	}

}