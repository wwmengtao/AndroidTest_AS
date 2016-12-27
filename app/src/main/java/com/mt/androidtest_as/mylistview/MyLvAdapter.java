package com.mt.androidtest_as.mylistview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.data.BaseData;

import java.util.List;


public class MyLvAdapter extends BaseAdapter{
	private Context mContext = null;
	private List<BaseData> mData = null;
	public MyLvAdapter(Fragment mFragment){
		this.mContext = mFragment.getActivity().getApplicationContext();
	}

	public void setData(List<BaseData> mData){
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
		MyLvViewHolder mMyViewHolder = MyLvViewHolder.get(mContext, convertView, parent, R.layout.list_item, position);
		TextView mTextView = mMyViewHolder.getView(R.id.tv);
		mTextView.setText(mData.get(position).getTitle());
		View mConvertView = mMyViewHolder.getConvertView();
		return mConvertView;
	}

}
