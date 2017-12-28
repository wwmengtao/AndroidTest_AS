package com.example.testmodule.viewpager.data.morning;


import com.example.testmodule.ALog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengtao1 on 2017/12/27.
 */

public class TrendingData {
    private String title = null;
    private String summary = null;
    private String pic = null;

    public TrendingData(String title, String summary, String pic){
        this.title = title;
        this.summary = summary;
        this.pic = pic;
    }

    public String getTitle(){
        return this.title;
    }

    public String getSummary(){
        return this.summary;
    }

    public String getPic(){
        return this.pic;
    }

    //Mock this trending data
    private static final String PREFIX_ASSET = "file:///android_asset/";
    private static String[][] originTrendingData={
            {"Sports","Villanova remains No. 1 in the latest USA TODAY men's basketball poll", "trending/pics/0.png"},
            {"Sports","USA TODAY Sports' Week 16 NFL picks","trending/pics/1.png"},
            {"Technology","4 Technology Trends That Will Transform Our World in 2018","trending/pics/2.png"},
            {"Financial","The Bitcoin Slump Is Beginning to Hurt the Stock Market, Says Wells Fargo","trending/pics/3.png"},
    };

    public static List<TrendingData> getTrendingData (List<TrendingData> data){
        return getData(originTrendingData);
    }

    private static List<TrendingData> getData(String[][] originData){
        List<TrendingData> data = new ArrayList<>();
        TrendingData mTrendingData = null;
        for(int i=0; i < originData.length; i++){
            mTrendingData = new TrendingData(originData[i][0], originData[i][1],
                    (null == originData[i][2]) ? null : PREFIX_ASSET + originData[i][2]);//Init the glide loading url
            ALog.Log("initData： "+originData[i][1]);
            data.add(mTrendingData);
        }
        return data;
    }
}
