package com.example.androidcommon.alog;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ALogFragment：用于展示Fragment的生命周期
 onAttach方法：Fragment和Activity建立关联的时候调用，在这个方法中可以获得所在的activity。
 onCreateView方法：为Fragment加载布局时调用，fragment在其中创建自己的layout(界面)。
 onActivityCreated方法：当Activity中的onCreate方法执行完后调用。
 onDestroyView方法：Fragment中的布局被移除时调用。
 onDetach方法：Fragment和Activity解除关联的时候调用，此时getActivity()返回null。
 *
 */
public abstract class ALogFragment extends Fragment {
	private boolean isLogRun = true; 
	private Handler mHandler = null;

	protected Handler getHandler(){
		if(null == mHandler){
			mHandler = new Handler();
		}
		return mHandler;
	}

	protected String getString(String strName){
		int strID = getResources().getIdentifier(strName, "string" ,getActivity().getPackageName());
		return getResources().getString(strID);
	}

	protected int getPicID(String picName){
		int strID = getResources().getIdentifier(picName, "drawable" ,getActivity().getPackageName());
		return strID;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if(isLogRun)ALog.Log("onAttach",this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isLogRun)ALog.Log("onCreate",this);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	if(isLogRun)ALog.Log("onCreateView",this);
        return null;
    }

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view,savedInstanceState);
		if(isLogRun)ALog.Log("onViewCreated",this);
	}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	if(isLogRun)ALog.Log("onActivityCreated",this);
    }

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if(isLogRun)ALog.Log("onViewStateRestored",this);
	}


	@Override
	public void onStart() {
        super.onStart();
		if(isLogRun)ALog.Log("onStart",this);		
    }
	
    
	@Override
	public void onResume(){
		super.onResume();
		if(isLogRun)ALog.Log("onResume",this);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if(isLogRun)ALog.Log("onSaveInstanceState",this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(isLogRun)ALog.Log("onConfigurationChanged",this);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(isLogRun)ALog.Log("onHiddenChanged_hidden: "+hidden,this);
	}

	@Override
	public void onPause(){
		super.onPause();
		if(isLogRun)ALog.Log("onPause",this);
	}

    @Override
	public void onStop() {
        super.onStop();
		if(isLogRun)ALog.Log("onStop",this);		
    }

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if(isLogRun)ALog.Log("onLowMemory",this);
	}

    @Override
    public void onDestroyView() {
		if(null != mHandler)mHandler.removeCallbacksAndMessages(null);
    	super.onDestroyView();
    	if(isLogRun)ALog.Log("onDestroyView",this);	
    }
    
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(isLogRun)ALog.Log("onDestroy",this);
	}	
	
	@Override
	public void onDetach() {
		super.onDetach();
		if(isLogRun)ALog.Log("onDetach",this);
	}

	private List<String> mActivitiesName = null;
	public String getActivityName(String str){
		if(null == mActivitiesName){
			mActivitiesName = getActivitiesName(getContext());
		}
		if(null == mActivitiesName)return null;
		if(null==str||null==mActivitiesName)return null;
		for(String mStr : mActivitiesName){
			if(mStr.endsWith(str)){
				return mStr;
			}
		}
		return null;
	}

	/**
	 * getActivities：获取当前应用AndroidManifest.xml文件中所有<activity>节点信息
	 * @param mContext
	 */
	public List<String> getActivitiesName(Context mContext) {
		ActivityInfo[] activities=null;
		try {
			activities = mContext.getPackageManager().getPackageInfo(getActivity().getPackageName(),PackageManager.GET_ACTIVITIES).activities;
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(null==activities)return null;
		List<String> mActivitiesName = new ArrayList<String>();
		ActivityInfo mActivityInfo=null;
		for (int i=0;i<activities.length;i++) {
			mActivityInfo=activities[i];
			if(null!=mActivityInfo){
				mActivitiesName.add(mActivityInfo.name);
			}
		}
		return mActivitiesName;
	}
}
