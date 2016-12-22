package com.mt.androidtest_as.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mengtao1 on 2016/12/22.
 */

public class DataBank {
    private static Context mContext = null;
    private static volatile DataBank mDataBank = null;
    private List<String> mData = null;

    private DataBank(Context context){
        mContext = context.getApplicationContext();
        mData = new ArrayList<String>();
    }

    public static DataBank getDataBank(Context context){
        if(null == mDataBank){
            synchronized (DataBank.class){
                if(null == mDataBank){
                    mDataBank = new DataBank(context);
                }
            }
        }
        return mDataBank;
    }

    public List<String> getData(int count){
        if(count<=0)return null;
        mData.clear();
        for(int i=0;i<count;i++){
            mData.add("item: "+i);
        }
        return mData;
    }
}
