package com.example.protoui.travelmode.recyclerviewinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.protoui.R;

import java.util.List;

/**
 * Created by Mengtao1 on 2017/11/26.
 */

public class SAppAdapter extends RecyclerView.Adapter<SAppHolder>{

    private Context mContext = null;
    private List<SAppInfo> mData = null;
    private View.OnClickListener mOnClickListener = null;

    public SAppAdapter(Context mContext, View.OnClickListener mOnClickListener){
        this.mContext = mContext;
        this.mOnClickListener = mOnClickListener;
        mData = SAppInfo.getInitData(mContext);
    }

    public void setData(List<SAppInfo> mData){
        this.mData = mData;
    }

    @Override
    public SAppHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_suggested2_app, parent, false);
        SAppHolder mMyViewHolder = new SAppHolder(mView);
        return mMyViewHolder;
    }

    @Override
    public void onBindViewHolder(SAppHolder holder, int position) {
        SAppInfo data = mData.get(position);
        holder.bindData(data);
        //
        View rootView = holder.rootView;
        rootView.setTag(data.getSIntent());
        rootView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
