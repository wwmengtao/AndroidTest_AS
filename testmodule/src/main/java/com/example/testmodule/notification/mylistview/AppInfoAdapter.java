package com.example.testmodule.notification.mylistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.testmodule.R;
import com.example.testmodule.notification.appinfos.AppInfo;

import java.util.List;


public class AppInfoAdapter extends BaseAdapter{
	private Context mContext = null;
	private List<AppInfo> mData = null;
	public AppInfoAdapter(Context mContext){
		this.mContext = mContext;
	}

	public void setData(List<AppInfo> mData){
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppInfoHolder mMyViewHolder = AppInfoHolder.get(mContext, convertView, parent, R.layout.list_item, position);
		ImageView mImageView = mMyViewHolder.getView(R.id.iv);
		mImageView.setImageDrawable(mData.get(position).icon);
		View mConvertView = mMyViewHolder.getConvertView();
		return mConvertView;
	}

}
