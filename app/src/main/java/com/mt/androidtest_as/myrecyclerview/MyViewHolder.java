package com.mt.androidtest_as.myrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mt.androidtest_as.R;

/**
 * RecyclerView把ViewHolder作为缓存的单位
 * Created by Mengtao1 on 2016/12/22.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    public View mView = null;
    public int viewType = -1;
    public TextView mTextView = null;
    public MyViewHolder(View itemView) {
        this(itemView, -999);
    }

    public MyViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
        mView = itemView;
        mTextView = (TextView)itemView.findViewById(R.id.tv);
    }

}
