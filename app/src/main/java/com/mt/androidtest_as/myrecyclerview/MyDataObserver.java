package com.mt.androidtest_as.myrecyclerview;

import android.support.v7.widget.RecyclerView;

import com.mt.androidtest_as.ALog;

/**
 * DataObserver:用于监听Adapter数据变化
 * Created by Mengtao1 on 2016/12/23.
 */

public class MyDataObserver extends RecyclerView.AdapterDataObserver {
    public MyDataObserver(){

    }

    @Override
    public void onChanged() {
        ALog.Log("Adapter data changed!");
    }
}
