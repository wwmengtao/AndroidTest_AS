package com.mt.androidtest_as.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class FLBank {
    private static Context mContext = null;
    private static volatile FLBank mDataBank = null;
    private List<String> mData = null;
    public static final String[] fragmentNameList={
      "MyRecyclerViewFragment"
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
        for(int i=0;i<fragmentNameList.length;i++){
            mData.add(fragmentNameList[i]);
        }
    }

    public List<String> getData(){
        return mData;
    }
}