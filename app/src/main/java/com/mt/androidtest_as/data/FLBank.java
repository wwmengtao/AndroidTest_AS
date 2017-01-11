package com.mt.androidtest_as.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/22.
 * FLBank:专门用于产生显示在主页上的Fragment等组件名称
 */

public class FLBank {
    private static Context mContext = null;
    private static volatile FLBank mDataBank = null;
    private List<String> mData = null;
    public static final String[] componentNameList={
        "Fragments:","MyRecyclerViewFragment","MyListViewFragment","MyListViewTestFragment",
        "Activities:","BeatBoxActivity"
    };
    private FLBank(Context context){
        mContext = context.getApplicationContext();
        generateData();
    }

    public static FLBank get(Context context){
        if(null == mDataBank){
            synchronized (FLBank.class){
                if(null == mDataBank){
                    mDataBank = new FLBank(context);
                }
            }
        }
        return mDataBank;
    }

    private void generateData(){
        mData = new ArrayList<String>();
        for(int i=0;i<componentNameList.length;i++){
            mData.add(componentNameList[i]);
        }
    }

    public List<String> getData(){
        return mData;
    }
}
