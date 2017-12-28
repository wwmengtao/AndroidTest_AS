package com.example.testmodule.viewpager.data.morning;

import android.content.Context;

import java.util.List;

/**
 * Created by mengtao1 on 2017/12/27.
 */

public class MockDataEngine {
    private volatile static MockDataEngine mMockDataEngine = null;
    private Context mContext = null;
    //
    private List<EventData> mEventDataTM = null;
    private List<EventData> mEventDataLT = null;
    //
    private List<TrendingData> mTrendingData = null;
    //
    private List<SocialData> mSocialData = null;

    public static synchronized MockDataEngine getInstance(Context mContext) {
        if (null == mMockDataEngine) {
            mMockDataEngine = new MockDataEngine(mContext);
        }
        return mMockDataEngine;
    }

    private MockDataEngine(Context mContext){
        this.mContext = mContext;
    }

    public synchronized List<EventData> getEventDataTM(){
        if(null == this.mEventDataTM){
            this.mEventDataTM = EventData.getEventDataTM();
        }
        return this.mEventDataTM;
    }

    public synchronized List<EventData> getEventDataLT(){
        if(null == this.mEventDataLT){
            this.mEventDataLT = EventData.getEventDataLT();
        }
        return this.mEventDataLT;
    }

    public synchronized List<TrendingData> getTrendingData(){
        if(null == this.mTrendingData){
            this.mTrendingData = TrendingData.getTrendingData(this.mTrendingData);
        }
        return this.mTrendingData;
    }

    public synchronized List<SocialData> getSocialData(){
        if(null == this.mSocialData){
            this.mSocialData = SocialData.getSocialData();
        }
        return this.mSocialData;
    }

    public void clear(){
        if(null == mMockDataEngine)return;
        if(null == mEventDataTM)return;
        clearAll(mEventDataTM);
        clearAll(mEventDataLT);
        clearAll(mTrendingData);
        clearAll(mSocialData);
        mMockDataEngine = null;
    }

    private <T> void clearAll(List<T> data){
        if(null != data){
            data.clear();
            data = null;
        }
    }
}
