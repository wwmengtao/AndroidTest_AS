package com.mt.androidtest_as.myrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.mt.androidtest_as.R;

public class RvStaggeredAnimatorAdapter extends RecyclerView.Adapter<RvStaggeredAnimatorAdapter.MyViewHolder> implements
View.OnClickListener{
	private RecyclerView mRecyclerView;
	private List<String> mDatas;
	private LayoutInflater mInflater;
	private List<Integer> mHeights;
	private View.OnClickListener mOnClickListener = null;

	public RvStaggeredAnimatorAdapter(Context mContext, List<String> datas){
		mInflater = LayoutInflater.from(mContext);
		mOnClickListener = this;
		mDatas = datas;
		//
		mHeights = new ArrayList<>();
		for (int i = 0; i < mDatas.size(); i++){
			mHeights.add( (int) (100 + Math.random() * 300));
		}
	}

	public void setRecyclerView(RecyclerView rv) {
		mRecyclerView = rv;
		mItemTouchHelper.attachToRecyclerView(rv);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		View mView = mInflater.inflate(R.layout.item_home, parent, false);
		MyViewHolder holder = new MyViewHolder(mView);
		mView.setTag(holder);
		mView.setOnClickListener(mOnClickListener);
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
//		notifyDataSetChanged();//没有动画效果
	}

	/**
	 * 这里更新数据集不是用notifyDataSetChanged()而是notifyItemInserted(position)与notifyItemRemoved(position),
	 * 否则没有动画效果。
	 * @param position
	 */
	public void removeData(int position){
		mDatas.remove(position);
		notifyItemRemoved(position);
//		notifyDataSetChanged();//没有动画效果
	}

	@Override
	public void onClick(View v) {
		MyViewHolder holder = (MyViewHolder)v.getTag();
		int position = holder.getAdapterPosition();
		mDatas.set(position, mDatas.get(position)+"#");
		notifyItemChanged(position);//这样有动画效果
//		notifyDataSetChanged();//没有动画效果
	}

	class MyViewHolder extends ViewHolder{
		TextView tv;
		public MyViewHolder(View view){
			super(view);
			tv = (TextView) view.findViewById(R.id.id_num);
		}
	}

	/**
	 * mItemTouchHelper:实现item的交换效果
	 */
	private ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
			ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0 /* no swipe */) {

		@Override
		public boolean onMove(RecyclerView view, RecyclerView.ViewHolder source,
				RecyclerView.ViewHolder target) {
			onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
			return true;
		}

		@Override
		public void onSwiped(ViewHolder viewHolder, int direction) {

		}
	});

	private void onItemMove(int fromPosition, int toPosition) {
		if (fromPosition >= 0 && toPosition >= 0) {
			final String saved = mDatas.get(fromPosition);
			mDatas.remove(fromPosition);
			mDatas.add(toPosition, saved);
		}
		notifyItemChanged(fromPosition);
		notifyItemChanged(toPosition);
		notifyItemMoved(fromPosition, toPosition);
	}

}