package com.example.framework_o;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	private boolean isLogRun = true; 
	
	@Override
	public void onAttach(Activity mActivity) {
		super.onAttach(mActivity);
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

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(isLogRun)ALog.Log("onConfigurationChanged",this);
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
	public void onTrimMemory(int level){
		super.onTrimMemory(level);
		if(isLogRun)ALog.Log("onTrimMemory level: "+level,this);
	}

    @Override
    public void onDestroyView() {
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
}
