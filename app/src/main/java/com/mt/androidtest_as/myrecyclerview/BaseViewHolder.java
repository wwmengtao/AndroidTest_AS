package com.mt.androidtest_as.myrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mt.androidtest_as.R;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextView = null;
    public BaseViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView)itemView.findViewById(R.id.tv);
    }
}
