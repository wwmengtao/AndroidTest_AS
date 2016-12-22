package com.mt.androidtest_as.myrecyclerview;

import android.app.Activity;
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

public class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder>{
    private Activity mActivity = null;
    private List<String> mData = null;

    public BaseAdapter(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void setData(List<String> mData){
        this.mData = mData;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(null == mData||0 == mData.size()){
            return null;
        }
        View mView = LayoutInflater.from(mActivity).inflate(R.layout.list_item, parent, false);
        BaseViewHolder mBaseViewHolder = new BaseViewHolder(mView);
        return mBaseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(null == mData||0 == mData.size()){
            return;
        }
        TextView mTextView = holder.mTextView;
        mTextView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if(null == mData || 0 == mData.size()){
            return 0;
        }
        return mData.size();
    }
}
