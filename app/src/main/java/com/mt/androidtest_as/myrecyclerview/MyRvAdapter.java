package com.mt.androidtest_as.myrecyclerview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt.androidtest_as.R;
import com.mt.androidtest_as.data.BaseData;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class MyRvAdapter extends RecyclerView.Adapter<MyRvViewHolder>{
    public static final int VIEW_TYPE_EMPTY = 0x01;
    public static final int VIEW_TYPE_NOTEMPTY = 0x10;
    private Context mContext = null;
    private List<BaseData> mData = null;
    private MyDataObserver mDataObserver = null;
    private View.OnClickListener mOnClickListener = null;
    private int emptyViewBackGroundColor = -1;
    private int emptyViewTextColor = -1;

    public MyRvAdapter(Fragment mFragment){
        this.mContext = mFragment.getActivity().getApplicationContext();
        mDataObserver = new MyDataObserver();
        registerAdapterDataObserver(mDataObserver);
        mOnClickListener = (View.OnClickListener)mFragment;
    }

    public void setData(List<BaseData> mData){
        this.mData = mData;
    }

    public void setColorForEmptyView(int emptyViewBackGroundColor, int emptyViewTextColor){
        this.emptyViewBackGroundColor = emptyViewBackGroundColor;
        this.emptyViewTextColor = emptyViewTextColor;
    }

    @Override
    public MyRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate((viewType == VIEW_TYPE_EMPTY)?
                R.layout.item_empty_view:R.layout.list_item, parent, false);
        MyRvViewHolder mMyViewHolder = new MyRvViewHolder(mView, viewType);
        mView.setTag(mMyViewHolder);
        mView.setOnClickListener(mOnClickListener);
        return mMyViewHolder;
    }

    @Override
    public void onBindViewHolder(MyRvViewHolder holder, int position) {
        if(VIEW_TYPE_EMPTY == holder.viewType){
            TextView tv = (TextView)holder.mView.findViewById(R.id.empty_view);
            holder.mView.setBackgroundColor(emptyViewBackGroundColor);
            tv.setTextColor(emptyViewTextColor);
            return;
        }
        TextView mTextView = holder.mTextView;
        mTextView.setText(mData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if(null == mData || 0 == mData.size()){
            return 1;
        }
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        //在这里进行判断，如果我们的集合的长度为0时，我们就使用emptyView的布局
        if (null == mData || mData.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        //如果有数据，则使用ITEM的布局
        return VIEW_TYPE_NOTEMPTY;
    }

}
