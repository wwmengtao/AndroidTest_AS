package com.example.testmodule.ui.swipemenu.data;


import com.example.testmodule.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengtao1 on 2018/02/07.
 */

public class RVData {
    private String title = null;
    private String summary = null;
    private int pic = -1;

    public RVData(String title, int pic){
        this.title = title;
        this.pic = pic;
    }

    public RVData(String title, String summary){
        this.title = title;
        this.summary = summary;
    }

    public String getTitle(){
        return this.title;
    }

    public String getSummary(){
        return this.summary;
    }

    public void setPic(int pic){
        this.pic = pic;
    }

    public int getPic(){
        return this.pic;
    }

    public static final String DIVIDER_RV = "RecyclerView_D_I_V_I_D_E_R";
    public static final int NO_PIC_ID = -1234;
    private static String[] TITLES={
            "Context Menu", "Moto Sleep", "Moto Key", "Moto Voice", "Moto Display", "Moto Actions", "Moto Place",
            DIVIDER_RV,
            "Moto Beta", "Call into meeting", "Battery performance", "Maximize storage"
    };

    private static int[] PICS={
            R.drawable.ic_place, R.drawable.ic_place, R.drawable.ic_key, R.drawable.ic_voice, R.drawable.ic_display, R.drawable.ic_actions, R.drawable.ic_place,
            NO_PIC_ID,
            NO_PIC_ID, R.drawable.ic_place, R.drawable.ic_place, R.drawable.ic_place
    };

    private static String[][] TITLES_SUMMARIES={
            {"Main Layout", "Return to main layout"},
            {"For you", "Lorem ipsum dolor sit amet, \nconsectetur adipiscing elit nullam felis"},
            {"Moto Actions", "Use simple gestures to control \nyour phone"},
            {"Moto Moments","Lorem ipsum dolor sit amet, \nconsectetur adipiscing elit nullam"},
            {"Moto Voice","Get a command and your phone \nresponds. Great for hands-free use"},
            {"Moto Display","Get discreet notifications and \nnight settings"},
            {"Moto Performance","Lorem ipsum dolor sit amet, \nconsectetur adipiscing elit nullam"},
            {"Moto Sound","Lorem ipsum dolor sit amet, \nconsectetur adipiscing elit nullam"},
            {"Moto Key","Access websites, apps and devices \nwith your fingerprint"},
            {"Cross share","Lorem ipsum dolor sit amet, \nconsectetur adipiscing elit nullam"},
            {DIVIDER_RV, DIVIDER_RV},
            {"About","Moto: version 6.0.60"},
    };

    public static List<RVData> getData(){
        List<RVData> dataList = new ArrayList<>();
        RVData data;
        for(int i = 0; i < TITLES_SUMMARIES.length; i++){
            data = new RVData(TITLES_SUMMARIES[i][0], TITLES_SUMMARIES[i][1]);
            if(TITLES_SUMMARIES[i][0].equals(DIVIDER_RV)){
                data.setPic(NO_PIC_ID);
            }else {
                data.setPic(R.drawable.ic_place);
            }
            dataList.add(data);
        }
        return dataList;
    }
}
