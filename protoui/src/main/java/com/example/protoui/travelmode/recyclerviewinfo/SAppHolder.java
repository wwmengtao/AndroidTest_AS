package com.example.protoui.travelmode.recyclerviewinfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView mDistance = null;
    private TextView mPrice = null;
    private TextView mWaitTime = null;

    public SAppHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext().getApplicationContext();
        rootView = itemView;
        mImageView = (ImageView)itemView.findViewById(R.id.suggestedappitem);
        mTitle = (TextView)itemView.findViewById(R.id.title);
        mDistance = (TextView)itemView.findViewById(R.id.distance);
        mPrice = (TextView)itemView.findViewById(R.id.price);
        mWaitTime = (TextView)itemView.findViewById(R.id.waiting_time);
    }

    private void setTextViewColor(int color){
        mTitle.setTextColor(color);
        mDistance.setTextColor(color);
        mPrice.setTextColor(color);
        mWaitTime.setTextColor(color);
    }

    public void bindData(SAppInfo data){
        Drawable drawable = data.getDrawable();
        String title = data.getTitle();
        String distance = data.getDistance();
        String price = data.getPrice();
        String waitingTime = data.getWaitingTime();
        mImageView.setBackground((null != drawable) ? drawable : mContext.getDrawable(R.drawable.googleg_color));
        mTitle.setText((null != title)? title : mContext.getString(R.string.title));
        mDistance.setText(mContext.getString(R.string.final_distance,
                (null != distance) ? distance : mContext.getString(R.string.distance)));
        mPrice.setText((null != price)? price : mContext.getString(R.string.price));
        mWaitTime.setText(mContext.getString(R.string.waiting_time,
                (null != waitingTime) ? waitingTime : mContext.getString(R.string.time)));
        //
        if(data.getInfoType() == RouteInfo.InfoType.LYFT){
            rootView.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
            setTextViewColor(mContext.getResources().getColor(android.R.color.black));
        }else if(data.getInfoType() == RouteInfo.InfoType.UBER){
            rootView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            setTextViewColor(mContext.getResources().getColor(android.R.color.white));
        }
    }
}
