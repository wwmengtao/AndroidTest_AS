package com.example.testmodule;

import android.content.Context;

import com.example.androidcommon.alog.ALog;
import com.example.androidcommon.alog.ALogFragment;
import com.example.androidcommon.application.AppExecutors;
import com.example.testmodule.application.BasicApp;

import butterknife.Unbinder;

/**
 * Created by mengtao1 on 2018/1/24.
 */

public abstract class BaseFragment2 extends ALogFragment {
    protected String TAG = null;
    protected Context mContext = null;
    protected BasicApp mApplication = null;
    protected AppExecutors mAppExecutors = null;
    protected Unbinder mUnbinder = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ALog.Log("onAttach",this);
        this.TAG = getClass().getSimpleName();
        this.mContext = context.getApplicationContext();
        this.mApplication = (BasicApp)getActivity().getApplication();
        this.mAppExecutors = mApplication.getAppExecutors();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(null != mUnbinder)mUnbinder.unbind();
    }
}
