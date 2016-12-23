package com.mt.androidtest_as.myrecyclerview;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt.androidtest_as.ALog;
import com.mt.androidtest_as.MainActivity;
import com.mt.androidtest_as.R;
import com.mt.androidtest_as.data.BaseData;
import com.mt.androidtest_as.data.DataBank;
import com.mt.androidtest_as.data.FLBank;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class FLAdapter extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener{
    private Activity mActivity = null;
    private List<String> mData = null;
    public FLAdapter(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void setData(List<String> mData){
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_function, parent, false);
        MyViewHolder mMyViewHolder = new MyViewHolder(mView, viewType);
        mView.setOnClickListener(this);
        mView.setTag(mMyViewHolder);
        return mMyViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView mTextView = holder.mTextView;
        mTextView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        int position = ((MyViewHolder)v.getTag()).getAdapterPosition();
        Intent mIntent = MainActivity.newIntent(mActivity,position);
        mActivity.startActivity(mIntent);
    }

}
