package com.example.testmodule.notification.mylistview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testmodule.R;
import com.example.testmodule.notification.model.AppInfo;
import com.example.testmodule.notification.notifiutils.MockNotifyBlockManager;

import java.util.ArrayList;
import java.util.List;


public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoHolder>{
	private Context mContext = null;
	private List<AppInfo> mData = null;
	private int whiteListAppInfoCount = -1;
	private OnItemViewClickListener mOnItemClickListener = null;
	private OnItemViewLongClickListener mOnItemViewLongClickListener = null;
	private LayoutType mLayoutType = LayoutType.LinearLayoutManager;
	private AppInfo mAppInfoAdd = null;//the last one "add" item

	public AppInfoAdapter(Context mContext){
		this.mContext = mContext;
		this.whiteListAppInfoCount = MockNotifyBlockManager.get(mContext).getAppsInfo(MockNotifyBlockManager.APP_TYPE.FLAG_WHITE_LIST).size();
		this.mData = new ArrayList<>();
		this.mAppInfoAdd = new AppInfo();
		this.mAppInfoAdd.setIcon(mContext.getResources().getDrawable(R.drawable.addapp, null));
	}

	public void setData(List<AppInfo> data){
		if(null != data){
			mData = data;
		}else {
			mData = new ArrayList<>();
		}
	}

	public void setBlocked(int index){
		if(null == mData || 0 == mData.size())return;
		AppInfo ai = mData.get(index);
		mData.get(index).setNotiBlocked(!ai.notiBlocked);
		if(LayoutType.GridLayoutManager == mLayoutType){
			notifyItemChanged(index);//just show selected state
		}
	}

	public void setOnItemViewClickListener(OnItemViewClickListener listener){
		this.mOnItemClickListener = listener;
	}

	public void setOnItemViewLongClickListener(OnItemViewLongClickListener listener){
		this.mOnItemViewLongClickListener = listener;
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
		final AppInfo data;
		if(position == mData.size() && mData.size() != whiteListAppInfoCount){
			data = mAppInfoAdd;
		}else{
			data = mData.get(position);
		}
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
		if(LayoutType.LinearLayoutManager == mLayoutType){
			if(null == mOnItemViewLongClickListener){
				throw new RuntimeException("must provide not null OnItemViewLongClickListener!");
			}
			rootView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					mOnItemViewLongClickListener.onItemViewLongClick(position);
					return false;
				}
			});
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		if(mLayoutType == LayoutType.GridLayoutManager || mData.size() == whiteListAppInfoCount){
			return mData.size();
		}
		return mData.size() + 1;
	}

	public interface OnItemViewClickListener{
		void onItemViewClick(int position, Intent intent);
	}

	public interface OnItemViewLongClickListener{
		void onItemViewLongClick(int position);
	}
}
