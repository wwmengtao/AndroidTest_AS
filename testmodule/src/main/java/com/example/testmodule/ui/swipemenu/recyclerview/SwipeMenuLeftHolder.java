package com.example.testmodule.ui.swipemenu.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testmodule.R;
import com.example.testmodule.ui.swipemenu.data.RVData;


/**
 * Created by Mengtao1 on 2018/02/07.
 */

public class SwipeMenuLeftHolder extends RecyclerView.ViewHolder {
    private Context mContext = null;
    private int viewType = -1;
    public View rootView = null;
    //
    private RelativeLayout normalView = null;
    private LinearLayout dividerView = null;
    //
    private TextView titleTV = null;
    private TextView summaryTV = null;

    private ImageView picIV = null;


    public SwipeMenuLeftHolder(View itemView, int viewType) {
        super(itemView);
        this.mContext = itemView.getContext().getApplicationContext();
        this.viewType = viewType;
        this.rootView = itemView;
        this.normalView = itemView.findViewById(R.id.normal_view);
        this.dividerView = itemView.findViewById(R.id.divider_view);
        this.picIV = itemView.findViewById(R.id.iv_swipe_menu_left_rv);
        this.titleTV = itemView.findViewById(R.id.tv_swipe_menu_left_rv);
        this.summaryTV = itemView.findViewById(R.id.tv_swipe_menu_left_rv_summary);
    }

    public void bindData(RVData data){
        if(SwipeMenuLeftAdapter.VIEW_TYPE_DIVIDER == viewType){
            normalView.setVisibility(View.GONE);
            dividerView.setVisibility(View.VISIBLE);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)rootView.getLayoutParams();
            params.height = 10;
            rootView.setLayoutParams(params);
            return;
        }
        String title = data.getTitle();
        titleTV.setText((null != title)? title : mContext.getString(R.string.title));
        titleTV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//1.set bold text

        String summary = data.getSummary();
        summaryTV.setText((null != summary)? summary : mContext.getString(R.string.summary));
        if(title.equals("About")){
            summaryTV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            summaryTV.setTextSize(12);
        }
        //
        if(SwipeMenuLeftAdapter.VIEW_TYPE_TITLE == viewType){
            picIV.setVisibility(View.GONE);
            return;
        }
        //
        int pic = data.getPic();
        if(pic < 0)return;
        Glide.with(mContext)
                .load(data.getPic())
                .into(picIV);
        picIV.setColorFilter(Color.BLACK);//2.set pic color, not ImageView background, or android:tint="@color/black"
    }

    public View getRootView(){
        return this.rootView;
    }

    public int getViewType(){
        return viewType;
    }
}
