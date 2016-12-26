package com.mt.androidtest_as.mylistview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mt.androidtest_as.ALog;
import com.mt.androidtest_as.R;
import com.mt.androidtest_as.data.BaseData;
import com.mt.androidtest_as.data.DataBank;

import java.util.List;


public class MyLvAdapter extends BaseAdapter{
	private Context mContext = null;
	private List<BaseData> mData = null;
	public MyLvAdapter(Fragment mFragment){
		this.mContext = mFragment.getActivity().getApplicationContext();
		mOnClickListener = (View.OnClickListener)mFragment;
	}
	public static final int ConvertViewTagID = R.layout.list_item;
	private View.OnClickListener mOnClickListener = null;

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
		mConvertView.setOnClickListener(mOnClickListener);
		mConvertView.setTag(ConvertViewTagID,position);
		return mConvertView;
	}

}
