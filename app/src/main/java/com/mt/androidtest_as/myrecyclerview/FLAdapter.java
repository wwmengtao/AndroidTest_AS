package com.mt.androidtest_as.myrecyclerview;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mt.androidtest_as.R;

import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class FLAdapter extends RecyclerView.Adapter<MyRvViewHolder>{
    private Fragment mFragment = null;
    private List<String> mData = null;
    private View.OnClickListener mOnClickListener = null;
    public FLAdapter(Fragment mFragment){
        this.mFragment = mFragment;
    }

    public void setData(List<String> mData){
        this.mData = mData;
    }

    @Override
    public MyRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mFragment.getContext()).inflate(R.layout.list_item_function, parent, false);
        MyRvViewHolder mMyViewHolder = new MyRvViewHolder(mView, viewType);
        mView.setOnClickListener(mOnClickListener);
        mView.setTag(mMyViewHolder);
        return mMyViewHolder;
    }

    @Override
    public void onBindViewHolder(MyRvViewHolder holder, int position) {
        TextView mTextView = holder.mTextView;
        mTextView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnClickListener(View.OnClickListener l){
        mOnClickListener = l;
    }

}
