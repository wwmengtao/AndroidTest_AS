package com.example.testmodule.viewpager.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testmodule.ALog;
import com.example.testmodule.R;
import com.example.testmodule.viewpager.data.morning.TrendingData;

import java.util.LinkedList;
import java.util.List;

/**
 * VPTAdapter：用于缓存删除的视图重复使用
 * Created by mengtao1 on 2017/11/29.
 */

public class VPTAdapter extends PagerAdapter {
    private Context mContext = null;
    private List<TrendingData> data = null;
    private LinkedList<View> mViewCache = null;//用于ViewPager销毁视图的回收利用

    public VPTAdapter(Context mContext){
        this.mContext = mContext;
        this.mViewCache = new LinkedList<>();
    }

    public void setData(List<TrendingData> list) {
        data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        ALog.Log("isViewFromObject: "+(view == object));
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ALog.Log1("instantiateItem: "+position);
        TrendingData mTrendingData = data.get(position);
        View mView = null;
        if(mViewCache.size() == 0){//说明此前没有执行destroyItem操作，因此所需视图无法通过回收获取，只能新建
            mView = LayoutInflater.from(mContext).inflate(R.layout.trending_item ,
                    container ,false);
        }else {
            mView = mViewCache.removeFirst();//遵循先销毁先显示的原则，返回队列第一个元素
        }
        bindData(mView, mTrendingData);
        container.addView(mView);
        return mView;
    }

    private void bindData(View mView, TrendingData data){
        TextView titleTV = mView.findViewById(R.id.title);
        TextView summaryTV = mView.findViewById(R.id.summary);
        ImageView imageView = mView.findViewById(R.id.pic);
        String title = data.getTitle();
        String summary = data.getSummary();
        ALog.Log("summary_summary: "+summary);
        titleTV.setText((null != title)? title : mContext.getString(R.string.title));
        summaryTV.setText((null != summary)? summary : mContext.getString(R.string.title));
        //
        String pic = data.getPic();
        if(null == pic)return;
        Glide.with(mContext)
                .load(pic)//load assets pic
                .into(imageView);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        ALog.Log1("destroyItem: "+position);
        mViewCache.add((View) object);//此时将回收的视图缓存起来
    }

}
