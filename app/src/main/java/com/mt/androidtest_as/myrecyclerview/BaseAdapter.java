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

public class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> implements View.OnClickListener{
    private Activity mActivity = null;
    private List<BaseData> mData = null;

    public BaseAdapter(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void setData(List<BaseData> mData){
        this.mData = mData;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(null == mData||0 == mData.size()){
            return null;
        }
        View mView = LayoutInflater.from(mActivity).inflate(R.layout.list_item, parent, false);
        BaseViewHolder mBaseViewHolder = new BaseViewHolder(mView);
        mView.setOnClickListener(this);
//        int position = mBaseViewHolder.getAdapterPosition();//position: Can noly get -1
//        ALog.Log("position:"+position);
        return mBaseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(null == mData||0 == mData.size()){
            return;
        }
        holder.mView.setTag(position);
        TextView mTextView = holder.mTextView;
        mTextView.setText(mData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if(null == mData || 0 == mData.size()){
            return 0;
        }
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        final int position = (int)v.getTag();
        Dialog mDialog = new AlertDialog.Builder(mActivity)
                .setTitle(mActivity.getString(R.string.delete_item))
                .setNegativeButton(android.R.string.cancel,null)
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBank.get(mActivity).delCrime(mData.get(position));
                        mData = DataBank.get(mActivity).getData();ALog.Log("size:"+mData.size());
                        setData(mData);
                        notifyDataSetChanged();
                    }
                })
                .create();
        mDialog.show();
    }
}
