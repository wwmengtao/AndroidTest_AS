package com.mt.androidtest_as.mylistview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.data.BaseData;
import com.mt.androidtest_as.data.DataBank;

import java.util.List;


public class MyListViewAdapter extends BaseAdapter implements View.OnClickListener{
	private Context mContext = null;
	private List<BaseData> mData = null;
	public MyListViewAdapter(Activity mActivity){
		mContext = mActivity.getApplicationContext();
	}
	private int ConvertViewTagID = R.layout.list_item;
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
		MyViewHolder mMyViewHolder = MyViewHolder.get(mContext, convertView, parent, R.layout.list_item, position);
		TextView mTextView = mMyViewHolder.getView(R.id.tv);
		mTextView.setText(mData.get(position).getTitle());
		View mConvertView = mMyViewHolder.getConvertView();
		mConvertView.setOnClickListener(this);
		mConvertView.setTag(ConvertViewTagID,position);
		return mConvertView;
	}

	@Override
	public void onClick(View v) {
		int position = (int)v.getTag(ConvertViewTagID);
		DataBank.get(mContext).delData(mData.get(position));
		mData = DataBank.get(mContext).getData();
		setData(mData);
		notifyDataSetChanged();
	}
}
