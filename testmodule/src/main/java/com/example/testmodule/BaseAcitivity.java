package com.example.testmodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import com.example.androidcommon.alog.CrashBaseActivity;
import com.example.testmodule.application.AppExecutors;
import com.example.testmodule.application.BasicApp;
import com.fernandocejas.android10.sample.data.ALog;

import butterknife.Unbinder;

/**
 * Created by mengtao1 on 2017/11/11.
 */

public abstract class BaseAcitivity extends CrashBaseActivity {
    protected String TAG = null;
    protected static final boolean SYSTRACE = false;
    protected static final boolean TRACEVIEW = false;
    protected Context mContext = null;
    protected AppExecutors mAppExecutors;
    protected Unbinder mUnbinder = null;
    public static final String ACTIVITY_NAME_TAG = "ACTIVITY_NAME";
    public String activity_name = null;
    protected SparseArray<Class<?>> mActivitySA = null;

    /**
     * getCallingIntent：获取Activity的开启intent同时获取Activity的名称。
     * @param context
     * @param activity
     * @return
     */
    public static Intent getCallingIntent(Context context, Class<?> activity){
        String activityName1 = activity.getName();//getName获取的是com.example.testmodule.SQLiteActivity之类的名称
        String activityName = activity.getSimpleName();//getSimpleName获取的是SQLiteActivity之类的名称
        //
        Intent intent = new Intent(context, activity);
        intent.putExtra(ACTIVITY_NAME_TAG, activityName);
        return intent;
    }

    public void setActivityTitle(){
        activity_name = getIntent().getStringExtra(ACTIVITY_NAME_TAG);
        ALog.Log1("activity_name: "+activity_name);
        if(activity_name != null){
            setTitle(activity_name);
            TAG = activity_name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mAppExecutors = ((BasicApp)getApplication()).getAppExecutors();
        setActivityTitle();
    }

    protected void initActivities(int []buttonIDs, Class<?>[] classEs){
        if(null == buttonIDs || buttonIDs.length == 0 || null == classEs || classEs.length == 0)return;
        mActivitySA = new SparseArray<>();
        for(int i = 0; i<buttonIDs.length; i++){
            mActivitySA.put(buttonIDs[i], classEs[i]);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(null != mUnbinder)mUnbinder.unbind();
    }
}
