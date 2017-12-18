package com.example.protoui.travelmode.recyclerviewinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protoui.ALog;
import com.example.protoui.R;
import com.example.protoui.travelmode.route.RouteInfo;


/**
 * RecyclerView把ViewHolder作为缓存的单位
 * Created by Mengtao1 on 2016/12/22.
 */

public class SAppHolder extends RecyclerView.ViewHolder {
    private Context mContext = null;
    public View rootView = null;
    private ImageView mImageView = null;
    private TextView mTitle = null;
    private TextView mSummary = null;
    private TextView mDescription = null;

    public SAppHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext().getApplicationContext();
        rootView = itemView;
        mImageView = (ImageView)itemView.findViewById(R.id.suggestedappitem);
        mTitle = (TextView)itemView.findViewById(R.id.title);
        mSummary = (TextView)itemView.findViewById(R.id.summary);
        mDescription = (TextView)itemView.findViewById(R.id.description);
    }

    private void setTextViewColor(int color){
        mTitle.setTextColor(color);
        mSummary.setTextColor(color);
        mDescription.setTextColor(color);
    }

    public void bindData(SAppInfo data){
        try {
            String title = data.getTitle();
            ALog.Log("bindData: "+data.getDrawable().toString());
            mImageView.setBackground(data.getDrawable());
            mTitle.setText(title);
            ALog.Log("bindData title:"+title);
            if(data.getInfoType() == RouteInfo.InfoType.LYFT){
                rootView.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                setTextViewColor(mContext.getResources().getColor(android.R.color.black));
            }else if(data.getInfoType() == RouteInfo.InfoType.UBER){
                rootView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                setTextViewColor(mContext.getResources().getColor(android.R.color.white));
            }
            mSummary.setText(mContext.getString(R.string.waiting_time, data.getSummary()));
            mDescription.setText(data.getDescription());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
