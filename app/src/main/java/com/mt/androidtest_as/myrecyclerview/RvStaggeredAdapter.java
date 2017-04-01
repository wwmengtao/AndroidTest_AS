package com.mt.androidtest_as.myrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.mt.androidtest_as.R;

public class RvStaggeredAdapter extends RecyclerView.Adapter<RvStaggeredAdapter.MyViewHolder>{

	private List<String> mDatas;
	private LayoutInflater mInflater;
	private List<Integer> mHeights;

	public RvStaggeredAdapter(Context context, List<String> datas){
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
		//
		mHeights = new ArrayList<>();
		for (int i = 0; i < mDatas.size(); i++){
			mHeights.add( (int) (100 + Math.random() * 300));
		}
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(
				R.layout.item_home, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position){
		ViewGroup.LayoutParams lp = holder.tv.getLayoutParams();
		lp.height = mHeights.get(position);
		holder.tv.setLayoutParams(lp);
		holder.tv.setText(mDatas.get(position));
	}

	@Override
	public int getItemCount(){
		return mDatas.size();
	}

	/**
	 * 这里更新数据集不是用notifyDataSetChanged()而是notifyItemInserted(position)与notifyItemRemoved(position),
	 * 否则没有动画效果。
	 * @param position
	 */
	public void addData(int position){
		mDatas.add(position, "Insert One");
		notifyItemInserted(position);
	}

	/**
	 * 这里更新数据集不是用notifyDataSetChanged()而是notifyItemInserted(position)与notifyItemRemoved(position),
	 * 否则没有动画效果。
	 * @param position
	 */
	public void removeData(int position){
		mDatas.remove(position);
		notifyItemRemoved(position);
	}

	class MyViewHolder extends ViewHolder{
		TextView tv;
		public MyViewHolder(View view){
			super(view);
			tv = (TextView) view.findViewById(R.id.id_num);
		}
	}
}