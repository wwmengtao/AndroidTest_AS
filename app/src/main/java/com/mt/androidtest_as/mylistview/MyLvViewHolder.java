package com.mt.androidtest_as.mylistview;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MyLvViewHolder {
	private final SparseArray<View> mViews;
	protected View mConvertView;
	private Context mContext = null;
	protected MyLvViewHolder(Context context, ViewGroup parent, int layoutId, int position){
		mContext = context.getApplicationContext();
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
		// setTag
		mConvertView.setTag(this);
	}

	public static MyLvViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position){
		MyLvViewHolder holder = null;
		if (convertView == null){
			holder = new MyLvViewHolder(context, parent, layoutId, position);
		} else{
			holder = (MyLvViewHolder) convertView.getTag();
		}
		return holder;
	}

	public View getConvertView(){
		return mConvertView;
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if (view == null){
			view = mConvertView.findViewById(viewId);
			if(null!=view)mViews.put(viewId, view);
		}
		return (T) view;
	}
}
