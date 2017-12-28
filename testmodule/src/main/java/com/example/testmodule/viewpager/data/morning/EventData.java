package com.example.testmodule.viewpager.data.morning;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengtao1 on 2017/12/27.
 */

public class EventData {
    private String title0 = null;
    private String title = null;
    private String time = null;
    private String place = null;
    private String pic = null;
    private String backColor = null;

    public EventData(String title0, String title, String time, String place, String pic, String backColor){
        this.title0 = title0;
        this.title = title;
        this.time = time;
        this.place = place;
        this.pic = pic;
        this.backColor = backColor;
    }


    public String getTitle0(){
        return this.title0;
    }

    public String getTitle(){
        return this.title;
    }

    public String getTime(){
        return this.time;
    }

    public String getPlace(){
        return this.place;
    }

    public String getPic(){
        return this.pic;
    }

    public String getBackColor(){
        return this.backColor;
    }

    //Mock this morning data
    private static final String PREFIX_ASSET = "file:///android_asset/";
    private static String[][] originEventDataTM={
            {"10:30\nAM","Malcolm Young Presentation","10:30AM - 11:00AM","UX Room","morning/pics/tm/0.png", "WHITE"},
            {"11:00\nAM","UX Design Review - Americas","11:00AM - 11:30AM","Online Meeting","morning/pics/tm/1.png", "WHITE"},
            {"11:30\nAM","Lunch with Toni","11:30AM - 1:00PM","The J.Parker",null, "RED"},
    };

    public static List<EventData> getEventDataTM (){
        return getData(originEventDataTM);
    }

    //Mock later today data
    private static String[][] originEventDataLT={
            {"11:00\nPM","Flight to Berlin","11:00PM - 1:00PM(Local Time)","O'Hare International Airpot",
                    "morning/pics/lt/0.png", "BLUE"},
    };

    public static List<EventData> getEventDataLT (){
        return getData(originEventDataLT);
    }

    private static List<EventData> getData(String[][] originData){
        List<EventData> data = new ArrayList<>();
        EventData mEventData = null;
        for(int i=0; i < originData.length; i++){
            mEventData = new EventData(originData[i][0], originData[i][1], originData[i][2],
                    originData[i][3],
                    (null == originData[i][4]) ? null : PREFIX_ASSET + originData[i][4],
                    originData[i][5]);//Init the glide loading url
            data.add(mEventData);
        }
        return data;
    }
}
