package com.example.testmodule.ui.selfview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.example.testmodule.ALog;


public class SelfDrawnView extends View implements OnClickListener {

    private Paint mPaint;
    private Rect mBounds;
    private int mCount;
    private String text = "S/D click!";
    long mLastTime=0;
    long mCurTime=0;
    public SelfDrawnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        mPaint.setColor(Color.YELLOW);
        mPaint.setTextSize(50);
        mPaint.getTextBounds(text, 0, text.length(), mBounds);
        float textWidth = mBounds.width();
        float textHeight = mBounds.height();
        canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2
                + textHeight / 2, mPaint);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        //以下通过TouchDelegate扩大点击区域。如果此扩展区域覆盖其他控件的Touch事件消费区域，那么此时点击
        //这样的Touch事件消费区域不会响应onClick事件
        Rect delegateArea = new Rect();
        getHitRect(delegateArea);
        delegateArea.left -= 1000;
        delegateArea.right += 1000;
        delegateArea.bottom += 2000;
        TouchDelegate touchDelegate = new TouchDelegate(delegateArea,this);
        ((LinearLayout) getParent()).setTouchDelegate(touchDelegate);
    }

    @Override
    public void onClick(View v) {
        ALog.Log("onClick");
        mLastTime=mCurTime;
        mCurTime= System.currentTimeMillis();
        if(mCurTime-mLastTime<300){//双击事件
            mCurTime =0;
            mLastTime = 0;
            removeCallbacks(mSingleClickRunnable);
            post(mDoubleClickRunnable);
        }else{//单击事件
            postDelayed(mSingleClickRunnable,310);
        }
        invalidate();
    }

    /**
     * 自定义View中尽量不要使用Handler，使用post即可
     */
    private Runnable mSingleClickRunnable = new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            ALog.Log("这是单击事件");
            mCount++;
            text = String.valueOf(mCount);
        }
    };

    private Runnable mDoubleClickRunnable = new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            ALog.Log("这是双击事件");
            text = "doubleClick";
        }
    };
}  
