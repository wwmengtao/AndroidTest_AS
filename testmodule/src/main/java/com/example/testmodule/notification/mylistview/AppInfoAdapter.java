package com.example.testmodule.notification.mylistview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testmodule.R;
import com.example.testmodule.notification.model.AppInfo;

import java.util.List;


public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoHolder>{
	private Context mContext = null;
	private List<AppInfo> mData = null;
	private OnItemViewClickListener mOnItemClickListener = null;
	private LayoutType mLayoutType = LayoutType.LinearLayoutManager;

	public AppInfoAdapter(Context mContext){
		this.mContext = mContext;
	}

	public void setData(List<AppInfo> mData){
		this.mData = mData;
		//
		if(mLayoutType != LayoutType.LinearLayoutManager){
			return;
		}
		AppInfo info = new AppInfo();
		info.setIcon(mContext.getResources().getDrawable(R.drawable.addapp, null));
		this.mData.add(info);
	}

	public void setBlocked(int index){
		AppInfo ai = mData.get(index);
		mData.get(index).setNotiBlocked(!ai.notiBlocked);
		notifyItemChanged(index);
	}

	public void setOnItemViewClickListener(OnItemViewClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}

	public void setLayoutType(LayoutType mLayoutType){
		this.mLayoutType = mLayoutType;
	}

	public enum LayoutType{
		GridLayoutManager,
		LinearLayoutManager
	}

	@Override
	public AppInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View mView = null;
		if(LayoutType.LinearLayoutManager == mLayoutType){
			mView = LayoutInflater.from(mContext).inflate(R.layout.list_item_rv_linear, parent, false);
		}else if(LayoutType.GridLayoutManager == mLayoutType){
			mView = LayoutInflater.from(mContext).inflate(R.layout.list_item_rv_grid, parent, false);
		}
		AppInfoHolder holder = new AppInfoHolder(mView);
		return holder;
	}

	@Override
	public void onBindViewHolder(AppInfoHolder holder, final int position) {
		final AppInfo data = mData.get(position);
		holder.bindData(data);
		View rootView = holder.getRootView();
		if(null == mOnItemClickListener){
			throw new RuntimeException("must provide not null OnItemClickListener!");
		}
		rootView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mOnItemClickListener.onItemViewClick(position, data.launchIntent);
			}
		});
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	public interface OnItemViewClickListener{
		void onItemViewClick(int position, Intent intent);
	}
}
