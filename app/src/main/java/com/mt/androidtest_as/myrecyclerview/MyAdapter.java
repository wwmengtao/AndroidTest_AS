package com.mt.androidtest_as.myrecyclerview;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt.androidtest_as.ALog;
import com.mt.androidtest_as.R;
import com.mt.androidtest_as.data.BaseData;
import com.mt.androidtest_as.data.DataBank;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener{
    private static final int VIEW_TYPE_EMPTY = 0x01;
    private static final int VIEW_TYPE_NOTEMPTY = 0x10;
    private Activity mActivity = null;
    private List<BaseData> mData = null;
    private MyDataObserver mDataObserver = null;
    public MyAdapter(Activity mActivity){
        this.mActivity = mActivity;
        mDataObserver = new MyDataObserver();
        registerAdapterDataObserver(mDataObserver);
    }

    public void setData(List<BaseData> mData){
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mActivity).inflate((viewType == VIEW_TYPE_EMPTY)?
                R.layout.item_empty_view:R.layout.list_item, parent, false);
        MyViewHolder mMyViewHolder = new MyViewHolder(mView, viewType);
        mView.setTag(mMyViewHolder);
        mView.setOnClickListener(this);
        return mMyViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(VIEW_TYPE_EMPTY == holder.viewType){
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

    @Override
    public void onClick(View v) {
        MyViewHolder holder = (MyViewHolder)v.getTag();
        int viewType = holder.viewType;
        if(VIEW_TYPE_EMPTY == viewType){
            int initialDataNumber = 5;
            DataBank.get(mActivity).generateData(initialDataNumber);
            mData = DataBank.get(mActivity).getData();
            setData(mData);
            notifyDataSetChanged();
            ALog.Log("onClick_Empty");
            return;
        }
        final int position = holder.getAdapterPosition();
        Dialog mDialog = new AlertDialog.Builder(mActivity)
                .setTitle(mActivity.getString(R.string.delete_item))
                .setNegativeButton(android.R.string.cancel,null)
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBank.get(mActivity).delCrime(mData.get(position));
                        mData = DataBank.get(mActivity).getData();
                        setData(mData);
                        notifyDataSetChanged();
                    }
                })
                .create();
        mDialog.show();
    }

}
