package com.example.testmodule.viewpager.data.morning;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengtao1 on 2017/12/27.
 */

public class SocialData {
    private String title = null;
    private String summary = null;
    private String pic = null;

    public SocialData(String title, String summary, String pic){
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

    //Mock this social data
    private static final String PREFIX_ASSET = "file:///android_asset/";
    private static String[][] originSocialData={
            {"Recently engaged","Jeff Huang and Stephanie Leung got engaged Yesterday, December 5 at New York", "social/pics/0.png"},
            {"Instagram comments","fabeereis mentioned you in a comment:\n@dedee miss you a lot!","social/pics/1.png"},
            {"Nearby Friends","Stephanie Huang and 5 friends are near Merchandise Mart","social/pics/2.png"},
    };

    public static List<SocialData> getSocialData(){
        return getData(originSocialData);
    }

    private static List<SocialData> getData(String[][] originData){
        List<SocialData> data = new ArrayList<>();
        SocialData mSocialData = null;
        for(int i=0; i < originData.length; i++){
            mSocialData = new SocialData(originData[i][0], originData[i][1],
                    (null == originData[i][2]) ? null : PREFIX_ASSET + originData[i][2]);//Init the glide loading url
            data.add(mSocialData);
        }
        return data;
    }
}
