package com.example.testmodule;

import android.content.Context;

import com.example.androidcommon.alog.ALog;
import com.example.androidcommon.alog.ALogFragment;
import com.example.testmodule.application.AppExecutors;
import com.example.testmodule.application.BasicApp;

import butterknife.Unbinder;

/**
 * Created by mengtao1 on 2018/1/24.
 */

public abstract class BaseFragment2 extends ALogFragment {
    protected String TAG = null;
    protected Context mContext = null;
    protected AppExecutors mAppExecutors;
    protected Unbinder mUnbinder = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ALog.Log("onAttach",this);
        this.mContext = context.getApplicationContext();
        this.mAppExecutors = ((BasicApp)getActivity().getApplication()).getAppExecutors();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(null != mUnbinder)mUnbinder.unbind();
    }
}
