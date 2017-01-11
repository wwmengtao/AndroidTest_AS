package com.mt.myapplication.criminalintent.mydefinedaboutrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mengtao1 on 2016/12/19.
 */

public class MyRecyclerView extends RecyclerView {

    /**
     * 当数据为空时展示的View
     */
    private View mEmptyView;

    /**
     * 创建一个观察者
     * 为什么要在onChanged里面写？
     * 因为每次notifyDataChanged的时候，系统都会调用这个观察者的onChange函数
     * 我们大可以在这个观察者这里判断我们的逻辑，就是显示隐藏
     */
    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {


        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            //这种写发跟之前我们之前看到的ListView的是一样的，判断数据为空否，在进行显示或者隐藏
            if (adapter != null && mEmptyView != null) {
//                ALog.Log("onChanged:"+adapter.getItemCount());
                if (adapter.getItemCount() == 0 && View.VISIBLE != mEmptyView.getVisibility()) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    setVisibility(View.GONE);
                } else if(adapter.getItemCount() > 0 && View.GONE != mEmptyView.getVisibility()){
                    mEmptyView.setVisibility(View.GONE);
                    setVisibility(View.VISIBLE);
                }
            }

        }
    };

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 依赖注入
     *
     * @param emptyView 展示的空view
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            //这里用了观察者模式，同时把这个观察者添加进去，
            // 至于这个模式怎么用，谷歌一下，不多讲了，因为这个涉及到了Adapter的一些原理，感兴趣可以点进去看看源码，还是受益匪浅的
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        //当setAdapter的时候也调一次
        emptyObserver.onChanged();
    }
}
