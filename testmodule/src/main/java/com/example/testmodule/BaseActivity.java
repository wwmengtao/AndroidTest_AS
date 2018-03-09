package com.example.testmodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.androidcommon.alog.CrashBaseActivity;
import com.example.androidcommon.application.AppExecutors;
import com.example.androidcommon.application.CommonApp;

import butterknife.Unbinder;

/**
 * Created by mengtao1 on 2017/11/11.
 */

public abstract class BaseActivity extends CrashBaseActivity {
    protected String TAG = null;
    protected Context mContext = null;
    protected AppExecutors mAppExecutors;
    protected Unbinder mUnbinder = null;
    public static final String ACTIVITY_NAME_TAG = "ACTIVITY_NAME";
    public String activity_name = null;
    protected SparseArray<Class<?>> mActivitySA = null;
    private int mDensityDpi = -1;

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
        mAppExecutors = ((CommonApp)getApplication()).getAppExecutors();
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

    public int getDensityDpi(){
        if(-1==mDensityDpi){
            mDensityDpi = getResources().getDisplayMetrics().densityDpi;
        }
        return mDensityDpi;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        ViewGroup.LayoutParams paramsItem = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            paramsItem = listItem.getLayoutParams();
            //以下将所有ListView中每个item view高度累加起来
            if(paramsItem.height >= 0){//1、直接通过item的LayoutParams获取高度
                ALog.Log("####mParams.height: "+paramsItem.height+" i:"+i);
                totalHeight += paramsItem.height;
            }else{//2、方法1不可以的话通过MeasuredHeight获取高度
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
                ALog.Log("####itemHeight: "+listItem.getMeasuredHeight()+" i:"+i);
            }
        }
        ViewGroup.LayoutParams paramsListView = listView.getLayoutParams();
        paramsListView.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(paramsListView);
    }

}
